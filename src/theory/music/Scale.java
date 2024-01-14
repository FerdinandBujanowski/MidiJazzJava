package theory.music;

public class Scale {

    /**
     * The reference scale is a scale in reference to which this scale's steps are defined.
     * For example, the Dorian scale is a natural minor scale with an augmented sixth (#6).
     * A major scale would be its own reference scale (the steps being 1, 2, 3, 4, 5, 6, 7 and 8 without # or b).
     */
    private Scale referenceScale;

    /**
     * The array of notes which compose the scale.
     */
    private final Note[] notes;

    private Scale(Note[] notes) {
        this.notes = notes;
    }

    /**
     * Function that allows the dynamic creation of scale objects, without invoking the constructor from outside.
     * @return the newly built Scale object.
     */
    public static Scale buildScale() {
        //...
        return new Scale(new Note[0]);
    }
}
