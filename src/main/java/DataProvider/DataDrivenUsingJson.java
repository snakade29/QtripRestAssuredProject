package DataProvider;


import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.DataProvider;
import com.jayway.jsonpath.JsonPath;
import java.io.File;
import java.io.IOException;
import java.util.List;


import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataDrivenUsingJson{


    public class DataDrivenTestingUsingFile {

        @DataProvider(name = "getTestData")
        public Object[] getTestDataUsingJson() {
            Object[] obj = null;
            try {
                // Load JSON file
                File filename = new File("Resources//TestData//RegisterTestData.json");

                // Convert JSON file into a String
                String json = FileUtils.readFileToString(filename, "UTF-8");

                // Parse the JSON string into a List using JsonPath
                List<Object> jsonArray = JsonPath.read(json, "$");

                // Convert List to Object array
                obj = jsonArray.toArray(new Object[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return obj;
        }
    }


}