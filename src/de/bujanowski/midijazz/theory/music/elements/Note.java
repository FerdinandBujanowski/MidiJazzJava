package de.bujanowski.midijazz.theory.music.elements;

public class Note {

    private int midi;
    private String name;
    private boolean relativeMidi;

    public Note(int midi, String name, boolean relativeMidi) {
        this.midi = midi;
        this.name = name;
        this.relativeMidi = relativeMidi;
    }


}
