import java.sql.ResultSet;
import java.util.List;
import java.util.Date;
import java.util.Scanner;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class appointment {

  private static boolean isValidInput = false;
  private static int select_appointment_id;
  public static void user_choice(Connection conn, List<Integer> appointmentList) {
    if (appointmentList.isEmpty()) {
      System.out.println("No appointment");
      return;
    }
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("-----------------------------");
      System.out.println("Pet owner, Choose an option: \n1. Type appointment id to see the appointment detail. " +
              "\n2. Type \'exit\' to exit appointment interface.");
      System.out.println("-----------------------------");

      int number = 0;
      String pet_owner_choice;
      while (!isValidInput) {
        pet_owner_choice = scanner.nextLine();
        if (pet_owner_choice.equalsIgnoreCase("exit")) {
          System.out.println("-----------------------------");
          System.out.println("Exiting Appointment interface.");
          return;
        }
        try {
          number = Integer.parseInt(pet_owner_choice);
          while (!appointmentList.contains(number)) {
            System.out.println("Appointment " + pet_owner_choice + " dose not exits, retype: ");
            try {
              pet_owner_choice = scanner.nextLine();
              number = Integer.parseInt(pet_owner_choice);
              if (pet_owner_choice.equalsIgnoreCase("exit")) {
                System.out.println("-----------------------------");
                return;
              }
            } catch (NumberFormatException e) {
              System.out.println("Error: Not a valid integer. Please try again.");
            }
          }
          select_appointment_id = number;
          appointmentChoice(conn);
          isValidInput = true;
          return;
        } catch (NumberFormatException e) {
          System.out.println("Error: Not a valid integer. Please try again.");
        }
      }
    }
  }


  public static void appointmentChoice(Connection conn) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Pet owner, Choose an option: \n1. View the treatments and medications" +
              "\n2. View the invoice \n3. Pay the invoice \n4. Exit");
      System.out.println("-----------------------------");
      String pet_owner_choice = scanner.nextLine();

      switch (pet_owner_choice) {
        case "1":
          System.out.println("-----------------------------");
          displayTreatmentsAndMedicationsForAppointment(conn, select_appointment_id);
          System.out.println("-----------------------------");
          break;
        case "2":
          System.out.println("-----------------------------");
          displayInvoicesForAppointment(conn, select_appointment_id);
          System.out.println("-----------------------------");
          break;
        case "3":
          System.out.println("-----------------------------");
          System.out.print("Enter the additional payment amount: ");
          double additionalAmount = Double.parseDouble(scanner.nextLine());
          updatePaymentReceived(conn, select_appointment_id, additionalAmount);
          break;
        case "4":
          return;
        default:
          System.out.println("-----------------------------");
          System.out.println("Invalid input. Please try again.");
          break;
      }
    }
  }

  private static void displayTreatmentsAndMedicationsForAppointment(Connection conn, int appointmentId) {
    try (CallableStatement stmt = conn.prepareCall("{CALL display_treatments_and_medications_for_appointment(?)}")) {

      stmt.setInt(1, appointmentId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String name = rs.getString("name");
        String description = rs.getString("description");
        double price = rs.getDouble("price");
        String type = rs.getString("type");

        System.out.println("Name: " + name + ", " + "Description: " + description + ", "
                + "Price: " + price + ", " + "Type: " + type);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  private static void displayInvoicesForAppointment(Connection conn, int appointmentId) {
    try (CallableStatement stmt = conn.prepareCall("{CALL display_invoices_for_appointment(?)}")) {

      stmt.setInt(1, appointmentId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Date issuedDate = rs.getDate("issued_date");
        double totalAmount = rs.getDouble("total_amount_per_visit");
        double paymentReceivedAmount = rs.getDouble("pmt_received_amt");
        Date paymentReceivedDate = rs.getDate("pmt_received_date");

        System.out.println("Issued Date: " + issuedDate);
        System.out.println("Total Amount: " + totalAmount);
        System.out.println("Payment Received Amount: " + paymentReceivedAmount);
        System.out.println("Payment Received Date: " + paymentReceivedDate);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void updatePaymentReceived(Connection conn, int appointmentId, double additionalAmount) {
    try (CallableStatement stmt = conn.prepareCall("{CALL update_payment_received(?, ?)}")) {
      stmt.setInt(1, appointmentId);
      stmt.setDouble(2, additionalAmount);
      int affectedRows = stmt.executeUpdate();
      if (affectedRows > 0) {
        System.out.println("Payment updated successfully.");
      } else {
        System.out.println("Payment update failed. Appointment ID may not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
