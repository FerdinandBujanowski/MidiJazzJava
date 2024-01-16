package theory.music;

import theory.music.notes.Note;

import java.util.List;

public abstract class MusicTheory {

    /**
     * NEVER CHANGE THESE TWO
     */
    public static final int midC = 60;
    public static final int stepsInOctave = 12;
    public static final int[] blackValues = new int[] {1, 3, 6, 8, 10};

    public static final List<Note> sharpNotes = Note.getNotesWithSharps();


    public static boolean isBlackKey(int midiKey) {
        int key = midiKey % stepsInOctave;
        for(int blackValue : blackValues) {
            if(key == blackValue) return true;
        }
        return false;
    }
}
