package midi.device;

public interface MidiDeviceListener {
    void changed(String oldSelected, String newSelected);
}
