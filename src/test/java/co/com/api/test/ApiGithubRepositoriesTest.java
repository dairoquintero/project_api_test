package co.com.api.test;

import static io.restassured.RestAssured.given;
import static org.codehaus.groovy.runtime.EncodingGroovyMethods.md5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import co.com.api.test.dto.GithubRepoDto;
import co.com.api.test.dto.GithubRepoListDto;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
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
  public void apiGetRepositoryFilterDownloadRepoTest() throws
    IOException, NoSuchAlgorithmException {
    final String expectedMd5 = "3bb456d1ece265eb37471d1d870647f7";
    Response response = given()
      .when()
      .contentType(ContentType.JSON)
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .get("repos");

    List<GithubRepoDto> values = response
      .then()
      .extract()
      .body()
      .jsonPath()
      .getList("findAll { it.name == 'project_api_test' }", GithubRepoDto.class);

    byte[] valueArray = given()
      .basePath("")
      .urlEncodingEnabled(false)
      .baseUri(values.get(0).getSvn_url())
      .when()
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .get("/archive/" + values.get(0).getDefault_branch() + ".zip").asByteArray();

    Path currentRelativePath = Paths.get("");
    Path fileLocation = Paths.get("/src/test/resources/downloadFile.zip");
    ByteBuffer buffer = ByteBuffer.wrap(valueArray);
    System.out.println(currentRelativePath.toAbsolutePath().toString() + fileLocation.toString());
    OutputStream os = new FileOutputStream(new File(
      currentRelativePath.toAbsolutePath().toString() + fileLocation.toString()));
    os.write(valueArray);
    os.close();
    String md5Value = md5(fileLocation.toString());
    assertThat(expectedMd5, equalTo(md5Value));

  }

  @Test
  @DisplayName("Check get response with authentication And get repos List file")
  public void apiGetRepositoryFilterListFilesRepoTest() {
    String fileName = "LICENSE.txt";
    String shaFile = "bb0f726067e4130d7c19ee9128ca3b44397e41d2";
    GithubRepoListDto payload = GithubRepoListDto.builder()
      .name(fileName)
      .path(fileName)
      .sha(shaFile)
      .build();
    Response response = given()
      .when()
      .contentType(ContentType.JSON)
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .get("repos");

    List<GithubRepoDto> values = response
      .then()
      .extract()
      .body()
      .jsonPath()
      .getList("findAll { it.name == 'project_api_test' }", GithubRepoDto.class);

    Response responseList = given()
      .basePath("")
      .contentType(ContentType.JSON)
      .urlEncodingEnabled(false)
      .baseUri(values.get(0).getUrl())
      .when()
      .auth()
      .oauth2(System.getenv("ACCESS_TOKEN"))
      .get("/contents");

    List<GithubRepoListDto> responseJsonList = responseList
      .then()
      .extract()
      .body()
      .jsonPath()
      .getList("findAll { it.name == '" + fileName + "'}", GithubRepoListDto.class);
    assertThat(responseJsonList.get(0), equalTo(payload));
  }

}
