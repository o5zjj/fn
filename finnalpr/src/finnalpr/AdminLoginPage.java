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
import java.util.function.Consumer;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class AdminLoginPage {
    private Connection connection;
    private Statement statement;
    // ...

    public void login() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Admin Login");
        dialog.setHeaderText("Enter username:");
        Optional<String> usernameResult = dialog.showAndWait();

        usernameResult.ifPresent((String username) -> {
            PasswordDialog passwordDialog = new PasswordDialog();
            Optional<String> passwordResult = passwordDialog.showAndWait();

            passwordResult.ifPresent(new Consumer<String>() {
                @Override
                public void accept(String password) {
                    try {
                        // Execute SQL query to validate admin login credentials
                        String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "' AND role = 'admin'";
                        ResultSet resultSet = statement.executeQuery(query);
                        
                        if (resultSet.next()) {
                            System.out.println("Admin login successful!");
                            AdminDashboardPage adminDashboard = new AdminDashboardPage();
                            adminDashboard.setConnection(connection);
                            adminDashboard.setStatement(statement);
                            adminDashboard.showDashboard();
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                        
                        // Close the result set
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    // ...

    void start(Stage stage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

