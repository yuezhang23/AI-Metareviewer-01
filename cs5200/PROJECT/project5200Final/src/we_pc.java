/**
 * @author Beini Wang, Yi Shi, Yue Zhang
 * @since  2023-12-7
 */

import java.sql.*;
import java.util.Scanner;

/**
 *
 */
public class we_pc {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/we_pc";
  private static String user_type = "";
  private static String user_id = "";

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter MySQL username: ");
    String user = scanner.nextLine();
    System.out.print("Enter MySQL password: ");
    String password = scanner.nextLine();
    System.out.println("-----------------------------");

    try (Connection conn = DriverManager.getConnection(DB_URL, user, password)) {
      System.out.println("URL: " + DB_URL);
      System.out.println("Connection: " + conn);
      System.out.println("-----------------------------");


      while (true) {
        System.out.println("Choose an option: \n1. Sign In \n2. Sign Up \n3. Exit");
        System.out.println("-----------------------------");
        String choice = scanner.nextLine();

        switch (choice) {
          case "1":
            System.out.println("-----------------------------");
            String[] signin_outcome = signin.signIn(conn, scanner, user_type, user_id);
            user_type = signin_outcome[0];
            user_id = signin_outcome[1];
            if (user_type.equalsIgnoreCase("user")){
              petuser.user_choice(conn, user_id);
            } else if (user_type.equalsIgnoreCase("veterinarian")) {
              vet v = new vet(conn, user_id);
              v.user_choice();
            } else if (user_type.equalsIgnoreCase("employee")) {
              new Employee(user_id, conn).make_choice();
            } else {
              break;
            }
            break;
          case "2":
            System.out.println("-----------------------------");
            signup.signUp(conn, scanner);
            break;
          case "3":
            System.out.println("-----------------------------");
            conn.close();
            System.out.println("Database Disconnected.");
            user_type = "";
            user_id = "";
            System.out.println("Exiting program.");
            return;
          default:
            System.out.println("-----------------------------");
            System.out.println("Invalid input. Please try again.");
            break;
        }
      }

    } catch (Exception e) {
      System.out.println("Database connection failed");
    }
  }


}