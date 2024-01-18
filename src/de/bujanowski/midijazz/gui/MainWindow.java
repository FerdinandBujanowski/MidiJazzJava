package de.bujanowski.midijazz.gui;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import de.bujanowski.midijazz.midi.MidiMessageListener;
import de.bujanowski.midijazz.midi.MidiDeviceManager;

import java.awt.*;

public class MainWindow {

    //--module-path "C:\Program Files\Java\javafx-sdk-21.0.1\lib" --add-modules javafx.base,javafx.graphics,javafx.controls

    public static final Color PURPLE_DARK = new Color(40/255., 40/255., 56/255., 1.0);
    public static final Color PURPLE_MID_DARK = new Color(64/255., 56/255., 76/255., 1.0);
    public static final Color PURPLE_MID = new Color(128/255., 98/255., 136/255., 1.0);

    private Stage stage;
    private Canvas canvas;
    private VBox vBox;

    private Menu inputMenu, refreshMenu;

    private PianoView pianoView;

    public MainWindow(Stage stage) {
        this.stage = stage;
        this.canvas = new Canvas();
        this.stage.setTitle("MIDI Jazz");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.canvas = new Canvas(2/3. * screenSize.width, 2/3. * screenSize.height);

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

        this.refreshMenu = new Menu("Refresh..");
        MenuItem refreshItem = new MenuItem("Refresh Devices");
        refreshItem.setOnAction(e -> {
            MidiDeviceManager.getInstance().refreshDevices();

            this.inputMenu.getItems().removeAll();
            for(String name : MidiDeviceManager.getInstance().getDeviceNames()) {
                MenuItem currentDeviceItem = new CheckMenuItem(name);
                currentDeviceItem.setOnAction(e1 -> {
                    // device selected
                    MidiDeviceManager.getInstance().selectPort(name);

                });
                this.inputMenu.getItems().add(currentDeviceItem);
            }
        });
        refreshMenu.getItems().addAll(refreshItem);

        MenuBar menuBar = new MenuBar(this.inputMenu, this.refreshMenu);
        menuBar.setBackground(Background.fill(PURPLE_MID));
        vBox.getChildren().add(menuBar);

        vBox.getChildren().add(this.canvas);
        this.stage.setScene(new Scene(this.vBox));

        this.pianoView = new PianoView(-1, 2);

        // update GUI on incoming MIDI event
        MidiDeviceManager.getInstance().addMessageListener(new MidiMessageListener() {
            @Override public void noteOn(int channel, int key, int velocity) { draw(); }
            @Override public void noteOff(int channel, int key, int velocity) { draw(); }
        });

        stage.show();
    }

    private void draw() {
        this.canvas.getGraphicsContext2D().setFill(PURPLE_DARK);
        this.canvas.getGraphicsContext2D().fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        this.pianoView.draw(this.canvas.getGraphicsContext2D());

    }
}
