import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class signin {
  public static String[] signIn(Connection conn, Scanner scanner, String user_type, String user_id) throws SQLException {
    System.out.print("Enter username for validation: ");
    String validateUser = scanner.nextLine();
    System.out.print("Enter password for validation: ");
    String validatePassword = scanner.nextLine();
    System.out.println("-----------------------------");

    try (CallableStatement stmt = conn.prepareCall("{CALL validate_user_credentials(?, ?)}")) {
      stmt.setString(1, validateUser);
      stmt.setString(2, validatePassword);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        boolean success = rs.getBoolean("success");
        if (success) {
          String type = rs.getString("user_type");
          user_type = type;
          user_id = validateUser;
          System.out.println("Sign-in successful. Welcome, " + user_type + " " + user_id);
          System.out.println("-----------------------------");
        } else {
          user_type = "";
          user_id = "";
          System.out.println("Sign-in failed.");
        }
      }
    }
    return new String[]{user_type, user_id};
  }
}
