package UserManagement;
import DataProvider.DataDrivenTestingUsingFile;
import POJO.RegisterLoginPostCall;

import POJO.TripReservationPostCall;
import Utils.PropertyReader;
import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import core.StatusCode;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static core.ApiEndpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

public class TestCases
{
    List<String> cities = Arrays.asList("bengaluru", "goa", "kolkata", "singapore", "malaysia", "bangkok", "new-york", "paris");

    List<String> expectedIds = Arrays.asList("2447910730", "1773524915", "0355034513", "2260150453", "1921387712", "7938812489", "2757195090", "3727396712", "2211420097");
    String BaseURI = PropertyReader.propertyReader( System.getProperty("user.dir")+"/config.properties","BaseURI");
    String  randomemail ;
    RegisterLoginPostCall register ;
    String email ;
    String token;
    String id ;
    String AdventureID ;
    String reservationID;
    String password ;




    @Test( priority =1 ,description =" Verify User is  able Register with valid Credential",
            dataProvider = "getTestData",dataProviderClass = DataDrivenTestingUsingFile.class)
    public void RegisterUser (Map<String, Object> data)
    {

        email = (String)data.get("username");
        password = (String)data.get("password");
        String uuid = UUID.randomUUID().toString();
        randomemail = "user_" + uuid + "@example.com";


                register = new RegisterLoginPostCall(  randomemail,password ,password);

        // Given: Precondition (API endpoint and request body)
        given()
                .header("Content-Type", "application/json")
                .body(register).log().all()

                // When: Action (the request is made)
                .when()
                .post(BaseURI+ Register_User)

                // Then: Expected result (response validation)
                .then().assertThat()
                .statusCode(StatusCode.CREATED.code).body("success",org.hamcrest.Matchers.is(true));
    }

    @Test( priority =2 ,description ="Verify User is not able to Reregister with same Email ",dependsOnMethods = { "RegisterUser" })
    public void ReregisterUser ()
    {

         register = new RegisterLoginPostCall(email,password ,password);

        // Given: Precondition (API endpoint and request body)
        given()
                .header("Content-Type", "application/json")
                .body(register).log().all()

                // When: Action (the request is made)
                .when()
                .post(BaseURI+Register_User)

                // Then: Expected result (response validation)
                .then().assertThat()
                .statusCode(StatusCode.BAD_REQUEST.code).body("success",org.hamcrest.Matchers.is(false)).body("message", org.hamcrest.Matchers.equalTo("Email already exists") );
    }


    @Test(priority =3 ,description = "Verify User is able to Login",
            dependsOnMethods = { "RegisterUser" })
    public void RegisterloginTest()
    {

     RegisterLoginPostCall login  = new RegisterLoginPostCall(register.getEmail(), register.getPassword());

        // Given: Precondition (API endpoint and request body)
           Response response = given()
                .header("Content-Type", "application/json")
                .body(login).log().all()

                // When: Action (the request is made)
                .when()
                .post(BaseURI+ Login)
                   .then().statusCode(StatusCode.CREATED.code)
                   .body("success",org.hamcrest.Matchers.is(true))
                   .extract().response().prettyPeek();


        token = response.path("data.token");
        id = response.path("data.id");

    }

@Test  (priority =4 ,description ="Verify user is able to search for adventures")
public void   GetAdventureDetails() throws IOException {

        File schemaFile = new File(System.getProperty("user.dir")+"/src/main/java/Schema/ExpectedAdventureResultSchema.txt");



    Response response  = given()
            .header("Cookie", "ARRAffinity=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7; ARRAffinitySameSite=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7")

            // Then: Expected result (response validation)
            .when()
            .get(BaseURI+Get_AdventureDetails)
            .then()
            .body(JsonSchemaValidator.matchesJsonSchema(schemaFile))
            .extract().response().prettyPeek();
    List<String> actualIds = response.jsonPath().getList("id");
    assertThat(actualIds, hasItems(expectedIds.toArray(new String[0])));
    assertEquals(response.jsonPath().getList("").size(), 9);
  assertEquals(actualIds.size(), 9); // Validate that the response contains some cities
// assertEquals(response.jsonPath().getString("[0].id"), notNullValue());
    response.then().body("[0].id",equalTo("2447910730"));
   response.then().body("[0].name",equalTo("Niaboytown"));
   response.then().body("[0].costPerHead",equalTo(4003));

    response.then().body("[0].currency",equalTo( "INR"));
    JsonArray responseJsonArray = JsonParser.parseString(response.asString()).getAsJsonArray();
    // Load the expected JSON from a file and parse it to JsonArray
//         Load the expected JSON from a file and parse it to JsonObject
    String expectedJsonContent = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/Resources/TestData/Search_Adventure_ResponsePayload.json")));


    JsonArray expectedJsonArray = JsonParser.parseString(expectedJsonContent).getAsJsonArray();
    // Compare the response JSON with the expected JSON
    Assert.assertEquals(  expectedJsonArray ,responseJsonArray );



    AdventureID  = response.path("[0].id");

}



    @Test(priority =5 ,description = "Verify user is able to book the Trip ",dependsOnMethods = { "RegisterloginTest" , "RegisterUser","GetAdventureDetails" }
           , dataProvider = "TestData",dataProviderClass = DataDrivenTestingUsingFile.class)
    public void TripReservation(Map<String, Object> data)
    {
        Faker faker = new Faker();
        // Create and populate the UserRegistration POJO with random data
        TripReservationPostCall trip = new TripReservationPostCall();
        trip.setUserId(id);
        trip.setName( (String)data.get("name"));
        trip.setDate((String)data.get("date"));
        trip.setPerson((String)data.get("person"));
        trip.setAdventure(AdventureID);

        // Given: Precondition (API endpoint and request body)
        Response response = given().header("Authorization", "Bearer " + token )
                .header("Content-Type", "application/json")
                .body(trip).log().all()

                // When: Action (the request is made)
                .when()
                .post(BaseURI+Reservation)
                .then()
                .statusCode(StatusCode.SUCCESS.code)
               // .body("success",org.hamcrest.Matchers.is(true))
                .extract().response().prettyPeek();

    }


    @Test(priority =6 ,description = "Validating Reservation Informatoion ",dependsOnMethods = { "RegisterloginTest" , "RegisterUser" ,"TripReservation","GetAdventureDetails"})
    public void GetReserverationInforrmation()
    {

        Response response  = given()
                .header("Authorization", "Bearer " + token )
                .header("Cookie", "ARRAffinity=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7; ARRAffinitySameSite=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7")
                .log().all()

                // Then: Expected result (response validation)
                .when()
                .get(BaseURI+CheckReservation_Get+id)
                .then()
                .statusCode(StatusCode.SUCCESS.code)
                .extract().response().prettyPeek();

         reservationID = response.path("[0].id");


    }


    @Test(priority =7 ,description = "Delete Reservation",dependsOnMethods = { "RegisterloginTest" , "RegisterUser" ,"TripReservation","GetAdventureDetails","GetReserverationInforrmation"})
    public void DeleteReservation()
    {

        Response response  = given()
                .header("Authorization", "Bearer " + token )
                .header("Cookie", "ARRAffinity=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7; ARRAffinitySameSite=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7")
                .body("{\n" +
                        "    \"userId\":\"" + id + "\" " +
                        "}").log().all()
                // Then: Expected result (response validation)
                .when()
                .delete(BaseURI+ Delete_Reservation +reservationID)

                .then()
                .extract().response().prettyPeek();




    }


    @Test(description = "Ensure Search Result making sure all the  cities are Displaying")
    public void  validatingGetCall() throws IOException {
        File schemaFile = new File(System.getProperty("user.dir")+"/src/main/java/Schema/ExpectedSearchCityResultSchema.txt");
        // JsonSchemaValidator jasonvalidator = JsonSchemaValidator.matchesJsonSchema(schemaFile);



        Response response = given()
                .header("Cookie", "ARRAffinity=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7; ARRAffinitySameSite=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7")
                .when()
                .get("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/cities")
                .then()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile))
                .extract().response().prettyPeek();

        // Then: Expected result (response validation)
        List<String> actualIds = response.jsonPath().getList("id");
        assertThat(actualIds, hasItems(cities.toArray(new String[0])));

        // Assert that the response contains specific city IDs

        assertThat(response.jsonPath().getList(""), hasSize(8));
        // Additional checks
        assertThat(actualIds.size(), greaterThan(0)); // Validate that the response contains some cities
        assertThat(response.jsonPath().getString("[0].city"), notNullValue());
        response.then().body("[0].id", equalTo("bengaluru"));
        response.then().body("[0].city", equalTo("Bengaluru"));
        response.then().body("[0].description", equalTo("100+ Places"));
        response.then().body("[0].image", equalTo("https://images.pexels.com/photos/3573382/pexels-photo-3573382.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));



        // Validating Response Header
        Headers headers = response.getHeaders();

        for (Header h : headers) {
            if (h.getValue().contains(
                    "Express")) {
                System.out.println(h.getName() + "     :" + h.getValue());
            }

            System.out.println(h.getName() + "     :" + h.getValue());
        }


        // Validating Whole Json
        JsonArray responseJsonArray = JsonParser.parseString(response.asString()).getAsJsonArray();

        // Load the expected JSON from a file and parse it to JsonArray
//
//         Load the expected JSON from a file and parse it to JsonObjec
        String expectedJsonContent = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/Resources/TestData/Search_City_ResponsePayload.json")));


        JsonArray expectedJsonArray = JsonParser.parseString(expectedJsonContent).getAsJsonArray();
        // Compare the response JSON with the expected JSON
        Assert.assertEquals(  expectedJsonArray ,responseJsonArray );

    }

















}
