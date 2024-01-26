package de.bujanowski.midijazz.theory.music.elements;

public interface Steppable {

    boolean isSubscale(Steppable other);
    boolean hasStep(int otherStep);
}
