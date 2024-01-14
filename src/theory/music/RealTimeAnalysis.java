package theory.music;

import midi.MidiMessageListener;

public class RealTimeAnalysis {
    private static RealTimeAnalysis INSTANCE;

    private MidiMessageListener sharpLogger = new MidiMessageListener() {
        @Override
        public void noteOn(int channel, int key, int velocity) {
            int index = (key % MusicTheory.midC) % 12;
            System.out.println(MusicTheory.sharpNotes.get(index));
        }

        @Override
        public void noteOff(int channel, int key, int velocity) {}
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
}
