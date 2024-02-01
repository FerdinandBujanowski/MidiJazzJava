package de.bujanowski.midijazz.theory.music;

import de.bujanowski.midijazz.gui.elements.PianoView;
import de.bujanowski.midijazz.theory.music.elements.Note;
import de.bujanowski.midijazz.theory.reader.Pipeline;
import javafx.scene.paint.Color;
import de.bujanowski.midijazz.midi.MidiMessageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JazzPlayer {
    private static JazzPlayer INSTANCE;

    /**
     * For each channel (key to the map), keeps track of which notes are turned on / off
     */
    private Map<Integer, List<Integer>> notesOn;
    private Note currentRoot;

    private Pipeline currentPipeline;
    private boolean taskFinish;

    private final MidiMessageListener noteStateUpdater = new MidiMessageListener() {
        @Override public void noteOn(int channel, int key, int velocity) {
            Integer channelInteger = channel;
            Integer keyInteger = key;

            if(!notesOn.containsKey(channelInteger)) notesOn.put(channelInteger, new ArrayList<>());
            List<Integer> notesOnChannel = notesOn.get(channelInteger);
            if(!notesOnChannel.contains(keyInteger)) notesOnChannel.add(keyInteger);
        }
        @Override public void noteOff(int channel, int key, int velocity) {
            Integer channelInteger = channel;
            Integer keyInteger = key;

            if(!notesOn.containsKey(channelInteger)) return;
            List<Integer> notesOnChannel = notesOn.get(channelInteger);
            notesOnChannel.remove(keyInteger);
        }
    };

    private JazzPlayer() {
        this.notesOn = new HashMap<>();
        this.currentPipeline = Pipeline.buildPipeline(Pipeline.basePath + "voicings/01_block_chords.json");
        this.taskFinish = false;
    }

    public static JazzPlayer getInstance() {
        if(INSTANCE == null) INSTANCE = new JazzPlayer();
        return INSTANCE;
    }

    public boolean currentlyPlaying(int channel, int key) {
        List<Integer> ch = this.notesOn.get(channel);
        if(ch == null) return false;
        return ch.contains(key);
    }

    public Color resolveColor(int channel, int key) {
        if(this.currentlyPlaying(channel, key)) return PianoView.CURRENTLY_PLAYING;
        return MusicTheory.isBlackKey(key) ? PianoView.BLACK_KEY : PianoView.WHITE_KEY;
    }

    public MidiMessageListener getNoteStateUpdater() {
        return this.noteStateUpdater;
    }
}
