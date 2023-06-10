
package finnalpr;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class AdminDashboardPage extends Application {
    private Connection connection;
    private Statement statement;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");

        // Establish database connection
        connectToDatabase();

        Button showRegisteredPatientsButton = new Button("Show Registered Patients");
        showRegisteredPatientsButton.setOnAction(e -> showRegisteredPatients());

        Button searchPatientsButton = new Button("Search Patients");
        searchPatientsButton.setOnAction(e -> searchPatients());

        Button createNewPatientButton = new Button("Create New Patient");
        createNewPatientButton.setOnAction(e -> createNewPatient());

        Button updatePatientButton = new Button("Update Patient");
        updatePatientButton.setOnAction(e -> updatePatient());

        Button deletePatientButton = new Button("Delete Patient");
        deletePatientButton.setOnAction(e -> deletePatient());

        Button showFreeAppointmentsButton = new Button("Show Free Appointments");
        showFreeAppointmentsButton.setOnAction(e -> showFreeAppointments());

        Button createNewAppointmentButton = new Button("Create New Appointment");
        createNewAppointmentButton.setOnAction(e -> createNewAppointment());

        Button updateAppointmentButton = new Button("Update Appointment");
        updateAppointmentButton.setOnAction(e -> updateAppointment());

        Button deleteAppointmentButton = new Button("Delete Appointment");
        deleteAppointmentButton.setOnAction(e -> deleteAppointment());

        Button showBookedAppointmentsButton = new Button("Show Booked Appointments");
        showBookedAppointmentsButton.setOnAction(e -> showBookedAppointments());

        Button searchBookedAppointmentsButton = new Button("Search Booked Appointments");
        searchBookedAppointmentsButton.setOnAction(e -> searchBookedAppointments());

        Button addCommentToBookedAppointmentButton = new Button("Add Comment to Booked Appointment");
        addCommentToBookedAppointmentButton.setOnAction(e -> addCommentToBookedAppointment());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            try {
                // Close the database connection
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            primaryStage.close();
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(showRegisteredPatientsButton, searchPatientsButton,
                createNewPatientButton, updatePatientButton, deletePatientButton,
                showFreeAppointmentsButton, createNewAppointmentButton,
                updateAppointmentButton, deleteAppointmentButton,
                showBookedAppointmentsButton, searchBookedAppointmentsButton,
                addCommentToBookedAppointmentButton, logoutButton);

        Scene scene = new Scene(vbox, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            // Configure database connection details
            String url = "jdbc:mysql://localhost:3306/clinic_appointments";
            String username = "root";
            String password = "";

            // Establish the database connection
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showRegisteredPatients() {
        try {
            // Execute SQL query to retrieve all registered patients
            String query = "SELECT * FROM users WHERE role = 'patient'";
            ResultSet resultSet = statement.executeQuery(query);

            // Process and display the results
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String gender = resultSet.getString("gender");

                System.out.println("ID: " + id);
                System.out.println("Username: " + username);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Age: " + age);
                System.out.println("Email: " + email);
                System.out.println("Phone: " + phone);
                System.out.println("Gender: " + gender);
                System.out.println();
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchPatients() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Patients");
        dialog.setHeaderText("Enter patient's first name:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(firstName -> {
            try {
                // Execute SQL query to search for patients by first name
                String query = "SELECT * FROM users WHERE role = 'patient' AND firstname = '" + firstName + "'";
                ResultSet resultSet = statement.executeQuery(query);

                // Process and display the results
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String lastName = resultSet.getString("lastname");
                    int age = resultSet.getInt("age");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    String gender = resultSet.getString("gender");

                    System.out.println("ID: " + id);
                    System.out.println("Username: " + username);
                    System.out.println("First Name: " + firstName);
                    System.out.println("Last Name: " + lastName);
                    System.out.println("Age: " + age);
                    System.out.println("Email: " + email);
                    System.out.println("Phone: " + phone);
                    System.out.println("Gender: " + gender);
                    System.out.println();
                }

                // Close the result set
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void createNewPatient() {
        Dialog<ButtonType> createDialog = new Dialog<>();
        createDialog.setTitle("Create New Patient");
        createDialog.setHeaderText("Enter patient details:");

        // Set up input fields
        createDialog.getDialogPane().setContentText("Username:");
        TextField usernameField = new TextField();
        createDialog.getDialogPane().setContent(usernameField);

        createDialog.getDialogPane().setContentText("Password:");
        PasswordField passwordField = new PasswordField();
        createDialog.getDialogPane().setContent(passwordField);

        // ... Add more input fields as needed ...

        // Add buttons to the dialog
        ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        createDialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

        // Process the dialog result
        createDialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == createButton) {
                return usernameField.getText(); // Return the new username
            }
            return null;
        });

        Optional<ButtonType> createResult = createDialog.showAndWait();
        createResult.ifPresent(newUsername -> {
            try {
                // Create a new patient in the database
                String createQuery = "INSERT INTO users (username, password, role) VALUES ('" + newUsername + "', '" + passwordField.getText() + "', 'patient')";
                statement.executeUpdate(createQuery);
                System.out.println("New patient created successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updatePatient() {
        Dialog<ButtonType> updateDialog = new Dialog<>();
        updateDialog.setTitle("Update Patient");
        updateDialog.setHeaderText("Enter patient ID:");

        // Set up input fields
        updateDialog.getDialogPane().setContentText("Patient ID:");
        TextField idField = new TextField();
        updateDialog.getDialogPane().setContent(idField);

        // Add buttons to the dialog
        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        updateDialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        // Process the dialog result
        updateDialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == updateButton) {
                return idField.getText(); // Return the patient ID to update
            }
            return null;
        });

        Optional<ButtonType> updateResult = updateDialog.showAndWait();
        updateResult.ifPresent(patientId -> {
            // Show a new dialog to update patient details
            Dialog<ButtonType> updateDetailsDialog = new Dialog<>();
            updateDetailsDialog.setTitle("Update Patient Details");
            updateDetailsDialog.setHeaderText("Update details for patient ID: " + patientId);

            // Set up input fields for updated details
            updateDetailsDialog.getDialogPane().setContentText("New First Name:");
            TextField firstNameField = new TextField();
            updateDetailsDialog.getDialogPane().setContent(firstNameField);

            // ... Add more input fields as needed ...

            // Add buttons to the dialog
            ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            updateDetailsDialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

            // Process the dialog result
            updateDetailsDialog.setResultConverter((ButtonType dialogButton) -> {
                if (dialogButton == saveButton) {
                    return firstNameField.getText(); // Return the updated first name
                }
                return null;
            });

            Optional<ButtonType> updateDetailsResult = updateDetailsDialog.showAndWait();
            updateDetailsResult.ifPresent(newFirstName -> {
                try {
                    // Update the patient's first name in the database
                    String updateQuery = "UPDATE users SET firstname = '" + newFirstName + "' WHERE id = " + patientId;
                    statement.executeUpdate(updateQuery);
                    System.out.println("Patient details updated successfully!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void deletePatient() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Patient");
        dialog.setHeaderText("Enter patient ID:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(patientId -> {
            try {
                // Delete the patient from the database
                String deleteQuery = "DELETE FROM users WHERE id = " + patientId;
                statement.executeUpdate(deleteQuery);
                System.out.println("Patient deleted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void showFreeAppointments() {
        try {
            // Execute SQL query to retrieve all free appointments
            String query = "SELECT * FROM appointments WHERE status = 'free'";
            ResultSet resultSet = statement.executeQuery(query);

            // Process and display the results
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate appointmentDate = resultSet.getDate("appointment_date").toLocalDate();
                String appointmentDay = resultSet.getString("appointment_day");
                LocalTime appointmentTime = resultSet.getTime("appointment_time").toLocalTime();

                System.out.println("ID: " + id);
                System.out.println("Date: " + appointmentDate);
                System.out.println("Day: " + appointmentDay);
                System.out.println("Time: " + appointmentTime);
                System.out.println();
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createNewAppointment() {
        Dialog<ButtonType> createDialog = new Dialog<>();
        createDialog.setTitle("Create New Appointment");
        createDialog.setHeaderText("Enter appointment details:");

        // Set up input fields
        DatePicker datePicker = new DatePicker();
        createDialog.getDialogPane().setContent(datePicker);

        // ... Add more input fields as needed ...

        // Add buttons to the dialog
        ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        createDialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

        // Process the dialog result
        createDialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == createButton) {
                return datePicker.getValue(); // Return the selected appointment date
            }
            return null;
        });

        Optional<ButtonType> createResult = createDialog.showAndWait();
        createResult.ifPresent(selectedDate -> {
            try {
                // Create a new appointment in the database
                String createQuery = "INSERT INTO appointments (appointment_date, status) VALUES ('" + selectedDate + "', 'free')";
                statement.executeUpdate(createQuery);
                System.out.println("New appointment created successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateAppointment() {
        Dialog<ButtonType> updateDialog = new Dialog<>();
        updateDialog.setTitle("Update Appointment");
        updateDialog.setHeaderText("Enter appointment ID:");

        // Set up input fields
        updateDialog.getDialogPane().setContentText("Appointment ID:");
        TextField idField = new TextField();
        updateDialog.getDialogPane().setContent(idField);

        // Add buttons to the dialog
        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        updateDialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        // Process the dialog result
        updateDialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == updateButton) {
                return idField.getText(); // Return the appointment ID to update
            }
            return null;
        });

        Optional<ButtonType> updateResult = updateDialog.showAndWait();
        updateResult.ifPresent((ButtonType appointmentId) -> {
            // Show a new dialog to update appointment details
            Dialog<ButtonType> updateDetailsDialog = new Dialog<>();
            updateDetailsDialog.setTitle("Update Appointment Details");
            updateDetailsDialog.setHeaderText("Update details for appointment ID: " + appointmentId);

            // Set up input fields for updated details
            updateDetailsDialog.getDialogPane().setContentText("New Date:");
            DatePicker datePicker = new DatePicker();
            updateDetailsDialog.getDialogPane().setContent(datePicker);

            // ... Add more input fields as needed ...

            // Add buttons to the dialog
            ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            updateDetailsDialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

            // Process the dialog result
            updateDetailsDialog.setResultConverter((ButtonType dialogButton) -> {
                if (dialogButton == saveButton) {
                    return datePicker.getValue(); // Return the updated date
                }
                return null;
            });

            Optional<ButtonType> updateDetailsResult = updateDetailsDialog.showAndWait();
            updateDetailsResult.ifPresent(newDate -> {
                try {
                    // Update the appointment date in the database
                    String updateQuery = "UPDATE appointments SET appointment_date = '" + newDate + "' WHERE id = " + appointmentId;
                    statement.executeUpdate(updateQuery);
                    System.out.println("Appointment details updated successfully!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void deleteAppointment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Appointment");
        dialog.setHeaderText("Enter appointment ID:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(appointmentId -> {
            try {
                // Delete the appointment from the database
                String deleteQuery = "DELETE FROM appointments WHERE id = " + appointmentId;
                statement.executeUpdate(deleteQuery);
                System.out.println("Appointment deleted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void showBookedAppointments() {
        try {
            // Execute SQL query to retrieve all booked appointments
            String query = "SELECT * FROM appointments WHERE status = 'booked'";
            ResultSet resultSet = statement.executeQuery(query);

            // Process and display the results
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate appointmentDate = resultSet.getDate("appointment_date").toLocalDate();
                String appointmentDay = resultSet.getString("appointment_day");
                LocalTime appointmentTime = resultSet.getTime("appointment_time").toLocalTime();
                int patientId = resultSet.getInt("patient_id");

                System.out.println("ID: " + id);
                System.out.println("Date: " + appointmentDate);
                System.out.println("Day: " + appointmentDay);
                System.out.println("Time: " + appointmentTime);
                System.out.println("Patient ID: " + patientId);
                System.out.println();
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchBookedAppointments() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Booked Appointments");
        dialog.setHeaderText("Enter patient ID:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(patientId -> {
            try {
                // Execute SQL query to search for booked appointments by patient ID
                String query = "SELECT * FROM appointments WHERE status = 'booked' AND patient_id = " + patientId;
                ResultSet resultSet = statement.executeQuery(query);

                // Process and display the results
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    LocalDate appointmentDate = resultSet.getDate("appointment_date").toLocalDate();
                    String appointmentDay = resultSet.getString("appointment_day");
                    LocalTime appointmentTime = resultSet.getTime("appointment_time").toLocalTime();

                    System.out.println("ID: " + id);
                    System.out.println("Date: " + appointmentDate);
                    System.out.println("Day: " + appointmentDay);
                    System.out.println("Time: " + appointmentTime);
                    System.out.println("Patient ID: " + patientId);
                    System.out.println();
                }

                // Close the result set
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void addCommentToBookedAppointment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Comment to Booked Appointment");
        dialog.setHeaderText("Enter appointment ID:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(appointmentId -> {
            TextInputDialog commentDialog = new TextInputDialog();
            commentDialog.setTitle("Add Comment to Booked Appointment");
            commentDialog.setHeaderText("Enter comment:");
            Optional<String> commentResult = commentDialog.showAndWait();

            commentResult.ifPresent(comment -> {
                try {
                    // Update the comment for the booked appointment in the database
                    String updateQuery = "UPDATE appointments SET comment = '" + comment + "' WHERE id = " + appointmentId;
                    statement.executeUpdate(updateQuery);
                    System.out.println("Comment added successfully!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void cancelBookedAppointment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cancel Booked Appointment");
        dialog.setHeaderText("Enter appointment ID:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(appointmentId -> {
            try {
                // Cancel the booked appointment by updating its status in the database
                String updateQuery = "UPDATE appointments SET status = 'free', patient_id = NULL WHERE id = " + appointmentId;
                statement.executeUpdate(updateQuery);
                System.out.println("Appointment canceled successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    void showDashboard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setConnection(Connection connection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setStatement(Statement statement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}

