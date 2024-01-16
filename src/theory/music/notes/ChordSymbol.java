package theory.music.notes;

public class ChordSymbol {

    public static final String root = "X";
    public static final String other = "Y";

    private String symbol;

    public ChordSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRealSymbol(Note rootNote, Note otherNote) {
        //TODO replace X and Y by root and other note
        return "";
    }
}
