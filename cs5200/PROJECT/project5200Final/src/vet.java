import com.sun.jdi.IntegerValue;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class vet {

  private String vetId;
  private Connection conn;
  private Integer appointId;
  private Scanner scan;
  private boolean quit;
  private List<Integer> appointList = new ArrayList<>();
  private List<Integer> availableT = new ArrayList<>();
  private List<Integer> addedT = new ArrayList<>();
  private List<String> medList = new ArrayList<>();
  public vet (Connection conn, String vetId) {
    this.conn = conn;
    this.vetId = vetId;
    this.quit = false;
    this.scan = new Scanner(System.in);
    this.getAppointList();
  }

  private void getAppointList() {
    String q = "SELECT appointment_id FROM appointment WHERE vet_id = ?";
    try {
      PreparedStatement ps= this.conn.prepareStatement(q);

      ps.setString(1, vetId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Integer id = rs.getInt("appointment_id");
        this.appointList.add(id);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Could not get appointment list now.");
    }
  }

  public void user_choice() {
    while (true) {
      System.out.println("Welcome back vet " + this.vetId);
      System.out.println("-----------------------------");
      validAppointOrQuit();
      if (this.quit) {
        return;
      }
      vetChoices();
    }
  }

  /** Prompt vet for the appoint id input and validate it. Print an error message to
   * standard output and re-prompt the user for input, if the user provides
   * invalid input.
   */
  private void validAppointOrQuit() {
    System.out.println("Please select one of your " +
            "appointment with id number to start with or press Q to return to login page.");
    try {
      displayAppointment();
    } catch (Exception e) {
      e.printStackTrace();
    }

    while (true) {
      String input = scan.next();

      // Check for 'Q' or 'q' to return
      if (input.equalsIgnoreCase("Q")) {
        System.out.println("Returning to the login page.");
        this.quit = true;
        break;
      }

      try {
        // Try to parse the input as an integer
        int inputId = Integer.parseInt(input);

        // Check if the input ID is in the list
        if (!this.appointList.isEmpty() && this.appointList.contains(inputId)) {
          System.out.println("Appointment selected: " + inputId);
          this.appointId = inputId;
          break;
        } else {
          System.out.println("Invalid appointment, please retry");
        }
      } catch (Exception e) {
        System.out.println("Invalid input, please enter a number or 'Q' to quit.");
      }
    }
  }

  private void vetChoices() {
    boolean done = false;
    while (!done) {
      System.out.println("For the chosen appoint choose one of the following options: \n" +
              "1. Add a treatment " +
              "\n2. Del a treatment \n3. Add a medication\n4. Choose another appointment");
      System.out.println("-----------------------------");
      String vet_choice = scan.next();
      switch (vet_choice) {
        case "1":
          System.out.println("-----------------------------");
          System.out.println("Add a treatment");
          addTreatment();
          System.out.println("-----------------------------");
          this.availableT = new ArrayList<>();
          break;
        case "2":
          System.out.println("-----------------------------");
          System.out.println("Del a treatment");
          delTreatment();
          System.out.println("-----------------------------");
          this.addedT = new ArrayList<>();
          break;
        case "3":
          System.out.println("-----------------------------");
          System.out.println("Add a medication");
          addMedication();
          System.out.println("-----------------------------");
          break;
        case "4":
          System.out.println("-----------------------------");
          System.out.println("Exiting modifying current appointment.");
          System.out.println("-----------------------------");
          done = true;
          break;
        default:
          System.out.println("-----------------------------");
          System.out.println("Invalid input. Please try again.");
          break;
      }
    }
  }

  private void addTreatment() {
    try {
      displayAvailableTreatment();
      System.out.print("Enter the treatment id number: ");
      Integer id = Integer.valueOf(scan.next());
      System.out.print("Enter the charge price in decimal number for this service: ");
      Float charge = scan.nextFloat();
      System.out.println("-----------------------------");
      if (!this.availableT.isEmpty() && this.availableT.contains(id)) {
        CallableStatement stmt = conn.prepareCall("{CALL add_treatment(?, ?, ?)}");
        stmt.setInt(1, this.appointId);
        stmt.setInt(2, id);
        stmt.setFloat(3, charge);
        stmt.executeQuery();
        System.out.println("Treatment successfully added.");
      } else {
        System.out.println("Invalid treatment selected.");
      }
    } catch (Exception e) {
      System.out.println("Invalid input. Return to the menu.");
    }
  }

  private void delTreatment() {
    try {
      displayTForAppoint();
      System.out.print("Enter the treatment id number: ");
      Integer id = scan.nextInt();
      System.out.println("-----------------------------");
      if (!this.addedT.isEmpty() && this.addedT.contains(id)) {
        CallableStatement stmt = conn.prepareCall("{CALL remove_treatment(?, ?)}");
        stmt.setInt(1, this.appointId);
        stmt.setInt(2, id);
        stmt.executeQuery();
        System.out.println("Treatment successfully deleted.");
      } else {
        System.out.println("Invalid treatment selected.");
      }
    } catch (Exception e) {
      System.out.println("Invalid input. Return to the menu.");
    }
  }

  private void addMedication() {
    try {
      System.out.println("Available medication list: ");
      displayMedicationList();
      System.out.println("Enter the medication name: ");
      String name = scan.next();
      System.out.println("Enter the medication quantity number: ");
      Integer quant = Integer.valueOf(scan.next());
      System.out.println("Enter the time interval of the medication: ");
      Integer interval = Integer.valueOf(scan.next());
      System.out.println("-----------------------------");
      if (!this.medList.isEmpty() && this.medList.contains(name)) {
        CallableStatement stmt = conn.prepareCall("{CALL add_medication(?, ?, ?, ?)}");
        stmt.setInt(1, this.appointId);
        stmt.setString(2, name);
        stmt.setInt(3, quant);
        stmt.setInt(4, interval);
        stmt.executeQuery();
        System.out.println("Medication successfully added.");
      } else {
        System.out.println("Invalid medication name entered.");
      }
    } catch (Exception e) {
      System.out.println("Invalid input. Return to the menu.");
    }
  }


  private void displayAvailableTreatment() throws SQLException {
    System.out.println("Available treatments to be added are: ");
    CallableStatement stmt = conn.prepareCall("{CALL clinic_has_treatments(?, ?)}");
    stmt.setString(1, vetId);
    stmt.setInt(2, appointId);
    ResultSet resultSet = stmt.executeQuery();
    // Print the list of available treatments in this clinic
    while (resultSet.next()) {
      int service = resultSet.getInt("service_id");
      String name = resultSet.getString("treatment_name");
      String description = resultSet.getString("treatment_description");
      double regular = resultSet.getDouble("regular_price");
      this.availableT.add(service);
      System.out.println("Service ID: " + service + ", Treatment: " + name
              + ", Description: " + description + ", Default price: " + regular);
    }
  }


  private void displayTForAppoint() throws SQLException {
    System.out.println("Added treatments for this appointment are: ");
    CallableStatement stmt = conn.prepareCall("{CALL appointment_treatments(?)}");
    stmt.setInt(1, appointId);
    ResultSet resultSet = stmt.executeQuery();
    // Print the list of available treatments in this clinic
    while (resultSet.next()) {
      int service = resultSet.getInt("service_id");
      String name = resultSet.getString("treatment_name");
      String description = resultSet.getString("treatment_description");
      double charge = resultSet.getDouble("charge");
      this.addedT.add(service);
      System.out.println("Treatment ID: " + service + ", Treatment: " + name
              + ", Description: " + description + ", Actual charge: " + charge);
    }
  }

  private void displayMedicationList() throws SQLException {
    CallableStatement stmt = conn.prepareCall("{CALL display_medication(?)}");
    stmt.setInt(1, appointId);
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
      String med = rs.getString("medication_name");
      System.out.println(med);
      this.medList.add(med.toLowerCase());
    }
  }

  private void displayAppointment() throws SQLException {
    CallableStatement stmt = conn.prepareCall("{CALL display_appointment(?)}");
    stmt.setString(1, vetId);
    ResultSet resultSet = stmt.executeQuery();

    // Print the list of vet's appointments
    while (resultSet.next()) {
      int appointmentId = resultSet.getInt("appointment");
      String datetime = resultSet.getString("meeting_time");
      String description = resultSet.getString("basic_description");
      String pet = resultSet.getString("pet");
      String owner = resultSet.getString("family");

      System.out.println("Appointment ID: " + appointmentId +
              ", Datetime: " + datetime + ", Description: " + description + ", Pet: " + pet +
              ", Owner: " + owner);
      System.out.println("-----------------------------");
    }
  }
}
