package de.bujanowski.midijazz.theory.music;

import de.bujanowski.midijazz.theory.JsonReader;
import de.bujanowski.midijazz.theory.music.elements.Scale;

import java.util.Collections;
import java.util.List;

public class MusicTheory {

    /**
     * NEVER CHANGE THESE
     */
    public static final int midC = 60;
    public static final int stepsInOctave = 12;
    public static final int[] blackValues = new int[] {1, 3, 6, 8, 10};

    public static final String[][] notes = {
            {"C", "B#", "Dbb"}, {"C#", "Db"}, {"D", "C##", "Ebb"}, {"D#", "Eb"},
            {"E", "D##", "Fb"}, {"F", "E#", "Gbb"}, {"F#", "Gb"}, {"G", "F##", "Abb"},
            {"G#", "Ab"}, {"A", "G##", "Bbb"}, {"A#", "Bb"}, {"B", "A##", "Cb"}
    };
    public static final String sharp = "#";
    public static final String flat = "b";

    public static final char halfStep  = 'H';
    public static final char wholeStep = 'W';
    public static final char minorStep = '-';

    private static MusicTheory INSTANCE;
    private List<Scale> scales;

    private MusicTheory() {
        this.scales = JsonReader.readScales();
        Collections.sort(this.scales);
        for(Scale s : this.scales) {
            System.out.println(s);
        }
    }

    public static MusicTheory getInstance() {
        if(INSTANCE == null) INSTANCE = new MusicTheory();
        return INSTANCE;
    }

    public static boolean isBlackKey(int midiKey) {
        int key = midiKey % stepsInOctave;
        for(int blackValue : blackValues) {
            if(key == blackValue) return true;
        }
        return false;
    }
}
