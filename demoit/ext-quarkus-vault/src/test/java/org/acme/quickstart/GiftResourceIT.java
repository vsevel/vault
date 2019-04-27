package org.acme.quickstart;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GiftResourceIT {

    @Test
    public void testGiftEndpoint() {
        given()
                .when().get("/gift")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

}