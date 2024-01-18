package de.bujanowski.midijazz.theory.music.elements;

import de.bujanowski.midijazz.theory.music.MusicTheory;

import java.util.Arrays;
import java.util.List;

public class Scale implements Comparable<Scale> {

    //TODO maybe delete this?
    /**
     * The reference scale is a scale in reference to which this scale's steps are defined.
     * For example, the Dorian scale is a natural minor scale with an augmented sixth (#6).
     * A major scale would be its own reference scale (the steps being 1, 2, 3, 4, 5, 6, 7 and 8 without # or b).
     */
    private Scale referenceScale;

    private final List<String> names;
    private final int[] steps;
    private String remark;

    private Scale(List<String> names, String steps, int offset, String remark) {
        this.names = names;
        this.remark = remark;

        int last = offset;
        this.steps = new int[steps.length()];
        for(int i = 0; i < this.steps.length; i++) {
            this.steps[i] = (last + MusicTheory.stepsInOctave) % MusicTheory.stepsInOctave;
            switch(steps.charAt(i)) {
                case(MusicTheory.halfStep) ->  last += 1;
                case(MusicTheory.wholeStep) -> last += 2;
                case(MusicTheory.minorStep) -> last += 3;
                default -> last = this.steps.length;
            }
        }
    }

    /**
     * Function that allows the dynamic creation of scale objects, without invoking the constructor from outside.
     * @return the newly built Scale object.
     */
    public static Scale buildScale(List<String> names, String steps, int offset, String remark) {
        return new Scale(names, steps, offset, remark);
    }

    @Override
    public int compareTo(Scale o) {
        return o.steps.length - this.steps.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.names.size(); i++) {
            sb.append(this.names.get(i));
            if(i != this.names.size() - 1) sb.append(", ");
        }
        if(this.remark != null) {
            sb.append(" ");
            sb.append(this.remark);
        }

        sb.append(" -> ");
        sb.append(Arrays.toString(this.steps));
        return sb.toString();
    }
}
