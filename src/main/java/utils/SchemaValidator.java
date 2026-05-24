package utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class SchemaValidator {

    public static void validate(Response response, String schemaFileName) {
        try {
            response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
                    "schemas/" + schemaFileName));
            System.out.println("[SCHEMA] Validation passed for: " + schemaFileName);
        } catch (Exception e) {
            System.out.println("[SCHEMA] Validation failed: " + e.getMessage());
            throw e;
        }
    }
}