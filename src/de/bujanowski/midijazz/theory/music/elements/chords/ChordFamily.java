package de.bujanowski.midijazz.theory.music.elements.chords;

import de.bujanowski.midijazz.theory.music.elements.Note;
import de.bujanowski.midijazz.theory.music.elements.Steppable;

import java.util.HashMap;
import java.util.Map;

public class ChordFamily implements Steppable, IVoicing {

    private final String name;
    private final int[] chordNotes;
    private final int[] availableTension;
    private final String[] noteSymbols;

    private final Map<String, Integer> symbolMap;
    private final Map<Integer, String> midiMap;

    private final Map<String, Voicing> childVoicings;

    private ChordFamily(String name, int[] chordNotes, int[] availableTension, String[] noteSymbols, Map<String, String[]> voicingNotes) {
        this.name = name;
        this.chordNotes = chordNotes;
        this.availableTension = availableTension;
        this.noteSymbols = noteSymbols;

        this.symbolMap = new HashMap<>();
        this.midiMap = new HashMap<>();

        int cCounter = 0, tCounter = 0;
        int currentStep = 0;
        for(String noteSymbol : this.noteSymbols) {
            int currentChordNote = cCounter < this.chordNotes.length ? this.chordNotes[cCounter] : this.availableTension[tCounter];
            int currentTension = tCounter < this.availableTension.length ? this.availableTension[tCounter] : currentChordNote;

            if(currentChordNote < currentTension) {
                currentStep = currentChordNote;
                cCounter++;
            } else {
                currentStep = currentTension;
                tCounter++;
            }
            this.symbolMap.put(noteSymbol, currentStep);
            this.midiMap.put(currentStep, noteSymbol);
        }

        this.childVoicings = new HashMap<>();
        for(String voicingName : voicingNotes.keySet()) {
            this.childVoicings.put(voicingName, new Voicing(this, voicingName, voicingNotes.get(voicingName)));
        }
    }

    public static ChordFamily buildChord(String name, int[] chordNotes, int[] availableTension, String[] noteSymbols, Map<String, String[]> voicingNotes) {
        return new ChordFamily(name, chordNotes, availableTension, noteSymbols, voicingNotes);
    }

    /**
     * Returns the name of a chord note in relation to it's root note
     * @param relativeStep the number of half steps away from the root
     * @return the relative name of the step
     */
    public String getStepName(int relativeStep) {
        if(!this.midiMap.containsKey(relativeStep)) return "";
        return this.midiMap.get(relativeStep);
    }

    /**
     * Returns the number of half steps that a note is away from the chord root
     * @param relativeName the relative name of the note
     * @return the "relative step"
     */
    public int getNoteStep(String relativeName) {
        if(!this.symbolMap.containsKey(relativeName)) return 0;
        return this.symbolMap.get(relativeName);
    }

    public Voicing getVoicing(String name) {
        return this.childVoicings.get(name);
    }

    @Override
    public boolean isSubscale(Steppable other) {
        for(int chordNote : this.chordNotes) {
            if(other.hasStep(chordNote)) return false;
        }
        return true;
    }

    @Override
    public boolean hasStep(int otherStep) {
        //TODO include tension notes ?
        for(int chordNote : this.chordNotes) {
            if(otherStep == chordNote) return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Note[] getNotes(int midiRoot, int inversion, boolean withRoot, boolean withFive) {

        return new Note[0];
    }

    @Override
    public ChordFamily getFamily() {
        return this;
    }
}
