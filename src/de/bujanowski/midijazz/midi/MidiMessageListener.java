package de.bujanowski.midijazz.midi;

public interface MidiMessageListener {
    void noteOn(int channel, int key, int velocity);
    void noteOff(int channel, int key, int velocity);
}
