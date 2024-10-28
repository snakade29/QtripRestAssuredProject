package DataProvider;



import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataDrivenTestingUsingFile {

    // DataProvider to supply JSON data to the test
    @DataProvider(name = "getTestData")
    public Object[] getTestDataUsingJson() {
        Object[] obj = null;
        try {
            // Specify the file path of your JSON test data
            File filename = new File("Resources/TestData/RegisterTestData.json");
            // Read the JSON file as a string
            String json = FileUtils.readFileToString(filename, "UTF-8");
            List<Map<String, Object>> jsonData = JsonPath.read(json, "$");
            // Convert the list to an array of objects for the DataProvider
            obj = jsonData.toArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }


    @DataProvider(name = "TestData")
    public Object[] getTestData() {
        Object[] obj = null;
        try {
            // Specify the file path of your JSON test data
            File filename = new File("Resources/TestData/AdventureBookingTestData.json");
            // Read the JSON file as a string
            String json = FileUtils.readFileToString(filename, "UTF-8");
            List<Map<String, Object>> jsonData = JsonPath.read(json, "$");
            // Convert the list to an array of objects for the DataProvider
            obj = jsonData.toArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}

