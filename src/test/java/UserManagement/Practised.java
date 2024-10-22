//package UserManagement;
//
//import static Utils.JsonReader.getJsonArray;
//import static io.restassured.RestAssured.*;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
//
//import DataProvider.DataDrivenTestingUsingFile;
//import POJO.RegisterLoginPostCall;
//import Utils.JsonReader;
//import Utils.PropertyReader;
//import com.google.gson.JsonArray;
//import core.StatusCode;
//import io.restassured.http.Header;
//import io.restassured.http.Headers;
//import io.restassured.response.Response;
//import org.json.simple.JSONArray;
//import org.json.simple.parser.ParseException;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//public class Practised {
//    String BaseURI = PropertyReader.propertyReader("/Users/testvagrant/IdeaProjects/RestAssuredLearning/config.properties","BaseURI");
//
//
//    @Test(dataProvider = "getTestData",dataProviderClass = DataDrivenTestingUsingFile.class)
//    public void testRegisterAPI(Map<String, Object> data) throws IOException, ParseException {
//
//
////        String uuid = UUID.randomUUID().toString();
////        String randomemail = "user_" + uuid + "@example.com";
//       // JSONArray jsonArray  = getJsonArray("User");
////        String username = JsonReader.getTestData("username");
////        String password = JsonReader.getTestData("password");
//        String email = (String)data.get("username");
//        String password = (String)data.get("password");
//
//
//
//        RegisterLoginPostCall  register = new RegisterLoginPostCall( email, password,password);
//
//        // Given: Precondition (API endpoint and request body)
//                given()
//                .header("Content-Type", "application/json")
//                .body( register ).log().all()
//                // When: Action (the request is made)
//                .when()
//                .post(BaseURI+"/api/v1/register")
//
//                // Then: Expected result (response validation)
//                .then().assertThat()
//                .statusCode(StatusCode.CREATED.code).body("success",org.hamcrest.Matchers.is(true)); // Assuming 201 is the success status code for registration
//
//
//    }
//
//
//
//
//    @Test
//    public void testCitiesAPI() {
//
//        // Given: Precondition (API endpoint and headers)
//       Response response  = given()
//                .header("Cookie", "ARRAffinity=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7; ARRAffinitySameSite=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7")
//
//                // Then: Expected result (response validation)
//                .when()
//                .get(BaseURI+"/api/v1/cities")
//                .then().extract().response().prettyPeek();
////                .statusCode(200) // Assuming a successful response returns 200
////                .body("size()", greaterThan(0)) // Validate that the response contains some cities
////                .body("[0].city", notNullValue()); // Validate that the first city's name is not null
//        List<String> actualIds = response.jsonPath().getList("id");
//        List<String> expectedIds = Arrays.asList("bengaluru", "goa", "kolkata", "singapore", "malaysia", "bangkok", "new-york","paris");
//
//
//        Assert.assertEquals(actualIds,expectedIds);
//
//
//    }
//
//
//
//
//
//
//        @Test
//        public void testCitiesAPI2() {
//            // Given: Precondition (API endpoint and headers)
//            Response response = given()
//                    .header("Cookie", "ARRAffinity=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7; ARRAffinitySameSite=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7")
//                    .when()
//                    .get("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/cities")
//                    .then().extract().response().prettyPeek();
//
//            // Then: Expected result (response validation)
//            List<String> actualIds = response.jsonPath().getList("id");
//
//            // Assert that the response contains specific city IDs
//            assertThat(actualIds, hasItems("bengaluru", "goa", "kolkata", "singapore", "malaysia", "bangkok", "new-york", "paris"));
//            assertThat(response.jsonPath().getList(""),hasSize(8));
//            // Additional checks
//            assertThat(actualIds.size(), greaterThan(0)); // Validate that the response contains some cities
//            assertThat(response.jsonPath().getString("[0].city"), notNullValue());
//              response.then().body("[0].id",equalTo("bengaluru"));
//            response.then().body("[0].city",equalTo("Bengaluru"));
//            response.then().body("[0].description",equalTo("100+ Places"));
//            response.then().body("[0].image",equalTo("https://images.pexels.com/photos/3573382/pexels-photo-3573382.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
//
//            Headers headers = response.getHeaders();
//
//            for(Header h : headers)
//            {
//                if(h.getValue().contains(
//                        "Express"))
//                {
//                    System.out.println(h.getName()+"     :" +h.getValue() );
//                }
//
//                System.out.println(h.getName()+"     :" +h.getValue() );
//            }
//
//
//        }
//
//
//
//        @Test
//        public void  test ()
//        {
//            Response response  = given()
//                    .header("Cookie", "ARRAffinity=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7; ARRAffinitySameSite=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7").queryParam("q","beng")
//
//                    // Then: Expected result (response validation)
//                    .when()
//                    .get("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/cities")
//                    .then().extract().response().prettyPeek();
//        }
//
//
//    @Test
//    public void  Logintest () throws IOException {
//        // Path to the JSON file
//        String filePath = "/Users/testvagrant/IdeaProjects/RestAssuredLearning/Resources/TestData/LoginTestdata.json";
//
//        // Read the JSON file as a String
//        String jsonBody = new String(Files.readAllBytes(Paths.get(filePath)));
//
//
//        Response response  = given()
//                .header("Cookie", "ARRAffinity=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7; ARRAffinitySameSite=6f722d9ba8c6f7ac8a05d991523ce95c10a2df43a9a9f2a9d215eed82ec16bb7")
//
//                // Then: Expected result (response validation)
//                .header("Content-Type", "application/json").when().body(jsonBody)
//                .post("https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/login")
//                .then().extract().response().prettyPeek();
//    }
//
//    }
//
//
//
