import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Scanner;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class petuser {

  private static List<Integer> appointmentList = new ArrayList<>();
  public static void user_choice(Connection conn, String pOwnerId){
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Pet owner, Choose an option: \n1. View pet information " +
              "\n2. View appointment Information \n3. Reset Password \n4. Sign Out");
      System.out.println("-----------------------------");
      String pet_owner_choice = scanner.nextLine();

      switch (pet_owner_choice) {
        case "1":
          System.out.println("-----------------------------");
          System.out.println("Pet Info");
          System.out.println("-----------------------------");
          displayPetInfo(conn, pOwnerId);
          System.out.println("-----------------------------");
          break;
        case "2":
          System.out.println("-----------------------------");
          System.out.println("Appointment Info");
          System.out.println("-----------------------------");
          displayAppointmentsForOwner(conn, pOwnerId);
          appointment.user_choice(conn, appointmentList);
          System.out.println("-----------------------------");
          break;
        case "3":
          System.out.print("Enter new password: ");
          String newPassword = scanner.nextLine();
          updateOwnerPassword(conn, pOwnerId, newPassword);
          break;
        case "4":
          System.out.println("-----------------------------");
          System.out.println("Log out. Exiting program.");
          System.out.println("-----------------------------");
          return;
        default:
          System.out.println("-----------------------------");
          System.out.println("Invalid input. Please try again.");
          break;
      }
    }
  }

  private static void displayPetInfo(Connection conn, String pOwnerId) {
    try (CallableStatement stmt = conn.prepareCall("{CALL display_pet_info(?)}")) {

      stmt.setString(1, pOwnerId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int patientId = rs.getInt("patient_id");
        String petName = rs.getString("pet_name");
        String breedName = rs.getString("breed_name");
        Float height_in_cm = rs.getFloat("height_in_cm");
        Float weight_in_lb = rs.getFloat("weight_in_lb");
        String pet_status = rs.getString("pet_status");

        System.out.println( patientId + ", " + petName + ", " + breedName + ", " + height_in_cm + "cm, "+ weight_in_lb + "lb, " + pet_status);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void displayAppointmentsForOwner(Connection conn, String pOwnerId) {
    try (CallableStatement stmt = conn.prepareCall("{CALL display_appointments_for_owner(?)}")) {

      stmt.setString(1, pOwnerId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int appointmentId = rs.getInt("appointment_id");
        appointmentList.add(appointmentId);
        Timestamp appointmentDateTime = rs.getTimestamp("appointment_datetime");
        String description = rs.getString("appointment_description");
        int patientId = rs.getInt("patient_id");
        String petName = rs.getString("pet_name");
        int clinicId = rs.getInt("clinic_id");
        String show_up_status = rs.getString("show_up_status");
        String vet_id = rs.getString("vet_id");
        String vet_name = rs.getString("vet_name");
        int room_id = rs.getInt("room_id");

        // Display the appointment information
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("DateTime: " + appointmentDateTime);
        System.out.println("Description: " + description);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Pet Name: " + petName);
        System.out.println("Clinic ID: " + clinicId);
        System.out.println("Show Up: " + show_up_status);
        System.out.println("Vet ID: " + vet_id);
        System.out.println("Vet Name: " + vet_name);
        System.out.println("Room ID: " + room_id);
        System.out.println("");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  private static void updateOwnerPassword(Connection conn, String ownerId, String newPassword) {
    try (CallableStatement stmt = conn.prepareCall("{CALL update_owner_password(?, ?)}")) {
      stmt.setString(1, ownerId);
      stmt.setString(2, newPassword);
      int affectedRows = stmt.executeUpdate();
      if (affectedRows > 0) {
        System.out.println("Password updated successfully.");
      } else {
        System.out.println("Password update failed. Owner ID may not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
