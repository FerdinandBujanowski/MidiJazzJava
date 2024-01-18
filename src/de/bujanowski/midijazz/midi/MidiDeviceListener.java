package de.bujanowski.midijazz.midi;

public interface MidiDeviceListener {
    void changed(String oldSelected, String newSelected);
}
