import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReqResTest {

    @BeforeEach
    public void setUp(){
        RestAssured.baseURI ="https://reqres.in";  //Base path
        RestAssured.basePath="/api";  //name base for API
        RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter()); //We call to filter functions
        RestAssured.requestSpecification = new RequestSpecBuilder()  //Type of content
                .setContentType(ContentType.JSON)
                .build();

    }


    // POST METHOD
    @Test
    public void loginTest(){

             String response = RestAssured
                            .given()
                            .log().all() // We can see all the parameters
                            .body("{\n" +
                                    "    \"email\": \"eve.holt@reqres.in\",\n" +
                                    "    \"password\": \"cityslicka\"\n" +
                                    "}")                                        // Parameter request
                            .post("/login")   // path URL
                            .then()
                            .log().all() // We can see all the parameters but here is inside the answer
                            .extract()      //Actions to do, extract and later the action
                            .asString();
                    System.out.println(response);   //Show the response, like the page.

    }

    @Test
    public void loginTestWithAssertions(){

                 given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")                                        // Parameter request
                .post("/login")   // path URL
                .then()
                .log().all() // We can see all the parameters but here is inside the answer
                .statusCode(HttpStatus.SC_OK)
                 .body("token",notNullValue());


    }


    // GET METHOD
    @Test
    public void getSingleUserTest(){

                 given()
                .log().all() // We can see all the parameters
                .get("/user/2")   // response
                .then()// validate response
                .log().all() // We can see all the parameters but here is inside the answer
                .statusCode(HttpStatus.SC_OK)
                .body("data.id",equalTo(2)); //Assertion with a intern data like "id" equal to "some value"

    }

    @Test
    public void getAllUsersTest(){

        Response response =  given()
                .get("users?page=2");  // response
        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String contentType = response.getContentType();

        assertThat(statusCode,equalTo(HttpStatus.SC_OK));
        System.out.println("Body" + body);
        System.out.println("Content Type" + contentType);
        System.out.println("Header" + headers.toString());

        System.out.println("**************");
        System.out.println("**************");

        System.out.println(headers.get("Content-Type"));
        headers.get("Transfer-Encoding");



    }


    //DELETE METHOD
    @Test
    public void deleteUserTest(){

        given()
                .delete("/user/2")   // response
                .then()// validate response
                .statusCode(HttpStatus.SC_NO_CONTENT);

    }


    //PATCH METHOD
    @Test
    public void patchUserTest(){

        String nameUpdated =
        given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .patch("/user/2")   // response
                .then()// validate response
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("name");

        assertThat(nameUpdated, equalTo("morpheus"));

    }

  //  PUT METHOD
    @Test
    public void putUserTest(){
        String jobUpdated =
                given()
                        .when()
                        .body("{\n" +
                                "    \"name\": \"morpheus\",\n" +
                                "    \"job\": \"zion resident\"\n" +
                                "}")
                        .put("/user/2")   // response
                        .then()// validate response
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .jsonPath().getString("job");

        assertThat(jobUpdated, equalTo("zion resident"));


    }




}
