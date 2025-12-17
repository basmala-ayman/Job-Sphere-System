package com.jobsphere.controller;

import com.jobsphere.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.jobsphere.model.User;
import com.jobsphere.service.auth.Authentication;
import com.jobsphere.service.auth.RegistrationResult;
import com.jobsphere.service.auth.SessionManager;
import com.jobsphere.service.creator.UserCreator;
import com.jobsphere.service.creator.UserCreatorFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private Label msg;

    @FXML
    private void handleLogin() {
        RegisterController rc = new RegisterController();
        String mail = email.getText().trim();
        String pass = password.getText();

        if (mail.isEmpty() || pass.isEmpty()) {
            msg.setText("Please fill all fields!!");
            return;
        }

        if (!rc.isValidEmail(mail)) {
            msg.setText("Please enter a valid email!!");
            return;
        }

        loginBtn.setDisable(true);

        try {
            User currentUser = UserDAO.getInstance().login(mail, pass);
            if (currentUser != null) {
                // store current user into session manager
                SessionManager.getInstance().setCurrentUser(currentUser);
                msg.setStyle("-fx-text-fill: green;");
                msg.setText("Logged in successfully!!");
            } else {
                msg.setText("Logging failed!! Check your email and password!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setText("An unexpected error occurred!!");
        } finally {
            loginBtn.setDisable(false);
        }

    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Register.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) registerBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}












