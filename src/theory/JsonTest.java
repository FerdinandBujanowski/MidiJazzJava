package theory;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonTest {
    public static void main(String[] args) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        JsonArray scalesArray = parser.parse(new FileReader("src/resources/scales.json")).getAsJsonArray();

        for(JsonElement element : scalesArray) {
            JsonObject scale = element.getAsJsonObject();
            JsonElement nameObject = scale.get("name");
            if(nameObject.isJsonArray()) {
                System.out.println(nameObject.getAsJsonArray());
            } else System.out.println(nameObject);
    }


    }
}
