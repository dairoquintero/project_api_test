package co.com.api.test;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ApiHttpBinTest {



    @BeforeAll
    public static void init() throws FileNotFoundException {
        RestAssured.requestSpecification = defaultRequestSpecification();
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

    }

    private static RequestSpecification defaultRequestSpecification() throws FileNotFoundException {

        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());
        filters.add(new AllureRestAssured());

        return new RequestSpecBuilder().setBaseUri(RestAssured.baseURI = "https://httpbin.org")
                .addFilters(filters)
                .build();
    }

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
                .contentType(ContentType.JSON)
                .get("/get")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("args.name", equalTo("John"))
                .body("args.age", equalTo("31"))
                .body("args.city", equalTo("New York"));


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
                .statusCode(HttpStatus.SC_CREATED);

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
