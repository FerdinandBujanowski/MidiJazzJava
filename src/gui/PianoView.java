package gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import theory.music.MusicTheory;

public class PianoView {

    /**
     * Example: lowestC = -1 --> one octave under medium C (MIDI : 48)
     */
    private int lowestC, highestC;
    private int lowMidiC, highMidiC;
    private int octaves;

    private int totalKeys, whiteKeys, blackKeys;

    public PianoView(int lowestC, int highestC) {
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
        int whiteKeysPassed = 0;
        for(int i = 0; i < this.totalKeys; i++) {
            if(!MusicTheory.isBlackKey(i)) {
                context.setFill(Color.WHITE);
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
                context.setFill(Color.DARKGRAY.darker());
                context.fillRect(whiteKeysPassed * whiteKeyWidth - halfBlackKeyWidth, height - whiteKeyHeight, blackKeyWidth, blackKeyHeight);
                context.setStroke(Color.BLACK);
                context.strokeRect(whiteKeysPassed * whiteKeyWidth - halfBlackKeyWidth, height - whiteKeyHeight, blackKeyWidth, blackKeyHeight);
            } else whiteKeysPassed++;
        }

    }
}
