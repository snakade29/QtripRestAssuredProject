 @Test(description = "Ensure Search Result making sure all the  cities are Displaying")
    public void  validatingGetCall() throws IOException {
        File schemaFile = new File("/Users/testvagrant/IdeaProjects/RestAssuredLearning/src/main/java/Schema/ExpectedSearchCityResultSchema.txt");
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

        // Assert that the response contains specific city IDs
        assertThat(actualIds, hasItems("bengaluru", "goa", "kolkata", "singapore", "malaysia", "bangkok", "new-york", "paris"));
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
String expectedJsonContent = new String(Files.readAllBytes(Paths.get("/Users/testvagrant/IdeaProjects/RestAssuredLearning/Resources/TestData/Search_City_ResponsePayload.json")));


        JsonArray expectedJsonArray = JsonParser.parseString(expectedJsonContent).getAsJsonArray();
            // Compare the response JSON with the expected JSON
            Assert.assertEquals(  expectedJsonArray ,responseJsonArray );

    }









    }