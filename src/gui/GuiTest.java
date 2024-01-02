package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Empty Window Example");

        // Creating an empty scene
        Scene scene = new Scene(new javafx.scene.layout.Region(), 400, 300);

        // Setting the scene to the stage
        primaryStage.setScene(scene);
        // Showing the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
