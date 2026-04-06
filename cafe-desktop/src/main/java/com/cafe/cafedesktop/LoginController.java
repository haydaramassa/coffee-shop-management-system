package com.cafe.cafedesktop;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import com.cafe.cafedesktop.service.ApiService;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField fp_answer;

    @FXML
    private TextField fp_username;

    @FXML
    private Button fp_back;

    @FXML
    private Button fp_proceed;

    @FXML
    private ComboBox<String> fp_question;

    @FXML
    private Button np_back;

    @FXML
    private Button np_changePassBtn;

    @FXML
    private PasswordField np_confirmPassword;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private PasswordField np_newPassword;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private Hyperlink si_forgotpass;

    @FXML
    private Button si_loginBtn;

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private Button side_CreatBtn;

    @FXML
    private Button side_alreadyHave;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_answer;

    @FXML
    private PasswordField su_password;

    @FXML
    private ComboBox<String> su_question;

    @FXML
    private Button su_signupBtn;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private TextField su_username;

    private final ApiService apiService = new ApiService();
    private String resetUsername;
    private String resetQuestion;
    private String resetAnswer;

    private final String[] questionList = {
            "What`s your favourite color?",
            "What`s your favourite food?",
            "When`s your birthday?"
    };

    @FXML
    public void initialize() {
        fp_questionForm.setVisible(false);
        np_newPassForm.setVisible(false);
        si_loginForm.setVisible(true);

        side_alreadyHave.setVisible(false);
        side_CreatBtn.setVisible(true);

        regQuestionList();
        forgotPassQuestionList();
    }

    public void regQuestionList() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        su_question.setItems(listData);
    }

    public void forgotPassQuestionList() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        fp_question.setItems(listData);
    }

    @FXML
    public void switchForgotPass() {
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);
        np_newPassForm.setVisible(false);
        forgotPassQuestionList();
    }

    @FXML
    public void backToLOginForm() {
        fp_username.clear();
        fp_answer.clear();
        fp_question.getSelectionModel().clearSelection();

        np_newPassword.clear();
        np_confirmPassword.clear();

        si_username.clear();
        si_password.clear();

        resetUsername = null;
        resetQuestion = null;
        resetAnswer = null;

        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);
        np_newPassForm.setVisible(false);
    }

    @FXML
    public void backToQuestionForm() {
        fp_username.clear();
        fp_answer.clear();
        fp_question.getSelectionModel().clearSelection();

        np_newPassword.clear();
        np_confirmPassword.clear();

        resetUsername = null;
        resetQuestion = null;
        resetAnswer = null;

        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);
    }

    @FXML
    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == side_CreatBtn) {
            clearLoginFields();

            slider.setNode(side_form);
            slider.setToX(450);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreatBtn.setVisible(false);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

                regQuestionList();
            });
            slider.play();

        } else if (event.getSource() == side_alreadyHave) {
            clearRegisterFields();

            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreatBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });
            slider.play();
        }
    }

    @FXML
    public void loginBtn() {
        if (si_username.getText().isBlank() || si_password.getText().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Please enter username and password");
            return;
        }

        try {
            String role = apiService.login(si_username.getText(), si_password.getText());

            if ("ADMIN".equalsIgnoreCase(role) || "CASHIER".equalsIgnoreCase(role)) {
                openMainScreen(si_username.getText(), role);
            } else if ("INVALID_CREDENTIALS".equals(role)) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Could not complete login");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not connect to backend: " + e.getMessage());
        }
    }

    @FXML
    public void regBtn() {
        if (su_username.getText().isBlank()
                || su_password.getText().isBlank()
                || su_question.getSelectionModel().getSelectedItem() == null
                || su_answer.getText().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");
            return;
        }
        if (!isPasswordValid(su_password.getText())) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Weak Password",
                    "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
            );
            return;
        }

        try {
            String result = apiService.register(
                    su_username.getText(),
                    su_password.getText(),
                    su_question.getSelectionModel().getSelectedItem(),
                    su_answer.getText()
            );

            if ("SUCCESS".equals(result)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful");

                su_username.clear();
                su_password.clear();
                su_answer.clear();
                su_question.getSelectionModel().clearSelection();

                TranslateTransition slider = new TranslateTransition();
                slider.setNode(side_form);
                slider.setToX(0);
                slider.setDuration(Duration.seconds(.5));
                slider.setOnFinished((ActionEvent e) -> {
                    side_alreadyHave.setVisible(false);
                    side_CreatBtn.setVisible(true);

                    si_loginForm.setVisible(true);
                    fp_questionForm.setVisible(false);
                    np_newPassForm.setVisible(false);
                });
                slider.play();

            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", result);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not connect to backend: " + e.getMessage());
        }
    }

    @FXML
    public void proceedBtn() {
        if (fp_username.getText().isBlank()
                || fp_question.getSelectionModel().getSelectedItem() == null
                || fp_answer.getText().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");
            return;
        }

        try {
            String username = fp_username.getText().trim();
            String question = fp_question.getSelectionModel().getSelectedItem();
            String answer = fp_answer.getText().trim();

            String result = apiService.verifyResetData(username, question, answer);

            if ("SUCCESS".equals(result)) {
                resetUsername = username;
                resetQuestion = question;
                resetAnswer = answer;

                np_newPassForm.setVisible(true);
                fp_questionForm.setVisible(false);
            } else {
                showAlert(Alert.AlertType.ERROR, "Verification Failed", result);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not connect to backend: " + e.getMessage());
        }
    }

    @FXML
    public void changePassBtn() {
        if (np_newPassword.getText().isBlank() || np_confirmPassword.getText().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");
            return;
        }

        if (!np_newPassword.getText().equals(np_confirmPassword.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Password does not match");
            return;
        }

        if (!isPasswordValid(np_newPassword.getText())) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Weak Password",
                    "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
            );
            return;
        }

        if (resetUsername == null || resetQuestion == null || resetAnswer == null) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Reset session is invalid. Please try again.");
            return;
        }

        try {
            String result = apiService.resetPassword(
                    resetUsername,
                    resetQuestion,
                    resetAnswer,
                    np_newPassword.getText()
            );

            if ("SUCCESS".equals(result)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully");

                fp_username.clear();
                fp_answer.clear();
                fp_question.getSelectionModel().clearSelection();
                np_newPassword.clear();
                np_confirmPassword.clear();

                resetUsername = null;
                resetQuestion = null;
                resetAnswer = null;

                si_loginForm.setVisible(true);
                fp_questionForm.setVisible(false);
                np_newPassForm.setVisible(false);
            } else {
                showAlert(Alert.AlertType.ERROR, "Reset Failed", result);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not connect to backend: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openMainScreen(String username, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainForm.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setUserSession(username, role);

            Stage stage = (Stage) si_loginBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 1360, 780));
            stage.setTitle("Cafe Dashboard");
            stage.setMinWidth(1360);
            stage.setMinHeight(780);
            stage.centerOnScreen();
            stage.show();
          /*  stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Cafe Dashboard");
            stage.setMinWidth(1280);
            stage.setMinHeight(720);
            stage.centerOnScreen();
            stage.show();
*/
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not open main screen: " + e.getMessage());
        }
    }
    private long extractLong(String json, String key) {
        String value = extractValue(json, key);
        return Long.parseLong(value.contains(".") ? value.substring(0, value.indexOf('.')) : value);
    }

    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) {
            return "0";
        }

        start += pattern.length();

        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        if (start < json.length() && json.charAt(start) == '"') {
            start++;
            int end = json.indexOf('"', start);
            return json.substring(start, end);
        } else {
            int end = start;
            while (end < json.length() && ",}\n\r ".indexOf(json.charAt(end)) == -1) {
                end++;
            }
            return json.substring(start, end);
        }
    }
    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }

        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&^#()_+\\-={}\\[\\]:;\"'<>,./]).{8,}$");
    }
    private void clearLoginFields() {
        si_username.clear();
        si_password.clear();
    }
    private void clearRegisterFields() {
        su_username.clear();
        su_password.clear();
        su_answer.clear();
        su_question.getSelectionModel().clearSelection();
    }
}