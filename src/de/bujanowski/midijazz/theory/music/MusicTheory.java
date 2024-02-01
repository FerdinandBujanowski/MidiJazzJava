package de.bujanowski.midijazz.theory.music;

import de.bujanowski.midijazz.theory.reader.ElementReader;
import de.bujanowski.midijazz.theory.music.elements.*;
import de.bujanowski.midijazz.theory.music.elements.chords.ChordFamily;
import de.bujanowski.midijazz.theory.music.structures.ScaleHierarchy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String[] circleBases = {"C", "Db", "D", "Eb", "E", "F", "(F#/Gb)", "G", "Ab", "A", "Bb", "B"};

    public static final String sharp = "#";
    public static final String flat = "b";

    public static final char halfStep  = 'H';
    public static final char wholeStep = 'W';
    public static final char minorStep = '-';

    private static MusicTheory INSTANCE;
    private List<Scale> scales;
    private Map<String, ChordFamily> chordFamilies;
    private ScaleHierarchy scaleHierarchy;

    private MusicTheory() {
        this.scales = ElementReader.readScales();
        Collections.sort(this.scales);

        this.scaleHierarchy = new ScaleHierarchy();
        for(Scale scale : this.scales) {
            this.scaleHierarchy.addScale(scale);
        }
        this.scaleHierarchy.log();

        this.chordFamilies = new HashMap<>();
        for(ChordFamily chordFamily : ElementReader.readChords()) {
            this.chordFamilies.put(chordFamily.getName(), chordFamily);
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

    public static String getRootName(int midi) {
        return circleBases[midi % stepsInOctave];
    }

    public static int translateCircleToChromatic(int circleProgress) {
        assert(circleProgress <= stepsInOctave && circleProgress >= -stepsInOctave);
        if(circleProgress < 0) circleProgress = stepsInOctave + circleProgress;
        //+1 step in circle represents +7 half steps chromatically
        // -> -1 represents -7 chromatic steps <=> +5 chromatic steps

        return (circleProgress * 7) % stepsInOctave;
    }

    public ChordFamily getChordFamily(String name) {
        return this.chordFamilies.get(name);
    }
}
