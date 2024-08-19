package Hospital_Management_System_Package;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.sql.*;

public class Hospital_Manage_System {

    public static void main(String[] args) throws ClassNotFoundException, RuntimeException {

        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Error loading config.properties file: " + e.getMessage());
            return;
        }

        // Get the database credentials from the properties file
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Established");
            Scanner sc = new Scanner(System.in);
            Patient_class patient = new Patient_class(con, sc);
            Doctor_class doctor = new Doctor_class(con, sc);

            while (true) {
                System.out.println("Enter your choice");
                System.out.println("1. Add Patient \n2. View Patients \n3. View Doctor \n4. Book Appointment \n5. Exit \n");
                System.out.println("Your choice: ");
                int c = sc.nextInt();
                sc.nextLine();
                System.out.println();

                switch (c) {
                    case 1: {
                        System.out.println("ADD PATIENT \n");
                        patient.addPatient();
                        break;
                    }
                    case 2: {
                        System.out.println("VIEW PATIENT \n");
                        patient.viewPatient();
                        break;
                    }
                    case 3: {
                        System.out.println("VIEW DOCTOR \n");
                        doctor.viewDoctor();
                        break;
                    }
                    case 4: {
                        System.out.println("BOOK APPOINTMENT \n");
                        System.out.println("Enter patient ID");
                        int patient_id = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter doctor ID");
                        int doctor_id = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter Appointment date in format YYYY-MM-DD");
                        String date = sc.nextLine();
                        bookAppointment(patient_id, doctor_id, date, con, patient, doctor);
                        break;
                    }
                    case 5: {
                        System.out.println("THANK YOU FOR USING OUR SYSTEM");
                        System.exit(0);
                        break;
                    }
                    default: {
                        System.out.println("WRONG CHOICE !!!! ");
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void bookAppointment(int patient_id, int doctor_id, String date, Connection con, Patient_class patient, Doctor_class doctor) {
        try {
            if (patient.checkPatient(patient_id) && doctor.checkDoctor(doctor_id)) {
                if (checkAvailability(doctor_id, date, con)) {
                    String query = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?,?,?);";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, patient_id);
                    ps.setInt(2, doctor_id);
                    ps.setString(3, date);
                    int row = ps.executeUpdate();
                    if (row > 0)
                        System.out.println("ENTRY SUCCESSFUL !! ");
                    else
                        System.out.println("ENTRY UNSUCCESSFUL ");
                } else {
                    System.out.println("Doctor is already booked \n TRY ANOTHER DATE \n");
                }

            } else {
                System.out.println("WRONG PATIENT ID OR DOCTOR ID !! \n");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean checkAvailability(int id, String date, Connection con) {
        try {
            String query = "SELECT appointment_date FROM appointments WHERE doctor_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String apdate = rs.getString("appointment_date");
                if (apdate.equals(date))
                    return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
