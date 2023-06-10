/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finnalpr;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginSelectionPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Selection");

        Button adminLoginButton = new Button("Admin Login");
        adminLoginButton.setOnAction(e -> openAdminLoginPage());

        Button patientLoginButton = new Button("Patient Login");
        patientLoginButton.setOnAction(e -> openPatientLoginPage());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(adminLoginButton, patientLoginButton);

        Scene scene = new Scene(vbox, 200, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openAdminLoginPage() {
        AdminLoginPage adminLoginPage = new AdminLoginPage();
        adminLoginPage.setLoginListener(credentials -> {
            // Simulating login verification
            if (credentials.getUsername().equals("admin") && credentials.getPassword().equals("admin123")) {
                openAdminDashboard();
            } else {
                showErrorAlert("Invalid credentials");
            }
        });
        adminLoginPage.start(new Stage());
    }

    private void openPatientLoginPage() {
        PatientLoginPage patientLoginPage = new PatientLoginPage();
        patientLoginPage.setLoginListener(credentials -> {
            // Simulating login verification
            if (credentials.getUsername().equals("patient") && credentials.getPassword().equals("patient123")) {
                openPatientDashboard();
            } else {
                showErrorAlert("Invalid credentials");
            }
        });
        patientLoginPage.start(new Stage());
    }

    private void openAdminDashboard() {
        AdminDashboardPage adminDashboardPage = new AdminDashboardPage();
        adminDashboardPage.start(new Stage());
    }

    private void openPatientDashboard() {
        PatientDashboardPage patientDashboardPage = new PatientDashboardPage();
        patientDashboardPage.start(new Stage());
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
