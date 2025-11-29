package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage stage) {
        Label label = new Label("Welcome to Design Patterns Project my Hero Team!");
        Scene scene = new Scene(label, 400, 200);
        stage.setTitle("Design Patterns");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}