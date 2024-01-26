package de.bujanowski.midijazz.theory.music.elements.chords;

import de.bujanowski.midijazz.theory.music.elements.Note;

public interface IVoicing {

    String getName();
    Note[] getNotes(int inversion, boolean root, boolean five);
}
