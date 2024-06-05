import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class PetOwnerGUI extends JFrame {
  private JButton viewPetInfoButton;
  private JButton viewAppointmentButton;
  private JButton resetPasswordButton;
  private JButton signOutButton;
  private Connection conn;
  private String ownerId;
  private List<Integer> appointmentList = new ArrayList<>();

  public PetOwnerGUI(Connection conn, String ownerId) {
    this.conn = conn;
    this.ownerId = ownerId;

    setTitle("Pet Owner Menu");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    initializeComponents();
    setupListeners();
    setVisible(true);
  }

  private void initializeComponents() {
    this.setLayout(new GridLayout(3, 1));

    viewPetInfoButton = new JButton("View Pet Information");
    viewAppointmentButton = new JButton("View Appointment Information");
    resetPasswordButton = new JButton("Reset your password");
    signOutButton = new JButton("Sign Out");

    add(viewPetInfoButton);
    add(viewAppointmentButton);
    add(resetPasswordButton);
    add(signOutButton);
  }

  private void setupListeners() {
    viewPetInfoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Implement logic to display pet information
        displayPetInfo();
      }
    });

    viewAppointmentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        displayAppointmentsForOwner();
      }
    });

    resetPasswordButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        resetPassword();
      }
    });

    signOutButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
  }

  private void displayPetInfo() {
    try (CallableStatement stmt = conn.prepareCall("{CALL display_pet_info(?)}")) {

      stmt.setString(1, ownerId);
      ResultSet rs = stmt.executeQuery();

      StringBuilder petInfo = new StringBuilder();

      while (rs.next()) {
        int patientId = rs.getInt("patient_id");
        String petName = rs.getString("pet_name");
        String breedName = rs.getString("breed_name");
        Float height_in_cm = rs.getFloat("height_in_cm");
        Float weight_in_lb = rs.getFloat("weight_in_lb");
        String pet_status = rs.getString("pet_status");

        petInfo.append("Patient ID: ").append(patientId).append("\n");
        petInfo.append("Pet Name: ").append(petName).append("\n");
        petInfo.append("Breed Name: ").append(breedName).append("\n");
        petInfo.append("Height: ").append(height_in_cm).append(" cm\n");
        petInfo.append("Weight: ").append(weight_in_lb).append(" lb\n");
        petInfo.append("Pet Status: ").append(pet_status).append("\n\n");
      }

      if (petInfo.length() > 0) {
        JOptionPane.showMessageDialog(null, petInfo.toString(),
                "Pet Information", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(null,
                "No pet information found.", "Pet Information",
                JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(),
              "Error", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void displayAppointmentsForOwner() {
    JFrame frame = new JFrame("My Appointments");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 800);

    JPanel mainPanel = new JPanel(new GridLayout(0, 1));

    try {
      CallableStatement stmt = conn.prepareCall("{CALL display_appointments_for_owner(?)}");
      stmt.setString(1, ownerId);
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

        JPanel appointmentPanel = new JPanel();
        appointmentPanel.setLayout(new BoxLayout(appointmentPanel, BoxLayout.Y_AXIS));

        String aInfo = "Appointment ID: " + appointmentId + ", " +
                "DateTime: " + appointmentDateTime + ", " +
                "Description: " + description + ", " +
                "Patient ID: " + patientId + ", " +
                "Pet Name: " + petName + ", " +
                "Clinic ID: " + clinicId + ", " +
                "Show Up: " + show_up_status + ", " +
                "Vet ID: " + vet_id + ", " +
                "Vet Name: " + vet_name + ", " +
                "Room ID: " + room_id;
        appointmentPanel.add(new JLabel(aInfo));

        // Create buttons for viewing invoices, treatments, and paying invoices for each appointment
        JButton viewInvoiceButton = new JButton("View Invoice");
        JButton viewTreatmentsButton = new JButton("View Treatments");
        JButton payInvoiceButton = new JButton("Pay Invoice");

        // Add action listeners to the buttons for each appointment
        viewInvoiceButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // Implement logic to view invoices for this appointment
            displayInvoices(appointmentId);
          }
        });

        viewTreatmentsButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // Implement logic to view treatments for this appointment
            displayTreatmentsAndMedications(appointmentId);
          }
        });

        payInvoiceButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // Implement logic to pay invoices for this appointment
            payInvoiceForAppointment(appointmentId);
          }
        });

        // Add buttons to the appointment panel for each appointment
        appointmentPanel.add(viewInvoiceButton);
        appointmentPanel.add(viewTreatmentsButton);
        appointmentPanel.add(payInvoiceButton);

        // Add the appointment panel to the main panel holding all appointments
        mainPanel.add(appointmentPanel);
      }

      if (appointmentList.isEmpty()) {
        JOptionPane.showMessageDialog(null,
                "No appointments found.", "Appointments",
                JOptionPane.INFORMATION_MESSAGE);
      } else {
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.add(scrollPane);
        frame.setVisible(true);
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "SQL Error", "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  private void payInvoiceForAppointment(int id) {
    String input = JOptionPane.showInputDialog("Enter the additional payment amount:");
    if (input == null) {
      // User clicked Cancel or closed the dialog
      return;
    }

    try {
      double additionalAmount = Double.parseDouble(input);
      CallableStatement stmt = conn.prepareCall("{CALL update_payment_received(?, ?)}");
      stmt.setInt(1, id);
      stmt.setDouble(2, additionalAmount);
      int affectedRows = stmt.executeUpdate();
      if (affectedRows > 0) {
        JOptionPane.showMessageDialog(null,
                "Payment updated successfully.", "Payment Update",
                JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(null,
                "Payment update failed. Appointment ID may not exist.",
                "Payment Update", JOptionPane.ERROR_MESSAGE);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "SQL Error", "Error",
              JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null,
              "Invalid input. Please enter a valid numeric value.",
              "Payment Update", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void displayTreatmentsAndMedications(int id) {
    try {
      CallableStatement stmt = conn.prepareCall(
              "{CALL display_treatments_and_medications_for_appointment(?)}");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      StringBuilder treatmentInfo = new StringBuilder();

      while (rs.next()) {
        String name = rs.getString("name");
        String description = rs.getString("description");
        double price = rs.getDouble("price");
        String type = rs.getString("type");

        // Append treatment information to the StringBuilder
        treatmentInfo.append("Name: ").append(name).append("\n");
        treatmentInfo.append("Description: ").append(description).append("\n");
        treatmentInfo.append("Price: ").append(price).append("\n");
        treatmentInfo.append("Type: ").append(type).append("\n\n");
      }

      if (treatmentInfo.length() > 0) {
        JTextArea textArea = new JTextArea(treatmentInfo.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane,
                "Treatments and Medications", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(null,
                "No treatments and medications found for this appointment.",
                "Treatments and Medications", JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "SQL Error", "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  private void resetPassword() {
    try (CallableStatement stmt = conn.prepareCall("{CALL update_owner_password(?, ?)}")) {
      String newPassword = JOptionPane.showInputDialog("Enter the additional payment amount:");
      stmt.setString(1, ownerId);
      stmt.setString(2, newPassword);
      int affectedRows = stmt.executeUpdate();
      if (affectedRows > 0) {
        System.out.println("Password updated successfully.");
      } else {
        System.out.println("Password update failed. Owner ID may not exist.");
      }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "SQL Error, cannot reset password.", "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }
  private void displayInvoices(int id) {
    try {
      CallableStatement stmt = conn.prepareCall("{CALL display_invoices_for_appointment(?)}");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      StringBuilder invoiceInfo = new StringBuilder();

      while (rs.next()) {
        Date issuedDate = rs.getDate("issued_date");
        double totalAmount = rs.getDouble("total_amount_per_visit");
        double paymentReceivedAmount = rs.getDouble("pmt_received_amt");
        Date paymentReceivedDate = rs.getDate("pmt_received_date");

        // Append invoice information to the StringBuilder
        invoiceInfo.append("Issued Date: ").append(issuedDate).append("\n");
        invoiceInfo.append("Total Amount: ").append(totalAmount).append("\n");
        invoiceInfo.append("Payment Received Amount: ").append(paymentReceivedAmount).append("\n");
        invoiceInfo.append("Payment Received Date: ").append(paymentReceivedDate).append("\n\n");
      }

      if (invoiceInfo.length() > 0) {
        JOptionPane.showMessageDialog(null, invoiceInfo.toString(),
                "Invoices", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(null,
                "No invoices found for this appointment.", "Invoices",
                JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null,
              "SQL Error.", "Error",
              JOptionPane.INFORMATION_MESSAGE);
    }
  }
}