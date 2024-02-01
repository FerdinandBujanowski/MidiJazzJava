package de.bujanowski.midijazz.theory.reader;

import com.google.gson.*;
import de.bujanowski.midijazz.theory.music.elements.*;
import de.bujanowski.midijazz.theory.music.elements.chords.ChordFamily;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ElementReader {

    public static final String NAME = "name";
    public static final String SCALE = "scale";
    public static final String OFFSET = "offset";
    public static final String REMARK = "remark";

    public static final String C_NOTES = "chord_notes";
    public static final String A_TENSION = "available_tension";
    public static final String N_SYMBOLS = "note_symbols";

    public static final String CHILDREN = "children";
    public static final String NOTES = "notes";

    public static List<Scale> readScales() {
        List<Scale> scales = new ArrayList<>();

        // create parser
        JsonParser parser = new JsonParser();
        JsonArray scalesArray = null;
        try {
            // open file
            scalesArray = parser.parse(new FileReader("src/de/bujanowski/midijazz/resources/music_bases/scale_syllabus.json")).getAsJsonArray();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(scalesArray == null) return scales;

        // iterate through scales
        for(JsonElement element : scalesArray) {
            JsonObject scale = element.getAsJsonObject();

            // name(s)
            JsonElement nameObject = scale.get(NAME);
            String[] names;
            if(nameObject.isJsonArray()) {
                names = new String[nameObject.getAsJsonArray().size()];
                for(int i = 0; i < names.length; i++) {
                    names[i] = nameObject.getAsJsonArray().get(i).getAsString();
                }
            } else names = new String[] { nameObject.getAsString() };

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

    public static List<ChordFamily> readChords() {
        List<ChordFamily> chordFamilyList = new ArrayList<>();

        // create parser
        JsonParser parser = new JsonParser();
        JsonArray chordArray = null;
        try {
            // open file
            chordArray = parser.parse(new FileReader("src/de/bujanowski/midijazz/resources/music_bases/chords.json")).getAsJsonArray();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(chordArray == null) return chordFamilyList;

        for(JsonElement element : chordArray) {
            JsonObject chord = element.getAsJsonObject();

            // name
            String name = chord.get(NAME).getAsString();

            // chord notes
            JsonArray noteArray = chord.get(C_NOTES).getAsJsonArray();
            int[] chordNotes = new int[noteArray.size()];
            for(int i = 0; i < chordNotes.length; i++) {
                chordNotes[i] = noteArray.get(i).getAsInt();
            }

            // avaiable tension
            JsonArray tensionArray = chord.get(A_TENSION).getAsJsonArray();
            int[] availableTension = new int[tensionArray.size()];
            for(int i = 0; i < availableTension.length; i++) {
                availableTension[i] = tensionArray.get(i).getAsInt();
            }

            // note symbols
            JsonArray symbolArray = chord.get(N_SYMBOLS).getAsJsonArray();
            String[] noteSymbols = new String[symbolArray.size()];
            for(int i = 0; i < noteSymbols.length; i++) {
                noteSymbols[i] = symbolArray.get(i).getAsString();
            }

            // child voicings
            Map<String, String[]> voicings = new HashMap<>();

            if(chord.has(CHILDREN)) {
                JsonArray childrenArray = chord.getAsJsonArray(CHILDREN);

                for(JsonElement voicingElement : childrenArray) {
                    JsonObject voicingObject = voicingElement.getAsJsonObject();
                    String voicingName = voicingObject.get(NAME).getAsString();
                    JsonArray voicingNoteArray = voicingObject.getAsJsonArray(NOTES);
                    String[] voicingNotes = new String[voicingNoteArray.size()];
                    for(int i = 0; i < voicingNotes.length; i++) {
                        voicingNotes[i] = voicingNoteArray.get(i).getAsString();
                    }
                    voicings.put(voicingName, voicingNotes);
                }
            }

            chordFamilyList.add(ChordFamily.buildChord(name, chordNotes, availableTension, noteSymbols, voicings));
        }
        return chordFamilyList;
    }
}
