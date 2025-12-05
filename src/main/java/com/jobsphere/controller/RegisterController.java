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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
    private Button cancelBtn;

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
    private void onCancelClicked() {
        clearForm();
        msg.setText("");
    }

    @FXML
    private void onRegisterClicked() {
        msg.setText("");
        msg.setStyle("-fx-text-fill: red;");

        String name = fullName.getText().trim();
        String mail = email.getText().trim();
        String pass = password.getText();
        String confirmPass = confirmPassword.getText();
        String selectedRole = role.getSelectionModel().getSelectedItem();

        // validation
        if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || selectedRole == null) {
            msg.setText("Please fill all the fields!!");
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
                User sessionUser = new User();
                sessionUser.setId(result.getUserId());
                sessionUser.setUsername(user.getUsername());
                sessionUser.setEmail(mail);
                sessionUser.setPassword(pass);
                sessionUser.setRole(selectedRole);
                // store current user into session manager
                SessionManager.getInstance().setCurrentUser(sessionUser);

                msg.setStyle("-fx-text-fill: green;");
                msg.setText("Registration successfully!!");
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
        return companyIndustry == null ? "" : companyIndustry.getText();
    }

    public String getCompanyWebsite() {
        return companyWebsite == null ? "" : companyWebsite.getText();
    }

    public String getSkills() {
        return skillsList == null ? "" : convertToJSON(skillsList.getItems());
    }

    public int getExperience() {
        return experience == null ? 0 : Integer.parseInt(experience.getText().trim());
    }

    public String getPhone() {
        return phone == null ? "" : phone.getText().trim();
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

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private String convertToJSON(ObservableList<String> skills) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < skills.size(); i++) {
            sb.append("\"").append(skills.get(i).replace("\"", "\\\"")).append("\"");
            if (i < skills.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}