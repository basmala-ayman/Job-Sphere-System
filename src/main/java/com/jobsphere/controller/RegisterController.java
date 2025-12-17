package com.jobsphere.controller;

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

public class RegisterController {

    // user data
    @FXML
    private TextField fullName;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private ComboBox<String> role;

    // applicant data
    @FXML
    private VBox applicantBox;
    @FXML
    private TextField phone;
    @FXML
    private TextField skillInput;
    @FXML
    private ListView<String> skillsList;
    @FXML
    private TextField experience;

    // company data
    @FXML
    private VBox companyBox;
    @FXML
    private TextField companyWebsite;
    @FXML
    private TextField companyIndustry;
    @FXML
    private TextArea companyDescription;

    // message for errors
    @FXML
    private Label msg;
    // buttons
    @FXML
    private Button registerBtn;
    @FXML
    private Button loginBtn;

    private final Authentication auth = new Authentication();

    @FXML
    public void initialize() {
        // role's ComboBox
        ObservableList<String> roles = FXCollections.observableArrayList("Applicant", "Company");
        role.setItems(roles);

        manageBox(applicantBox, false);
        manageBox(companyBox, false);

        role.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if ("applicant".equalsIgnoreCase(newValue)) {
                manageBox(applicantBox, true);
                manageBox(companyBox, false);
            } else if ("company".equalsIgnoreCase(newValue)) {
                manageBox(applicantBox, false);
                manageBox(companyBox, true);
            } else {
                manageBox(applicantBox, false);
                manageBox(companyBox, false);
            }
        });

        skillsList.setItems((FXCollections.observableArrayList()));
    }

    @FXML
    private void onAddSkill() {
        String skill = skillInput.getText();
        if (skill != null) {
            skill = skill.trim();
            if (!skill.isEmpty()) {
                skillsList.getItems().add(skill);
                skillInput.clear();
            }
        }
    }

    @FXML
    private void onRemoveSkill() {
        String skillSelected = skillsList.getSelectionModel().getSelectedItem();
        if (skillSelected != null) {
            skillsList.getItems().remove(skillSelected);
        }
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
        }
    }

    String name, mail, pass, confirmPass, selectedRole, applicantPhone, comWebsite, comIndustry;

    @FXML
    private void onRegisterClicked() {
        msg.setText("");
        msg.setStyle("-fx-text-fill: red;");

        name = fullName.getText().trim();
        mail = email.getText().trim();
        pass = password.getText();
        confirmPass = confirmPassword.getText();
        selectedRole = role.getSelectionModel().getSelectedItem();

        applicantPhone = phone.getText().trim();

        comWebsite = companyWebsite.getText().trim();
        comIndustry = companyIndustry.getText().trim();

        // validation
        if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || selectedRole == null) {
            msg.setText("Please fill all fields!!");
            return;
        }

        if (!isValidEmail(mail)) {
            msg.setText("Please enter a valid email!!");
            return;
        }

        // check on confirmation of password
        if (!pass.equals(confirmPass)) {
            msg.setText("Passwords don't match!!");
            return;
        }

        if (pass.length() < 6) {
            msg.setText("Passwords must be at least 6 characters!!");
            return;
        }

        // validation for applicant
        if (selectedRole.equalsIgnoreCase("applicant")) {
            if (applicantPhone.isEmpty()) {
                msg.setText("Please fill the fields!!");
                return;
            }
        } else if (selectedRole.equalsIgnoreCase("company")) {
            if (comWebsite.isEmpty() || comIndustry.isEmpty()) {
                msg.setText("Please fill the fields!!");
                return;
            }
        }

        // to avoid double clicks
        registerBtn.setDisable(true);

        try {
            UserCreator userCreator = UserCreatorFactory.getUserCreator(selectedRole);
            User user = userCreator.createUser(this);
            user.setUsername(name);
            user.setEmail(mail);
            user.setPassword(pass);
            user.setRole(selectedRole);

            RegistrationResult result = auth.registerUserAuth(user, selectedRole);
            if (result.isSuccess()) {

                msg.setStyle("-fx-text-fill: green;");
                msg.setText("Registration successfully!!");
                clearForm();
                msg.setText("");
                goToLogin();

            } else {
                msg.setText("Registration failed!! Email may already exist! Please try to login.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setText("An unexpected error occurred!!");
        } finally {
            registerBtn.setDisable(false);
        }

    }

    public String getCompanyDescription() {
        return companyDescription == null ? "" : companyDescription.getText();
    }

    public String getCompanyIndustry() {
        return comIndustry;
    }

    public String getCompanyWebsite() {
        return comWebsite;
    }

    public String getSkills() {
        return convertToJSON(skillsList.getItems());
    }

    public int getExperience() {
        String ex = experience.getText().trim();
        if (ex.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(ex);
        }
    }

    public String getPhone() {
        return applicantPhone;
    }

    private void clearForm() {
        fullName.clear();
        email.clear();
        password.clear();
        confirmPassword.clear();
        role.getSelectionModel().clearSelection();
        phone.clear();
        skillInput.clear();
        skillsList.getItems().clear();
        experience.clear();
        companyWebsite.clear();
        companyIndustry.clear();
        companyDescription.clear();
        msg.setText("");
        manageBox(applicantBox, false);
        manageBox(companyBox, false);
    }

    private void manageBox(VBox box, boolean status) {
        box.setVisible(status);
        box.setManaged(status);
    }

    public boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private String convertToJSON(ObservableList<String> skills) {
        if (skills.isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < skills.size(); i++) {
            sb.append("\"").append(skills.get(i).replace("\"", "\\\"")).append("\"");
            if (i < skills.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}