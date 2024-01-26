package de.bujanowski.midijazz.gui.elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import de.bujanowski.midijazz.midi.MidiDeviceManager;
import de.bujanowski.midijazz.theory.music.*;

import java.awt.*;

public class PianoView extends AbstractElement {

    public static final Color WHITE_KEY = Color.WHITE;
    public static final Color BLACK_KEY = Color.DARKGRAY.darker();
    public static final Color CURRENTLY_PLAYING = Color.BLUE;

    /**
     * Example: lowestC = -1 --> one octave under medium C (MIDI : 48)
     */
    private int lowestC, highestC;
    private int lowMidiC, highMidiC;
    private int octaves;

    private int totalKeys, whiteKeys, blackKeys;

    public PianoView(double[] bounds, int lowestC, int highestC) {
        super(bounds);
        assert lowestC < highestC;

        int midC = MusicTheory.midC;
        int steps = MusicTheory.stepsInOctave;

        this.lowestC = lowestC;
        this.lowMidiC = midC + (lowestC * steps);

        this.highestC = highestC;
        this.highMidiC = midC + (highestC * steps);

        this.octaves = (this.highMidiC - this.lowMidiC) / steps;
        this.totalKeys = (this.octaves * steps) + 1;
        this.blackKeys = this.octaves * 5;
        this.whiteKeys = this.totalKeys - this.blackKeys;

        //System.out.println("total : " + this.totalKeys + ", black : " + this.blackKeys + ", white : " + this.whiteKeys);
    }

    @Override
    public void draw(GraphicsContext context) {

        //TODO create function to recalculate those values, called when window resized in MainWindow.java
        double width = context.getCanvas().getWidth();
        double height = context.getCanvas().getHeight();
        int whiteKeyHeight = (int)Math.round(1/3. * height);

        int whiteKeyWidth = (int)Math.round(width / this.whiteKeys);
        int blackKeyWidth = (int)Math.round(2/3. * whiteKeyWidth);
        int halfBlackKeyWidth = (int)Math.round(0.5 * blackKeyWidth);
        int blackKeyHeight = (int)Math.round(0.5 * whiteKeyHeight);

        // white keys
        int whiteKeysPassed = (int)Math.round(this.getBounds()[0] * width);
        for(int i = 0; i < this.totalKeys; i++) {
            if(!MusicTheory.isBlackKey(i)) {
                context.setFill(JazzPlayer.getInstance().resolveColor(MidiDeviceManager.defaultChannel, this.lowMidiC + i));
                context.fillRect(whiteKeysPassed * whiteKeyWidth, height - whiteKeyHeight, whiteKeyWidth, whiteKeyHeight);
                context.setStroke(Color.BLACK);
                context.strokeRect(whiteKeysPassed * whiteKeyWidth, height - whiteKeyHeight, whiteKeyWidth, whiteKeyHeight);

                whiteKeysPassed++;
            }
        }

        // black keys
        whiteKeysPassed = 0;
        for(int i = 0; i < this.totalKeys; i++) {
            if(MusicTheory.isBlackKey(i)) {
                context.setFill(JazzPlayer.getInstance().resolveColor(MidiDeviceManager.defaultChannel, this.lowMidiC + i));
                context.fillRect(whiteKeysPassed * whiteKeyWidth - halfBlackKeyWidth, height - whiteKeyHeight, blackKeyWidth, blackKeyHeight);
                context.setStroke(Color.BLACK);
                context.strokeRect(whiteKeysPassed * whiteKeyWidth - halfBlackKeyWidth, height - whiteKeyHeight, blackKeyWidth, blackKeyHeight);
            } else whiteKeysPassed++;
        }

    }
}
