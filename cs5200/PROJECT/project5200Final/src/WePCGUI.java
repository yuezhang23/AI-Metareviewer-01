import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class WePCGUI {
  private final String DB_URL = "jdbc:mysql://localhost:3306/we_pc";
  private JFrame Login;
  private String user_type = "";
  private String user_id = "";
  private Connection conn;
  private JButton signInButton;
  private JButton signUpButton;
  private JTextField usernameField;
  private JPasswordField passwordField;
  private boolean signUpSuccessful = false;

  public WePCGUI() {

    JPanel loginPanel = new JPanel(new GridLayout(2, 2));
    JTextField enterUser = new JTextField();
    JTextField enterPass = new JPasswordField();

    loginPanel.add(new JLabel("Enter MySQL username:"));
    loginPanel.add(enterUser);
    loginPanel.add(new JLabel("Enter MySQL password:"));
    loginPanel.add(enterPass);

    int result = JOptionPane.showConfirmDialog(null, loginPanel, "Connect to MySQL Database",
            JOptionPane.OK_CANCEL_OPTION);
    String user = "";
    String password = "";
    if (result == JOptionPane.OK_OPTION) {
      user = enterUser.getText();
      password = String.valueOf(enterPass.getText());
    } else {
      return;
    }

    try {
      conn = DriverManager.getConnection(DB_URL, user, password);
      System.out.println("URL: " + DB_URL);
      System.out.println("Connection: " + conn);
      System.out.println("-----------------------------");
    } catch (SQLException exception) {
      JOptionPane.showMessageDialog(null, "Login Failed.",
              "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    Login = new JFrame("Login");

    // Initialize Swing components
    usernameField = new JTextField(20);
    passwordField = new JPasswordField(20);
    signInButton = new JButton("Sign In");
    signUpButton = new JButton("Sign Up");

    JPanel panel = new JPanel(new GridLayout(3, 2));
    panel.add(new JLabel("Username:"));
    panel.add(usernameField);
    panel.add(new JLabel("Password:"));
    panel.add(passwordField);
    panel.add(signInButton);
    panel.add(signUpButton);
    Login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add the panel to the content pane of the frame
    Login.getContentPane().add(panel);

    // Set the size and make the frame visible
    Login.setSize(300, 150);
    Login.setVisible(true);
    Login.getContentPane().add(panel);
    Login.setLocationRelativeTo(null);

    signInButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        performSignIn();
      }
    });

    signUpButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        createSignUpForm();
      }
    });

  }

  private void performSignIn() {
    String[] signinOutcome = signIn();
    user_type = signinOutcome[0];
    user_id = signinOutcome[1];

    if (user_type.equalsIgnoreCase("user")) {

      JOptionPane.showMessageDialog(null, "Sign-in successful.");
      Login.dispose();
      PetOwnerGUI ownerGUI = new PetOwnerGUI(conn, user_id);

    } else if (user_type.equalsIgnoreCase("veterinarian")) {

      JOptionPane.showMessageDialog(null, "Sign-in successful.");
      Login.dispose();
      VetGUI vet = new VetGUI(conn, user_id);
    } else if (user_type.equalsIgnoreCase("employee")) {

      // Handle employee-specific actions here
      JOptionPane.showMessageDialog(null, "Sign-in successful.");
      Login.dispose();
      EmpGUI emp = new EmpGUI(conn, user_id);
    } else {
      JOptionPane.showMessageDialog(null, "Sign-in failed: ",
              "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private String[] signIn() {
    String username = usernameField.getText();
    String code = String.valueOf(passwordField.getPassword());

    try (CallableStatement stmt = conn.prepareCall("{CALL validate_user_credentials(?, ?)}")) {
      stmt.setString(1, username);
      stmt.setString(2, code);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        boolean success = rs.getBoolean("success");
        if (success) {
          String type = rs.getString("user_type");
          user_type = type;
          user_id = username;
        } else {
          user_type = "";
          user_id = "";
          String errorMessage = "Sign in failed.";
          JOptionPane.showMessageDialog(Login, errorMessage,
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Sign-in failed: " +
              e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return new String[] {user_type, user_id};
  }

  private void createSignUpForm() {
    JFrame signUpFrame = new JFrame("Sign Up");
    signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    signUpFrame.setSize(400, 300);
    signUpFrame.setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    signUpFrame.add(panel);
    List<String> cardTypes = Arrays.asList("Debit", "VISA", "MasterCard",
            "American Express", "Discover");

    // Create Swing components for the sign-up form
    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField(20);

    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField(20);

    JLabel cardNumberLabel = new JLabel("Card Number:");
    JTextField cardNumberField = new JTextField(20);

    JLabel firstNameLabel = new JLabel("First Name:");
    JTextField firstNameField = new JTextField(20);

    JLabel lastNameLabel = new JLabel("Last Name:");
    JTextField lastNameField = new JTextField(20);

    JLabel cardTypeLabel = new JLabel("Card Type:");
    JComboBox<String> cardTypeComboBox = new JComboBox<>(cardTypes.toArray(new String[0]));

    JLabel billingStreetNumberLabel = new JLabel("Billing Street Number:");
    JTextField billingStreetNumberField = new JTextField(10);

    JLabel billingStreetNameLabel = new JLabel("Billing Street Name:");
    JTextField billingStreetNameField = new JTextField(20);

    JLabel billingTownLabel = new JLabel("Billing Town:");
    JTextField billingTownField = new JTextField(20);

    JLabel billingStateAbbrevLabel = new JLabel("Billing State Abbreviation:");
    JTextField billingStateAbbrevField = new JTextField(2);

    JLabel billingZipLabel = new JLabel("Billing ZIP Code:");
    JTextField billingZipField = new JTextField(5);

    JButton signUpButton = new JButton("Sign Up");

    // Add action listener for the "Sign Up" button
    signUpButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          String username = usernameField.getText();
          char[] password = passwordField.getPassword();
          String cardNumber = cardNumberField.getText();
          String firstName = firstNameField.getText();
          String lastName = lastNameField.getText();
          String cardType = cardTypeComboBox.getSelectedItem().toString();
          int billingStreetNumber = Integer.parseInt(billingStreetNumberField.getText());
          String billingStreetName = billingStreetNameField.getText();
          String billingTown = billingTownField.getText();
          String billingStateAbbrev = billingStateAbbrevField.getText();
          String billingZip = billingZipField.getText();

          // Call your sign-up logic here using the provided user inputs
          performSignUp(username, password, cardNumber, firstName, lastName, cardType,
                  billingStreetNumber, billingStreetName, billingTown, billingStateAbbrev, billingZip);

          // Close the sign-up window if sign-up is successful
          if (signUpSuccessful) {
            signUpFrame.dispose();
          }
        } catch (Exception exception) {
          JOptionPane.showMessageDialog(null, "Sign-up failed.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // Add components to the panel
    panel.setLayout(new GridLayout(13, 2));
    panel.add(usernameLabel);
    panel.add(usernameField);
    panel.add(passwordLabel);
    panel.add(passwordField);
    panel.add(cardNumberLabel);
    panel.add(cardNumberField);
    panel.add(firstNameLabel);
    panel.add(firstNameField);
    panel.add(lastNameLabel);
    panel.add(lastNameField);
    panel.add(cardTypeLabel);
    panel.add(cardTypeComboBox);
    panel.add(billingStreetNumberLabel);
    panel.add(billingStreetNumberField);
    panel.add(billingStreetNameLabel);
    panel.add(billingStreetNameField);
    panel.add(billingTownLabel);
    panel.add(billingTownField);
    panel.add(billingStateAbbrevLabel);
    panel.add(billingStateAbbrevField);
    panel.add(billingZipLabel);
    panel.add(billingZipField);
    panel.add(signUpButton);

    signUpFrame.setVisible(true);
  }

  private void performSignUp(String username, char[] password, String cardNumber, String firstName,
                             String lastName, String cardType, int billingStreetNumber,
                             String billingStreetName, String billingTown, String billingStateAbbrev,
                             String billingZip) {
    try (CallableStatement stmt = conn.prepareCall(
            "{CALL signup_pet_owner(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
      stmt.setString(1, username);
      stmt.setString(2, new String(password));
      stmt.setString(3, cardNumber);
      stmt.setString(4, firstName);
      stmt.setString(5, lastName);
      stmt.setString(6, cardType);
      stmt.setInt(7, billingStreetNumber);
      stmt.setString(8, billingStreetName);
      stmt.setString(9, billingTown);
      stmt.setString(10, billingStateAbbrev);
      stmt.setString(11, billingZip);
      stmt.execute();
      signUpSuccessful = true;
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "Sign-up failed: " +
              e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      signUpSuccessful = false;
    }
  }
}
