package co.com.api.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import co.com.api.test.dto.PersonDto;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.log4j.varia.NullAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiHttpBinTest {
  @BeforeAll
  public static void init() throws FileNotFoundException {
    RestAssured.requestSpecification = defaultRequestSpecificationHttpBin();
    org.apache.log4j.BasicConfigurator.configure(new NullAppender());
  }

  private static RequestSpecification defaultRequestSpecificationHttpBin()
    throws FileNotFoundException {
    List<Filter> filters = new ArrayList<>();
    filters.add(new RequestLoggingFilter());
    filters.add(new ResponseLoggingFilter());
    filters.add(new AllureRestAssured());
    return new RequestSpecBuilder().setBaseUri(RestAssured.baseURI = "https://httpbin.org")
      .setBasePath("")
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
      .body(containsString("origin")); //check body contain origin
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
    PersonDto payload = PersonDto.builder()
      .name("John")
      .age("31")
      .city("New York")
      .build();
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
    PersonDto payload = PersonDto.builder()
      .name("John")
      .age("31")
      .city("New York")
      .build();
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
    PersonDto payload = PersonDto.builder()
      .name("John")
      .age("31")
      .city("New York")
      .build();
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
    PersonDto payload = PersonDto.builder()
      .name("John")
      .age("31")
      .city("New York")
      .build();
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
