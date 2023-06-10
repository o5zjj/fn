/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finnalpr;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class PatientDashboardPage extends Application {
    private Connection connection;
    private Statement statement;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Patient Dashboard");

        // Establish database connection
        connectToDatabase();

        Button showFreeAppointmentsButton = new Button("Show Free Appointments");
        showFreeAppointmentsButton.setOnAction(e -> showAllFreeAppointments());

        Button bookAppointmentButton = new Button("Book an Appointment");
        bookAppointmentButton.setOnAction(e -> bookAppointment());

        Button showBookedWaitingAppointmentsButton = new Button("Show Booked Waiting Appointments");
        showBookedWaitingAppointmentsButton.setOnAction(e -> showBookedWaitingAppointments());

        Button showBookedFinishedAppointmentsButton = new Button("Show Booked Finished Appointments");
        showBookedFinishedAppointmentsButton.setOnAction(e -> showBookedFinishedAppointments());

        Button viewDoctorCommentButton = new Button("View Doctor's Comment");
        viewDoctorCommentButton.setOnAction(e -> viewDoctorComment());

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
        vbox.getChildren().addAll(showFreeAppointmentsButton, bookAppointmentButton,
                showBookedWaitingAppointmentsButton, showBookedFinishedAppointmentsButton,
                viewDoctorCommentButton, logoutButton);

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
    public void showAllFreeAppointments() {
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

    public void bookAppointment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Book Appointment");
        dialog.setHeaderText("Enter appointment ID:");
        Optional<String> result = dialog.showAndWait();
        String appointmentId;

        result.ifPresent(  patientId -> {
            try {
                // Update the appointment status and patient ID in the database
                String updateQuery = "UPDATE appointments SET status = 'booked', patient_id = " + patientId + " WHERE id = " + appointmentId;
                statement.executeUpdate(updateQuery);
                System.out.println("Appointment booked successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void showBookedWaitingAppointments() {
        try {
            // Execute SQL query to retrieve all booked waiting appointments for the current patient
            String query = "SELECT * FROM appointments WHERE status = 'booked' " ;
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

    public void showBookedFinishedAppointments(String id) {
        try {
            // Execute SQL query to retrieve all booked finished appointments for the current patient
            String query = "SELECT * FROM appointments WHERE status = 'booked' AND patient_id = " + id;
            ResultSet resultSet = statement.executeQuery(query);

            // Process and display the results
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate appointmentDate = resultSet.getDate("appointment_date").toLocalDate();
                String appointmentDay = resultSet.getString("appointment_day");
                LocalTime appointmentTime = resultSet.getTime("appointment_time").toLocalTime();
                String comment = resultSet.getString("comment");

                System.out.println("ID: " + id);
                System.out.println("Date: " + appointmentDate);
                System.out.println("Day: " + appointmentDay);
                System.out.println("Time: " + appointmentTime);
                System.out.println("Comment: " + comment);
                System.out.println();
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     public void viewDoctorComment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Doctor's Comment");
        dialog.setHeaderText("Enter appointment ID:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(appointmentId -> {
            try {
                // Execute SQL query to retrieve the doctor's comment for the given appointment ID
                String query = "SELECT comment FROM appointments WHERE id = " + appointmentId + " AND status = 'booked'  " ;
                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next()) {
                    String comment = resultSet.getString("comment");
                    System.out.println("Doctor's Comment: " + comment);
                } else {
                    System.out.println("No comment found for the given appointment ID.");
                }

                // Close the result set
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    

    // ...

    void showDashboard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setPatientId(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setConnection(Connection connection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setStatement(Statement statement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
   

