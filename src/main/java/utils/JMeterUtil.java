package utils;

import io.qameta.allure.Allure;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import config.ConfigReader;

public class JMeterUtil {

        static String JMETER_PATH = ConfigReader.get("jmeter.path");

        public static void runJMeter(String testPlan, String resultFile, String reportDir) throws Exception {

                Files.createDirectories(Paths.get("target/jmeter"));

                // delete old jtl file if exists
                File jtlFile = new File(resultFile);
                if (jtlFile.exists()) {
                        jtlFile.delete();
                        System.out.println("[JMETER] Deleted old result file: " + resultFile);
                }

                // delete old report folder if exists
                File reportFolder = new File(reportDir);
                if (reportFolder.exists()) {
                        deleteFolder(reportFolder);
                        System.out.println("[JMETER] Deleted old report folder: " + reportDir);
                }
                Files.createDirectories(Paths.get(reportDir));

                System.out.println("[JMETER] Running: " + testPlan);

                ProcessBuilder pb = new ProcessBuilder(
                                JMETER_PATH,
                                "-n",
                                "-t", testPlan,
                                "-l", resultFile,
                                "-e",
                                "-o", reportDir);

                pb.redirectErrorStream(true);
                Process process = pb.start();

                BufferedReader reader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                }

                if (process.waitFor() != 0) {
                        throw new RuntimeException("[JMETER] Execution failed!");
                }

                System.out.println("[JMETER] Done. Results: " + resultFile);
                System.out.println("[JMETER] HTML Report: " + reportDir + "/index.html");
        }

        private static void deleteFolder(File folder) {
                if (folder.listFiles() != null) {
                        for (File f : folder.listFiles()) {
                                if (f.isDirectory())
                                        deleteFolder(f);
                                else
                                        f.delete();
                        }
                }
                folder.delete();
        }

        public static void printPerformanceSummary(String resultFile, String label) throws Exception {

                List<String> lines = Files.readAllLines(Paths.get(resultFile));

                long total = 0;
                long min_time = Long.MAX_VALUE;
                long max_time = Long.MIN_VALUE;
                int count = 0;
                int errors = 0;

                for (String line : lines) {
                        if (line.contains("elapsed") || line.trim().isEmpty())
                                continue;
                        String[] data = line.split(",");
                        long responseTime = Long.parseLong(data[1]);
                        boolean success = data[7].equals("true");
                        total += responseTime;
                        if (responseTime < min_time)
                                min_time = responseTime;
                        if (responseTime > max_time)
                                max_time = responseTime;
                        if (!success)
                                errors++;
                        count++;
                }

                long avg = count > 0 ? total / count : 0;

                String summary = "[JMETER] PERFORMANCE SUMMARY : " + label + "\n"
                                + "Total Requests : " + count + "\n"
                                + "Average Time   : " + avg + " ms\n"
                                + "Min Time       : " + min_time + " ms\n"
                                + "Max Time       : " + max_time + " ms\n"
                                + "Error Count    : " + errors + "\n"
                                + "Error Rate     : " + (count > 0 ? (errors * 100 / count) : 0) + "%\n";

                System.out.println(summary);

                Allure.addAttachment(label + " JMeter Summary", "text/plain", summary, ".txt");
        }
}