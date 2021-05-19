package co.com.api.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import co.com.api.test.dto.GithubDto;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.log4j.varia.NullAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiGithubRepositoriesTest {
  @BeforeAll
  public static void init() throws FileNotFoundException {
    RestAssured.requestSpecification = defaultRequestSpecification();
    org.apache.log4j.BasicConfigurator.configure(new NullAppender());
  }

  private static RequestSpecification defaultRequestSpecification() throws FileNotFoundException {
    final String githubUsername = "dairoquintero";
    List<Filter> filters = new ArrayList<>();
    filters.add(new RequestLoggingFilter());
    filters.add(new ResponseLoggingFilter());
    filters.add(new AllureRestAssured());
    return new RequestSpecBuilder().setBaseUri(RestAssured.baseURI = "https://api.github.com")
      .setBasePath("/users/" + githubUsername)
      .addFilters(filters)
      .build();
  }

  @Test
  @DisplayName("Check get response with authentication and  validate property")
  public void apiGetRepositoryTest() {
    given()
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .when()
      .get("")
      .then().statusCode(HttpStatus.SC_OK)//check status code
      .body("name", equalTo("Dairo Quintero"))
      .body("company", equalTo("Perficient Latam"))
      .body("location", equalTo("Bogota"));

  }

  @Test
  @DisplayName("Check get response with authentication And get repos")
  public void apiGetRepositoryCheckRepoTest() {
    String expect = "project_api_test";
    given()
      .when()
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .get("repos")
      .then()
      .body("name", hasItem(expect));

  }

  @Test
  @DisplayName("Check get response with authentication And get repos and filter specific with find")
  public void apiGetRepositoryFilterRepoTest() {
    String username = "dairoquintero";
    boolean privateStatus = false;
    String description = "This a project api test";
    String expect = "project_api_test";
    given()
      .when()
      .contentType(ContentType.JSON)
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .get("repos")
      .then().body("find { it.name == 'project_api_test' }.name", equalTo(expect))
      .body("find { it.name == 'project_api_test' }.full_name", equalTo(username + "/" + expect))
      .body("find { it.name == 'project_api_test' }.private", is(privateStatus))
      .body("find { it.name == 'project_api_test' }.description", equalTo(description));

  }

  @Test
  @DisplayName("Check get response with authentication And get repos with filter download")
  public void apiGetRepositoryFilterDownloadRepoTest() {
    Response response = given()
      .when()
      .contentType(ContentType.JSON)
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .get("repos");
    List<GithubDto> values = response
      .then()
      .extract()
      .body()
      .jsonPath()
      .getList("findAll { it.name == 'project_api_test' }", GithubDto.class);

    given()
      .basePath("")
      .urlEncodingEnabled(false)
      .baseUri(values.get(0).getSvn_url())
      .when()
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .get("/archive/" + values.get(0).getDefault_Branch() + ".zip");
  }

}
