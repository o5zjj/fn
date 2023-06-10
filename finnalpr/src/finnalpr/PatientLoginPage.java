/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finnalpr;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class PatientLoginPage {
    private Connection connection;
    private Statement statement;
    // ...

    public void login() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Patient Login");
        dialog.setHeaderText("Enter username:");
        Optional<String> usernameResult = dialog.showAndWait();

        usernameResult.ifPresent(username -> {
            PasswordDialog passwordDialog = new PasswordDialog();
            Optional<String> passwordResult = passwordDialog.showAndWait();

            passwordResult.ifPresent(password -> {
                // Execute SQL query to validate patient login credentials
                String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "' AND role = 'patient'";
                ResultSet resultSet = null;
                try {
                    resultSet = statement.executeQuery(query);
                } catch (SQLException ex) {
                    Logger.getLogger(PatientLoginPage.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (resultSet.next()) {
                        System.out.println("Patient login successful!");
                        PatientDashboardPage patientDashboard = new PatientDashboardPage();
                        patientDashboard.setConnection(connection);
                        patientDashboard.setStatement(statement);
                        patientDashboard.setPatientId(resultSet.getInt("id"));
                        patientDashboard.showDashboard();
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PatientLoginPage.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    // Close the result set
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PatientLoginPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }

    // ...

    void start(Stage stage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

