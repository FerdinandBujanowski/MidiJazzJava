package de.bujanowski.midijazz.theory.music.elements.chords;

import de.bujanowski.midijazz.theory.music.elements.Note;

public class Voicing implements IVoicing {

    private ChordFamily family;
    private String name;
    private String[] symbols;

    public Voicing(ChordFamily chordFamily, String name, String[] symbols) {
        this.family = chordFamily;
        this.name = name;
        this.symbols = symbols;
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
        return this.family;
    }
}
