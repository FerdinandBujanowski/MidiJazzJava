package theory.music;

import midi.MidiMessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RealTimeAnalysis {
    private static RealTimeAnalysis INSTANCE;

    /**
     * For each channel (key to the map), keeps track of which notes are turned on / off
     */
    private Map<Integer, List<Integer>> notesOn;

    private MidiMessageListener sharpLogger = new MidiMessageListener() {
        @Override
        public void noteOn(int channel, int key, int velocity) {
            int index = key % 12;
            System.out.println(MusicTheory.sharpNotes.get(index));
        }

        @Override
        public void noteOff(int channel, int key, int velocity) {}
    };

    private final MidiMessageListener noteStateUpdater = new MidiMessageListener() {
        @Override
        public void noteOn(int channel, int key, int velocity) {
            if(!notesOn.containsKey(channel)) notesOn.put(channel, new ArrayList<>());
            List<Integer> notesOnChannel = notesOn.get(channel);
            if(!notesOnChannel.contains(key)) notesOnChannel.add(key);
        }

        @Override
        public void noteOff(int channel, int key, int velocity) {
            if(!notesOn.containsKey(channel)) return;
            List<Integer> notesOnChannel = notesOn.get(channel);
            if(notesOnChannel.contains(key)) notesOnChannel.remove(key);
        }
    };

    private RealTimeAnalysis() {
        //...
    }

    public static RealTimeAnalysis getInstance() {
        if(INSTANCE == null) INSTANCE = new RealTimeAnalysis();
        return INSTANCE;
    }

    public MidiMessageListener getSharpLogger() {
        return this.sharpLogger;
    }
    public MidiMessageListener getNoteStateUpdater() {
        return this.noteStateUpdater;
    }
}
