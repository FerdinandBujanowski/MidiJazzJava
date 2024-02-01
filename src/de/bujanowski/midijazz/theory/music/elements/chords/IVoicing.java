package de.bujanowski.midijazz.theory.music.elements.chords;

import de.bujanowski.midijazz.theory.music.elements.Note;

public interface IVoicing {

    String getName();
    Note[] getNotes(int midiRoot, int inversion, boolean withRoot, boolean withFive);
    ChordFamily getFamily();

}
