package com.jobsphere.launcher;

import com.jobsphere.model.User;
import com.jobsphere.service.auth.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        // Set up mock user
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layouts/SearchCompany.fxml")); //will be changed later

        User mockUser = new User();
        mockUser.setId(3);
        mockUser.setUsername("Test Company");
        mockUser.setRole("Company");

        SessionManager.getInstance().setCurrentUser(mockUser);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(fxmlLoader.load(), 620, 640);
        stage.setTitle("JobSphere App");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}