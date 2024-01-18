package de.bujanowski.midijazz.gui;

import de.bujanowski.midijazz.theory.music.MusicTheory;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //load MusicTheory
        MusicTheory musicTheory = MusicTheory.getInstance();
        new MainWindow(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
