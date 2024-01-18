package de.bujanowski.midijazz.theory;

import com.google.gson.*;
import de.bujanowski.midijazz.theory.music.elements.Scale;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

    public static final String NAME = "name";
    public static final String SCALE = "scale";
    public static final String OFFSET = "offset";
    public static final String REMARK = "remark";

    public static List<Scale> readScales() {
        List<Scale> scales = new ArrayList<>();

        // create parser
        JsonParser parser = new JsonParser();
        JsonArray scalesArray = null;
        try {
            // open file
            scalesArray = parser.parse(new FileReader("src/de/bujanowski/midijazz/resources/scale_syllabus.json")).getAsJsonArray();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(scalesArray == null) return scales;

        // iterate through scales
        for(JsonElement element : scalesArray) {
            JsonObject scale = element.getAsJsonObject();

            // name(s)
            JsonElement nameObject = scale.get(NAME);
            List<String> names = new ArrayList<>();
            if(nameObject.isJsonArray()) {
                for(JsonElement nameElement : nameObject.getAsJsonArray()) {
                    names.add(nameElement.getAsString());
                }
            } else names.add(nameObject.getAsString());

            // scale
            String scaleString = scale.get(SCALE).getAsString();

            // offset
            int offset = 0;
            if(scale.has(OFFSET)) offset = scale.get(OFFSET).getAsInt();

            // remark
            String remark = null;
            if(scale.has(REMARK)) remark = scale.get(REMARK).getAsString();

            // ...

            scales.add(Scale.buildScale(names, scaleString, offset, remark));
        }

        return scales;
    }
}
