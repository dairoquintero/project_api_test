package co.com.api.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.log4j.varia.NullAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiGithubTest {
  @BeforeAll
  public static void init() throws FileNotFoundException {
    RestAssured.requestSpecification = defaultRequestSpecification();
    org.apache.log4j.BasicConfigurator.configure(new NullAppender());
  }

  private static RequestSpecification defaultRequestSpecification() throws FileNotFoundException {
    final String  githubUsername = "dairoquintero";
    final String repository = "project_api_test";
    List<Filter> filters = new ArrayList<>();
    filters.add(new RequestLoggingFilter());
    filters.add(new ResponseLoggingFilter());
    filters.add(new AllureRestAssured());
    return new RequestSpecBuilder().setBaseUri(RestAssured.baseURI = "https://api.github.com")
      .setBasePath("/repos/" + githubUsername + "/" + repository)
      .addFilters(filters)
      .build();
  }

  @Test
  @DisplayName("Check get response with  validate property")
  public void apiGetTest() {
    given()
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .when()
      .get("")
      .then().statusCode(HttpStatus.SC_OK)//check status code
      .body("description", equalTo("This a project api test"));

  }

}
