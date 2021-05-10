package co.com.api.test;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class PageBase {

    @BeforeAll
    public static void init(){

        RestAssured.baseURI="https://httpbin.org";


    }
}
