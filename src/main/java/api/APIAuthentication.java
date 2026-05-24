
package api;

import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.CSVReader;

import java.util.Map;

public class APIAuthentication {

    public static String token;

    public static void generateToken() {

        Map<String, String> data = CSVReader.getRowByType("valid_user");
        String email    = data.get("email");
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
        return RestAssured.given()
            .baseUri(ConfigReader.get("api.base.url"))
            .header("x-auth-token", token)
            .contentType("application/json");
    }

    public static Response getWithRetry(String endpoint, int maxRetries) {
        int attempts  = 0;
        Response response = null;

        while (attempts < maxRetries) {
            response = getBaseSpec().get(endpoint);
            attempts++;

            if (response.statusCode() == 200) {
                System.out.println("[RETRY] Success on attempt: " + attempts);
                return response;
            }

            System.out.println("[RETRY] Attempt " + attempts + " failed with status: "
                + response.statusCode());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }

        System.out.println("[RETRY] All " + maxRetries + " attempts failed");
        return response;
    }
}