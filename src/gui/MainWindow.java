package gui;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import midi.MidiHandler;

import javax.sound.midi.MidiUnavailableException;

public class MainWindow {

    public static final Color PURPLE_DARK = new Color(40/255., 40/255., 56/255., 1.0);
    public static final Color PURPLE_MID_DARK = new Color(64/255., 56/255., 76/255., 1.0);
    public static final Color PURPLE_MID = new Color(128/255., 98/255., 136/255., 1.0);

    private Stage stage;
    private Canvas canvas;
    private VBox vBox;

    private Menu inputMenu, outputMenu, mixerMenu;

    public MainWindow(Stage stage) {
        this.stage = stage;
        this.canvas = new Canvas();
        this.stage.setTitle("Jazz Trainer");
        this.canvas = new Canvas(500, 500);

        this.stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            this.canvas.setWidth((double)newVal);
            this.draw();
        });
        this.stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            this.canvas.setHeight((double)newVal);
            this.draw();
        });
        this.vBox = new VBox();

        this.inputMenu = new Menu("MIDI Input");
        this.outputMenu = new Menu("Midi Output");
        this.mixerMenu = new Menu("Audio Devices");

        MenuBar menuBar = new MenuBar(this.inputMenu, this.outputMenu, this.mixerMenu);
        menuBar.setBackground(Background.fill(PURPLE_MID));
        vBox.getChildren().add(menuBar);

        vBox.getChildren().add(this.canvas);
        this.stage.setScene(new Scene(this.vBox));

        stage.show();

        try {
            MidiHandler.logMidiDevices();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void draw() {
        this.canvas.getGraphicsContext2D().setFill(PURPLE_DARK);
        this.canvas.getGraphicsContext2D().fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    private void updateMIDI() {

    }

    private void updateMixers() {
        
    }
}
