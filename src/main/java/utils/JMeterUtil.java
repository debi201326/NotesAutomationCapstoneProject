package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JMeterUtil {

        // Absolute path to the JMeter executable used by the runner.
        static String JMETER_PATH = "C:\\Users\\debi2\\Downloads\\apache-jmeter-5.6.3\\bin\\jmeter.bat";

        public static void runJMeter(String testPlan, String resultFile) throws Exception {

                // Ensure the output directory exists before launching JMeter.
                Files.createDirectories(Paths.get("target/jmeter"));
                System.out.println("[JMETER] Running: " + testPlan);

                // Build the command to execute JMeter in non-GUI mode with the specified test
                // plan and result file.
                ProcessBuilder pb = new ProcessBuilder(
                                JMETER_PATH,
                                "-n",
                                "-t", testPlan,
                                "-l", resultFile);

                pb.redirectErrorStream(true);
                Process process = pb.start();
                // Capture and print JMeter's console output in real-time for better visibility.
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                }
                if (process.waitFor() != 0) {
                        throw new RuntimeException("[JMETER] Execution failed!");
                }
                System.out.println("[JMETER] Done. Results saved at: " + resultFile);
        }

        public static void printPerformanceSummary(
                        String resultFile,
                        String label) throws Exception {

                // Read the JMeter results file and parse each CSV line.
                List<String> lines = Files.readAllLines(Paths.get(resultFile));
                // Calculate total, min, max, and average response times.
                long total = 0;
                long min_time = Long.MAX_VALUE;
                long max_time = Long.MIN_VALUE;
                int count = 0;

                for (String line : lines) {
                        // Skip header rows and empty lines to avoid parsing errors.
                        if (line.contains("elapsed") || line.trim().isEmpty()) {
                                continue;
                        }
                        String[] data = line.split(",");
                        long responseTime = Long.parseLong(data[1]);
                        total += responseTime;
                        if (responseTime < min_time) {
                                min_time = responseTime;
                        }
                        if (responseTime > max_time) {
                                max_time = responseTime;
                        }
                        count++;
                }
                long avg = count > 0 ? total / count : 0;
                // Print a clear performance summary to the console for quick analysis.
                System.out.println("[JMETER] PERFORMANCE SUMMARY : " + label);
                System.out.println("Total Requests : " + count);
                System.out.println("Average Time   : " + avg + " ms");
                System.out.println("Min Time       : " + min_time + " ms");
                System.out.println("Max Time       : " + max_time + " ms");
        }
}