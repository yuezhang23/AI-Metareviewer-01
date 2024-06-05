import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;

public class signup {
  private static final List<String> card_type = Arrays.asList("Debit", "VISA", "MasterCard", "American Express", "Discover");

  public static void signUp(Connection conn, Scanner scanner) throws SQLException {
    System.out.print("Enter your new username: ");
    String username = scanner.nextLine();

    System.out.print("Enter your new password: ");
    String password = scanner.nextLine();

    System.out.print("Enter your card number (length 12, type exit to exit): ");
    String p_card_number = scanner.nextLine();
    while (p_card_number.length() != 12 || !isNumeric(p_card_number)) {
      System.out.print("The card number length need to be 12 and numeric, type exit to exit: ");
      if (p_card_number.equalsIgnoreCase("exit")){
        System.out.println("Exiting program.");
        return;
      }
      p_card_number = scanner.nextLine();
    }

    System.out.print("Enter your first name: ");
    String p_first_name = scanner.nextLine();

    System.out.print("Enter your last name: ");
    String p_last_name = scanner.nextLine();

    System.out.print("Enter your card type, ");
    System.out.print("Choose from: Debit, VISA, MasterCard, American Express, Discover");
    String p_card_type = scanner.nextLine();

    System.out.print("Enter your billing street number: ");
    Integer billing_street_number = Integer.valueOf(scanner.nextLine());

    System.out.print("Enter your billing street name: ");
    String billing_street_name = scanner.nextLine();
    System.out.print("Enter your billing town: ");
    String billing_street_town = scanner.nextLine();
    System.out.print("Enter your billing state abbreviation: ");
    String billing_street_state_abbrev = scanner.nextLine();
    System.out.print("Enter your billing zip code: ");
    String billing_street_zip = scanner.nextLine();

    try (CallableStatement stmt = conn.prepareCall("{CALL signup_pet_owner(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
      stmt.setString(1, username);
      stmt.setString(2, password);
      stmt.setString(3, p_card_number);
      stmt.setString(4, p_first_name);
      stmt.setString(5, p_last_name);
      stmt.setString(6, p_card_type);
      stmt.setInt(7, billing_street_number);
      stmt.setString(8, billing_street_name);
      stmt.setString(9, billing_street_town);
      stmt.setString(10, billing_street_state_abbrev);
      stmt.setString(11, billing_street_zip);
      stmt.execute();
      System.out.println("Sign-up successful.");
    } catch (SQLException e) {
      System.out.println("Sign-up failed: " + e.getMessage());
    }
  }

  public static boolean isNumeric(String str) {
    for (int i = 0; i < str.length(); i++) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
