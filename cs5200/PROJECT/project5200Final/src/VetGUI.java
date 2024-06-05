import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VetGUI {
  private JFrame frame;
  private JLabel welcomeLabel;
  private JButton selectAppointmentButton;
  private JButton ATreatments;
  private JButton addButton;
  private JButton DTreatments;
  private JButton delButton;
  private JButton AMed;
  private JButton medicationButton;
  private JButton complete;
  private JTextArea appointmentInfoTextArea;
  private JTextArea treatmentTextArea;
  private JScrollPane scrollPane;
  private Connection conn;
  private String vetId;
  private List<String[]> appointments = new ArrayList<>();
  private int selectedAppointmentId = -1;
  private List<Integer> availableT = new ArrayList<>();
  private List<Integer> doneT = new ArrayList<>();
  private List<String> medList = new ArrayList<>();

  public VetGUI(Connection conn, String vetId) {
    this.conn = conn;
    this.vetId = vetId;
    listAppointment();

    frame = new JFrame("Vet Application");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 800);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    frame.add(mainPanel);

    welcomeLabel = new JLabel("Welcome back vet");
    selectAppointmentButton = new JButton("Select Appointment");
    treatmentTextArea = new JTextArea(1200, 500);
    treatmentTextArea.setEditable(true);

    scrollPane = new JScrollPane(treatmentTextArea);
    ATreatments = new JButton("Available treatments");
    addButton = new JButton("Add a treatment");
    DTreatments = new JButton("Done treatments");
    delButton = new JButton("Delete a treatment");
    AMed = new JButton("Available Med");
    medicationButton = new JButton("Add Medication");
    complete = new JButton("Complete the appointment");
    appointmentInfoTextArea = new JTextArea(10, 30);
    appointmentInfoTextArea.setEditable(false);

    mainPanel.add(welcomeLabel);
    mainPanel.add(selectAppointmentButton);
    mainPanel.add(appointmentInfoTextArea);
    mainPanel.add(scrollPane);
    mainPanel.add(ATreatments);
    mainPanel.add(addButton);
    mainPanel.add(DTreatments);
    mainPanel.add(delButton);
    mainPanel.add(AMed);
    mainPanel.add(medicationButton);
    mainPanel.add(complete);
    frame.setVisible(true);

    selectAppointmentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectAppointment();
      }
    });

    ATreatments.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (selectedAppointmentId != -1) {
          displayAvailableT();
          availableT = new ArrayList<>();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select an appointment first.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (selectedAppointmentId != -1) {
          addTreatment();
          availableT = new ArrayList<>();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select an appointment first.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    DTreatments.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (selectedAppointmentId != -1) {
          displayDoneTreats();
          doneT = new ArrayList<>();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select an appointment first.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    delButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (selectedAppointmentId != -1) {
          delTreatment();
          doneT = new ArrayList<>();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select an appointment first.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    AMed.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (selectedAppointmentId != -1) {
          displayAvailableM();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select an appointment first.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    medicationButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (selectedAppointmentId != -1) {
          addMedication();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select an appointment first.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    complete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (selectedAppointmentId != -1) {
          completeAppoint();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select an appointment first.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
  }

  private void completeAppoint() {
    try {
      CallableStatement stmt = conn.prepareCall("{CALL complete_appoint(?)}");
      stmt.setInt(1, selectedAppointmentId);
      ResultSet resultSet = stmt.executeQuery();
      JOptionPane.showMessageDialog(frame, "Appointment " + selectedAppointmentId
              + "Successfully completed.", "Success", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
      JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frame, "Invalid input(s)." + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    }
  }


  private void addTreatment() {
    try {
      if (!displayAvailableT()) {
        JOptionPane.showMessageDialog(frame, "No treatments available for this appointment",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Create a dialog to get user input
      JPanel inputPanel = new JPanel(new GridLayout(2, 2));
      JTextField treatmentIdField = new JTextField();
      JTextField chargeField = new JTextField();

      inputPanel.add(new JLabel("Enter the treatment id number:"));
      inputPanel.add(treatmentIdField);
      inputPanel.add(new JLabel("Enter the charge price in decimal number for this service:"));
      inputPanel.add(chargeField);

      int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Add Treatment",
              JOptionPane.OK_CANCEL_OPTION);

      if (result == JOptionPane.OK_OPTION) {
        // Get user input
        int id = Integer.parseInt(treatmentIdField.getText());
        double charge = Double.parseDouble(chargeField.getText());

        // Validate user input
        if (!this.availableT.isEmpty() && this.availableT.contains(id) && (charge > 0)) {
          CallableStatement stmt = conn.prepareCall("{CALL add_treatment(?, ?, ?)}");
          stmt.setInt(1, this.selectedAppointmentId);
          stmt.setInt(2, id);
          stmt.setDouble(3, charge);
          stmt.executeQuery();
          JOptionPane.showMessageDialog(frame, "Treatment successfully added.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(frame, "Invalid treatment selected or " +
                  "invalid charge amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frame, "Invalid input(s)." + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private boolean displayAvailableT() {
    try {
      CallableStatement stmt = conn.prepareCall("{CALL clinic_has_treatments(?, ?)}");
      stmt.setString(1, vetId);
      stmt.setInt(2, selectedAppointmentId);
      ResultSet resultSet = stmt.executeQuery();

      StringBuilder treatmentInfo = new StringBuilder();

      while (resultSet.next()) {
        int serviceId = resultSet.getInt("service_id");
        String name = resultSet.getString("treatment_name");
        String description = resultSet.getString("treatment_description");
        double regularPrice = resultSet.getDouble("regular_price");

        treatmentInfo.append("Service ID: ").append(serviceId).append("\n");
        treatmentInfo.append("Treatment: ").append(name).append("\n");
        treatmentInfo.append("Description: ").append(description).append("\n");
        treatmentInfo.append("Default price: ").append(regularPrice).append("\n\n");
        this.availableT.add(serviceId);
      }

      if (treatmentInfo.length() > 0) {
        treatmentTextArea.setText(treatmentInfo.toString());
        scrollPane.getVerticalScrollBar().setValue(0);
        return true;
      } else {
        JOptionPane.showMessageDialog(null,
                "No available treatments for this appointment found.",
                "Available Treatments",
                JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null,
              "SQL error: " + e.getMessage(), "Error",
              JOptionPane.INFORMATION_MESSAGE);
    }
    return false;
  }

  private void delTreatment() {
    try {
      if (!displayDoneTreats()) {
        JOptionPane.showMessageDialog(frame, "No treatments done for this appointment yet",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      // Create a dialog to get user input
      JPanel inputPanel = new JPanel(new GridLayout(1, 2));
      JTextField treatmentIdField = new JTextField();

      inputPanel.add(new JLabel("Enter a treatment id number to delete:"));
      inputPanel.add(treatmentIdField);

      int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Delete Treatment",
              JOptionPane.OK_CANCEL_OPTION);

      if (result == JOptionPane.OK_OPTION) {
        // Get user input
        int id = Integer.parseInt(treatmentIdField.getText());

        // Validate user input
        if (!this.doneT.isEmpty() && this.doneT.contains(id)) {
          CallableStatement stmt = conn.prepareCall("{CALL remove_treatment(?, ?)}");
          stmt.setInt(1, this.selectedAppointmentId);
          stmt.setInt(2, id);
          stmt.executeQuery();
          JOptionPane.showMessageDialog(frame, "Treatment successfully deleted.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(frame, "Invalid treatment selected.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frame, "Invalid input(s).",
              "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private boolean displayDoneTreats() {
    try {
      CallableStatement stmt = conn.prepareCall("{CALL appointment_treatments(?)}");
      stmt.setInt(1, selectedAppointmentId);
      ResultSet resultSet = stmt.executeQuery();
      StringBuilder treatments = new StringBuilder();

      while (resultSet.next()) {
        int service = resultSet.getInt("service_id");
        String name = resultSet.getString("treatment_name");
        String description = resultSet.getString("treatment_description");
        double charge = resultSet.getDouble("charge");
        this.doneT.add(service);

        treatments.append("Service ID: ").append(service).append("\n");
        treatments.append("Treatment: ").append(name).append("\n");
        treatments.append("Description: ").append(description).append("\n");
        treatments.append("Actual charge: $").append(charge).append("\n\n");
      }
      if (treatments.length() > 0) {
        treatmentTextArea.setText(treatments.toString());
        scrollPane.getVerticalScrollBar().setValue(0);
        return true;
      } else {
        JOptionPane.showMessageDialog(null,
                "No added treatments for this appointment found.",
                "Done Treatments",
                JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null,
              "SQL error: " + e.getMessage(), "Error",
              JOptionPane.INFORMATION_MESSAGE);
    }
    return false;
  }

  private void addMedication() {
    try {
      if (!displayAvailableM()) {
        JOptionPane.showMessageDialog(frame, "No medications available",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Create a dialog to get user input
      JPanel inputPanel = new JPanel(new GridLayout(3, 2));
      JTextField medicationNameField = new JTextField();
      JTextField quantityField = new JTextField();
      JTextField intervalField = new JTextField();

      inputPanel.add(new JLabel("Enter the medication name:"));
      inputPanel.add(medicationNameField);
      inputPanel.add(new JLabel("Enter the medication quantity number:"));
      inputPanel.add(quantityField);
      inputPanel.add(new JLabel("Enter the time interval of the medication:"));
      inputPanel.add(intervalField);

      int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Add Medication",
              JOptionPane.OK_CANCEL_OPTION);

      if (result == JOptionPane.OK_OPTION) {
        // Get user input
        String name = medicationNameField.getText();
        int quant = Integer.parseInt(quantityField.getText());
        int interval = Integer.parseInt(intervalField.getText());

        // Validate user input
        if (!this.medList.isEmpty() && this.medList.contains(name)
                && (quant > 0) && (interval > 0)) {
          CallableStatement stmt = conn.prepareCall("{CALL add_medication(?, ?, ?, ?)}");
          stmt.setInt(1, this.selectedAppointmentId);
          stmt.setString(2, name);
          stmt.setInt(3, quant);
          stmt.setInt(4, interval);
          stmt.executeQuery();
          JOptionPane.showMessageDialog(frame, "Medication successfully added.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(frame, "Invalid medication name or " +
                  "invalid quantity/interval entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frame, "Error: Invalid input(s)" + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    }
  }



  private boolean displayAvailableM() {
    try {
      CallableStatement stmt = conn.prepareCall("{CALL display_medication(?)}");
      stmt.setInt(1, selectedAppointmentId);
      ResultSet rs = stmt.executeQuery();
      StringBuilder meds = new StringBuilder();

      while (rs.next()) {
        String med = rs.getString("medication_name");
        System.out.println(med);
        medList.add(med.toLowerCase());
        meds.append("Medication name: ").append(med).append("\n");
      }
      if (meds.length() > 0) {
        treatmentTextArea.setText(meds.toString());
        scrollPane.getVerticalScrollBar().setValue(0);
        return true;
      } else {
        JOptionPane.showMessageDialog(null,
                "No available medication for this appointment is found.",
                "Available Meds",
                JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null,
              "SQL error: " + e.getMessage(), "Error",
              JOptionPane.INFORMATION_MESSAGE);
    }
    return false;
  }

    private void selectAppointment() {
    if (appointments.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "No appointments found.",
              "Appointments", JOptionPane.INFORMATION_MESSAGE);
    } else {
      String[] options = new String[appointments.size()];

      for (int i = 0; i < appointments.size(); i++) {
        options[i] = "Appointment ID: " + appointments.get(i)[0] +
                ", Time: " + appointments.get(i)[1] +
                ", Description: " + appointments.get(i)[2] +
                ", Pet Name: " + appointments.get(i)[3] +
                ", Owner: " + appointments.get(i)[4];
      }
      JList<String> optionList = new JList<>(options);
      JScrollPane scrollPane = new JScrollPane(optionList);
      scrollPane.setPreferredSize(new Dimension(1200, 400));
      int result = JOptionPane.showConfirmDialog(frame,
              scrollPane, "Select Appointment",
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

      if (result == JOptionPane.OK_OPTION) {
        int selectedOption = optionList.getSelectedIndex();
        if (selectedOption >= 0) {
          selectedAppointmentId = Integer.parseInt(appointments.get(selectedOption)[0]);
          welcomeLabel.setText("Welcome back vet - " +
                  "Selected Appointment ID: " + selectedAppointmentId);
        }
      }
    }
  }


  private void listAppointment() {
    try {
      CallableStatement stmt = conn.prepareCall("{CALL display_appointment(?)}");
      stmt.setString(1, vetId);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        int appointmentId = resultSet.getInt("appointment");
        String datetime = resultSet.getString("meeting_time");
        String description = resultSet.getString("basic_description");
        String pet = resultSet.getString("pet");
        String owner = resultSet.getString("family");
        appointments.add(new String[]{String.valueOf(appointmentId), datetime,
                description, pet, owner});
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frame, "Error fetching appointments: "
              + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
