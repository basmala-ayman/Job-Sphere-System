package com.jobsphere.controller;

import com.jobsphere.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.jobsphere.model.User;
import com.jobsphere.service.auth.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
                goToFeatures(currentUser.getRole());
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

    private void goToFeatures(String role) {
        String form = "";
        if (role.equalsIgnoreCase("applicant"))
            form = "ApplicantFeatures.fxml";
        else if (role.equalsIgnoreCase("company"))
            form = "CompanyFeatures.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/" + form));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}












