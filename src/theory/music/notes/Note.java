package theory.music.notes;

import java.util.ArrayList;
import java.util.List;

public class Note {

    public static final String[] notes = {"C", "_", "D", "_", "E", "F", "_", "G", "_", "A", "_", "B"};
    public static final String sharp = "#";
    public static final String flat = "b";

    private String symbol;
    private boolean bSharp, bFlat;

    private Note(String symbol, boolean bSharp, boolean bFlat) {
        this.symbol = symbol;
        this.bSharp = bSharp;
        this.bFlat = bFlat;
    }

    public static List<Note> getNotesWithSharps() {
        List<Note> output = new ArrayList<>();
        for(int i = 0; i < notes.length; i++) {
            String note = notes[i];
            if(note.equals("_")) {
                output.add(new Note(notes[i - 1], true, false));
            } else output.add(new Note(notes[i], false, false));
        }
        return output;
    }

    @Override
    public String toString() {
        return this.symbol + (this.bSharp ? sharp : (this.bFlat ? flat : ""));
    }
}
