import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.IllegalFormatException;

public class EmpGUI {
  private JFrame frame;
  private JButton newAppointmentButton;
  private JButton cancelAppointmentButton;
  private JButton displayAppointmentsButton;
  private JButton appointmentDetailButton;
  private JButton showUpButton;
  private JButton exitButton;
  private JTextArea outputTextArea;
  private Connection conn;
  private String empId;

  public EmpGUI(Connection conn, String empId) {
    this.conn = conn;
    this.empId = empId;

    frame = new JFrame("Admin Employee Application");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 400);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    newAppointmentButton = new JButton("New Appointment");
    cancelAppointmentButton = new JButton("Cancel Appointment");
    displayAppointmentsButton = new JButton("Display Appointments");
    appointmentDetailButton = new JButton("Appointment Detail");
    showUpButton = new JButton("showUp");
    exitButton = new JButton("Exit");
    outputTextArea = new JTextArea(10, 30);
    outputTextArea.setEditable(false);

    mainPanel.add(newAppointmentButton);
    mainPanel.add(cancelAppointmentButton);
    mainPanel.add(displayAppointmentsButton);
    mainPanel.add(appointmentDetailButton);
    mainPanel.add(exitButton);
    mainPanel.add(showUpButton);
    mainPanel.add(new JScrollPane(outputTextArea));

    newAppointmentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showMakeAppointmentDialog();
      }
    });

    cancelAppointmentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cancelAppointment();
      }
    });

    displayAppointmentsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        displayAppoint();
      }
    });

    appointmentDetailButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        displayDetail();
      }
    });

    exitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });

    showUpButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {appoint_show_up();}
    });

    frame.add(mainPanel);
    frame.setVisible(true);
  }


  private void appoint_show_up() {

    JPanel inputPanel = new JPanel(new GridLayout(1, 1));
    JTextField idField = new JTextField();

    inputPanel.add(new JLabel("show up appointment ID:"));
    inputPanel.add(idField);

    int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Appointment Detail",
            JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
      try {
        int id = Integer.parseInt(idField.getText());
        String qry = "{CALL appoint_show_up(?)}";

        try {
          CallableStatement stmt = conn.prepareCall(qry);
          stmt.setInt(1, id); // Use the selected appointment ID
          ResultSet rsl = stmt.executeQuery();
          JOptionPane.showMessageDialog(null,
                  "Done", "Pet shows up",
                  JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
          JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                  JOptionPane.ERROR_MESSAGE);
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Invalid Input", "Error",
                JOptionPane.ERROR_MESSAGE);
      }
    }
  }


  private void displayAppoint() {
    String qry = "{CALL display_appointment_for_employee (?)}";
    try {
      CallableStatement stmt = conn.prepareCall(qry);
      stmt.setString(1, empId);
      ResultSet rsl = stmt.executeQuery();
      StringBuilder appointmentList = new StringBuilder("All appointments are listed: \n");
      while (rsl.next()) {
        String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rsl.getTimestamp(2));
        appointmentList.append("Appointment ID: ").append(rsl.getInt(1)).append("\n")
                .append("Time: ").append(s).append("\n")
                .append("PetID: ").append(rsl.getString(3)).append("\n")
                .append("Pet: ").append(rsl.getString(4)).append("\n")
                .append("Family: ").append(rsl.getString(5)).append("\n")
                .append("Show-up: ").append(rsl.getInt(6)).append("\n\n");
      }
      outputTextArea.setText(appointmentList.toString());
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "SQL Error", "Error",
              JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
      outputTextArea.setText("No appointment to display.");
    }
  }

  private void displayDetail() {
    // Create a dialog to get user input
    JPanel inputPanel = new JPanel(new GridLayout(1, 2));
    JTextField idField = new JTextField();

    inputPanel.add(new JLabel("Enter the appointment id number:"));
    inputPanel.add(idField);

    int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Appointment Detail",
            JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
      try {
        int id = Integer.parseInt(idField.getText());
        StringBuilder appointmentDetail = new StringBuilder();

        String qry = "{CALL display_appointment_detail(?, ?)}";
        try {
          CallableStatement stmt = conn.prepareCall(qry);
          stmt.setString(1, empId);
          stmt.setInt(2, id); // Use the selected appointment ID
          ResultSet rsl = stmt.executeQuery();

          appointmentDetail.append("Appointment Detail:\n");
          while (rsl.next()) {
            String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format
                    (rsl.getTimestamp(2));
            String s1 = "NULL";
            if (rsl.getDate(10) != null) {
              s1 = new SimpleDateFormat("MM/dd/yyyy").format
                      (rsl.getDate(10));
            }
            appointmentDetail.append("Appointment ID: ").append(rsl.getInt(1)).append("\n")
                    .append("Time: ").append(s).append("\n")
                    .append("Pet: ").append(rsl.getString(3)).append("\n")
                    .append("Family: ").append(rsl.getString(4)).append("\n")
                    .append("Appointment Description: ").append(rsl.getString(5)).append("\n")
                    .append("Veterinarian: ").append(rsl.getString(6)).append("\n")
                    .append("Room ID: ").append(rsl.getInt(7)).append("\n")
                    .append("Expenditure: $").append(String.format("%.2f",
                            rsl.getDouble(8))).append("\n")
                    .append("Received: $").append(String.format("%.2f",
                            rsl.getDouble(9))).append("\n")
                    .append("Received Date: ").append(s1).append("\n");
          }
          outputTextArea.setText(appointmentDetail.toString());
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                  JOptionPane.ERROR_MESSAGE);
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Invalid Input", "Error",
                JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void showMakeAppointmentDialog() {

    JDialog dialog = new JDialog(frame, "New Appointment", true);
    dialog.setSize(300, 200);
    dialog.setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new GridLayout(4, 2));

    JTextField petIdField = new JTextField();
    JTextField descriptionField = new JTextField();

    inputPanel.add(new JLabel("Appointment Time (yyyy-mm-dd HH:mm:ss):"));
    inputPanel.add(new JTextField());
    inputPanel.add(new JLabel("Pet ID:"));
    inputPanel.add(petIdField);
    inputPanel.add(new JLabel("Description of Request:"));
    inputPanel.add(descriptionField);

    int result = JOptionPane.showConfirmDialog(dialog, inputPanel, "New Appointment",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    try {
      String appointmentTimeText = ((JTextField) inputPanel.getComponent(1)).getText();
      Timestamp appointmentTime = this.readTimeStamp(appointmentTimeText);

      // check if the schedule is full
      if (result == JOptionPane.OK_OPTION && isBookFull(appointmentTime) == 1) {
        JOptionPane.showMessageDialog(frame, "Sorry this time is FULL. " +
                "Please re-schedule your time", " ", JOptionPane.INFORMATION_MESSAGE);
        result = JOptionPane.showConfirmDialog(dialog, inputPanel, "New Appointment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        appointmentTimeText = ((JTextField) inputPanel.getComponent(1)).getText();
        appointmentTime = this.readTimeStamp(appointmentTimeText);
      }
      // successful schedule
      if (result == JOptionPane.OK_OPTION && isBookFull(appointmentTime) == 0) {
        String petIdText = petIdField.getText();
        String description = descriptionField.getText();
        makeAppointment(appointmentTime, Integer.parseInt(petIdText), description);
        JOptionPane.showMessageDialog(frame, "Appointment successfully created.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Invalid Input", "Error",
              JOptionPane.ERROR_MESSAGE);
    }
    dialog.dispose(); // Close the dialog
  }

  private int isBookFull(Timestamp time) {
    int val = 0;
    String qry = "{? = CALL is_book_full(?,?)}";
    try {
      CallableStatement stmt = conn.prepareCall(qry);
      stmt.registerOutParameter(1, Types.INTEGER);
      stmt.setTimestamp(2, time);
      stmt.setString(3, empId);
      stmt.execute();
      val = stmt.getInt(1);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return val;
  }


  public void makeAppointment(Timestamp ts, int petID, String description_of_request) {
    String qry = "{CALL make_appointment (?,?,?,?)}";
    try {
      CallableStatement stmt = conn.prepareCall(qry);
      stmt.setString(1, empId);
      stmt.setTimestamp(2, ts);
      stmt.setInt(3, petID);
      stmt.setString(4, description_of_request);
      ResultSet rsl = stmt.executeQuery();
      StringBuilder bs = new StringBuilder();
      while (rsl.next()) {
        String tss = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rsl.getTimestamp(1));
        bs.append("time: ").append(tss).append("\n").append("description: ")
               .append(rsl.getString(2)).append("\n").append("pet ID: ").append(petID).append("\n")
                .append("pet: ").append(rsl.getString(3)).append("\n").append("veterinarian: ").
                append(rsl.getString(4)).append("\n");
      }
      outputTextArea.setText("Scheduled: \n"+ bs);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  private void cancelAppointment() {
    JDialog dialog = new JDialog(frame, "New Appointment", true);
    dialog.setSize(300, 200);
    dialog.setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new GridLayout(2, 2));

    JTextField petIdField = new JTextField();

    inputPanel.add(new JLabel("Appointment Time (yyyy-mm-dd HH:mm:ss):"));
    inputPanel.add(new JTextField());
    inputPanel.add(new JLabel("Pet ID:"));
    inputPanel.add(petIdField);

    int result = JOptionPane.showConfirmDialog(dialog, inputPanel, "Del Appointment",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    String ts = "";
    String petId = "";
    if (result == JOptionPane.OK_OPTION) {
      ts = ((JTextField) inputPanel.getComponent(1)).getText();
      petId = petIdField.getText();

      try {
        Timestamp appointmentTime = this.readTimeStamp(ts);
        String qry = "{CALL cancel_appointment(?, ?, ?)}";
        try {
          CallableStatement stmt = conn.prepareCall(qry);
          stmt.setString(1, empId);
          stmt.setTimestamp(2, appointmentTime);
          stmt.setInt(3, Integer.valueOf(petId));
          stmt.executeQuery();
          JOptionPane.showMessageDialog(null,
                  "Deleted", "Appointment Deletion",
                  JOptionPane.INFORMATION_MESSAGE);
          outputTextArea.setText("Deleted Info:\nAppointment Time: "
                  + ts + "    pet id: " + petId);
        } catch (SQLException e) {
          JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                  JOptionPane.ERROR_MESSAGE);
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Invalid Input", "Error",
                JOptionPane.ERROR_MESSAGE);
      }
    }
  }


  private Timestamp readTimeStamp(String time) {
    String[] st = time.replace("-"," ").replace(":", " ").split(" ");
    int y = 0;
    int m =0;
    int d = 0;
    int h = 0;
    int mi = 0;
    y = Integer.parseInt(st[0]);
    m = Integer.parseInt(st[1]);
    d = Integer.parseInt(st[2]);
    h = Integer.parseInt(st[3]);
    mi = Integer.parseInt(st[4]);
    return new java.sql.Timestamp(y-1900, m-1, d, h, mi, 0, 0);
  }



  public static final String blue = "\u001B[34m";
  public static final String red = "\u001B[31m";
  public static final String reset = "\u001B[0m";
  public static final String yellow = "\u001B[33m";
}

