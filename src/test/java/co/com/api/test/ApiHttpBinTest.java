package co.com.api.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ApiHttpBinTest extends PageBase {

    @Test
    @DisplayName("Check get response and validate property")
    public void apiGetTest() {

        given()
                .when()
                .get("/ip")
                .then().statusCode(HttpStatus.SC_OK) //check status code
                .body(containsString("origin"));//check body contain origin;

    }

    @Test
    @DisplayName("Check get response and validate property")
    public void apiGetWithQueryParamsTest() {

        given()

                .queryParam("name", "John")
                .queryParam("age", "31")
                .queryParam("city", "New York")
                .when()
                .get("/get");
                //.then()
                //.statusCode(HttpStatus.SC_OK)
                //.body("args.name", equalTo("John"))
                //.body("args.age", equalTo("31"))
                //.body("args.city", equalTo("New York"));


    }

    @Test
    @DisplayName("Consume services with POST")

    public void apiPostTest() {
        String payload = "{\"args\":{\"age\":\"16\",\"city\":\"Salento\",\"name\":\"Dairo\"}}";

        given()
                .when()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .post("/post")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    @DisplayName("Consume services with Delete")

    public void apiDeleteTest() {

        String payload = "{\"args\":{\"age\":\"16\",\"city\":\"Salento\",\"name\":\"Dairo\"}}";

        given()
                .when()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .delete("/delete")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Consume services with Patch")

    public void apiPatchTest() {

        String payload = "{\"args\":{\"city\":\"Bogota\"}}";
        given()
                .when()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .patch("/patch")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    @DisplayName("Consume services with PUT")

    public void apiPutTest() {

        String payload = "{\"args\":{\"age\":\"18\",\"city\":\"Salento\",\"name\":\"Dairo\"}}";

        given()
                .when()
                .contentType(ContentType.JSON)
                .when()
                .body(payload)
                .when()
                .put("/put")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }


}
