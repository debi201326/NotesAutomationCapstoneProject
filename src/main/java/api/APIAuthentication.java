package api;

import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.CSVReader;
import java.util.Map;

public class APIAuthentication {
    // shared token used by authenticated API requests
    public static String token;

    // generateToken reads valid credentials from CSV data and logs in to get an auth token
    public static void generateToken() {
        Map<String, String> data = CSVReader.getRowByType("valid_user");
        String email = data.get("email");
        String password = data.get("password");
        RestAssured.baseURI = ConfigReader.get("api.base.url");

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                .post("/users/login");

        token = response.jsonPath().getString("data.token");
        System.out.println("Token generated: " + token);
    }

    public static RequestSpecification getBaseSpec() {
        // Build a standard request specification with auth header and JSON content type
        return RestAssured.given()
                .baseUri(ConfigReader.get("api.base.url"))
                .header("x-auth-token", token)
                .contentType("application/json");
    }

    // Helper for retries in case a transient error occurs while fetching an endpoint
    public static Response getWithRetry(String endpoint, int maxRetries) {
        int attempts = 0;
        Response response = null;
        // Loop until we get a successful response or exceed retries
        while (attempts < maxRetries) {
            response = getBaseSpec().get(endpoint);
            attempts++;
            if (response.statusCode() == 200) {
                System.out.println("[RETRY] Success on attempt: " + attempts);
                return response;
            }
            System.out.println("[RETRY] Attempt " + attempts + " failed with status: " + response.statusCode());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                // if sleep is interrupted, just continue retrying
            }
        }
        System.out.println("[RETRY] All " + maxRetries + " attempts failed");
        return response;
    }
}