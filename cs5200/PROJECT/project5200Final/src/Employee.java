import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.IllegalFormatException;
import java.util.Scanner;

public class Employee {
  private String emp_id;
  private Connection conn;

  public Employee(String emp_id, Connection conn) {
    this.emp_id = emp_id;
    this.conn = conn;
  }

  public void make_choice() {
    while (true) {
      Scanner sc = new Scanner(System.in);
      try {
        System.out.println(blue + "Choose an option: \n1. new appointment \n2. cancel appointment \n"
                + "3. display all appointments \n4. appointment detail\n5. exit" + reset);
        System.out.println("-----------------------------");
        int choice = sc.nextInt();
        if (choice == 1) {
          sc = new Scanner(System.in);
          Timestamp ts = this.readTimeStamp(sc);
          // check if schedule is already full at specific time
          while (is_book_full(ts) == 1) {
            System.out.println(red + "sorry, the appointment is full at that time, please provide another time:" + reset);
            ts = this.readTimeStamp(sc);
          }
          System.out.println(yellow + "Yes, this time is available" + reset);
          System.out.println(yellow + "** pet id: " + reset);
          sc.nextLine();
          int petID = Integer.parseInt(sc.nextLine());
          System.out.println(yellow + "What is the condition of your pet: " + reset);
          String description_of_request = sc.nextLine();
          System.out.println("-----------------------------");
          this.makeAppointment(ts, petID, description_of_request);
          System.out.println("-----------------------------");
        }
        if (choice == 2) {
          sc = new Scanner(System.in);
          System.out.println(yellow + "Provide information about cancellation: " + reset);
          Timestamp ts = this.readTimeStamp(sc);
          System.out.println(yellow + "** pet id: " + reset);
          int petID = sc.nextInt();
          System.out.println("-----------------------------");
          this.cancelAppointment(ts, petID);
          System.out.println("-----------------------------");
        }
        if (choice == 3) {
          this.display_appointments();
          System.out.println("-----------------------------");
        }
        if (choice == 4) {
          this.display_appointment_detail(sc);
          System.out.println("-----------------------------");
        }
        if (choice == 5) {
          System.out.println("-----------------------------");
          System.out.println(red + "employee exits\n" + reset);
          break;
        }
      } catch (Exception e) {
        System.out.println(red+"invalid input, try again:"+reset);
      }
    }
  }


  public int is_book_full(Timestamp appoint_time) {
    System.out.println(yellow+ "Checking...."+reset);
    int val = 0;
    String qry = "{? = CALL is_book_full(?,?)}";
    try {
      CallableStatement stmt = conn.prepareCall(qry);
      stmt.registerOutParameter(1, Types.INTEGER);
      stmt.setTimestamp(2, appoint_time);
      stmt.setString(3, emp_id);
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
      stmt.setString(1, emp_id);
      stmt.setTimestamp(2, ts);
      stmt.setInt(3, petID);
      stmt.setString(4, description_of_request);
      ResultSet rsl = stmt.executeQuery();

      System.out.println("Scheduled:");
      while (rsl.next()) {
        String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rsl.getTimestamp(1));
        System.out.printf("time: %1$s\ndescription: %2$s\npet: %3$s\nveterinarian: %4$s\n",
                s,rsl.getString(2), rsl.getString(3),rsl.getString(4));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }


  public void cancelAppointment(Timestamp ts, int petID) {
    String qry = "{CALL cancel_appointment(?, ?, ?)}";
    try {
      CallableStatement stmt = conn.prepareCall(qry);
      stmt.setString(1, emp_id);
      stmt.setTimestamp(2, ts);
      stmt.setInt(3, petID);
      stmt.executeQuery();
      System.out.println(red+ "appointment DELETED!"+reset);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } catch (IllegalArgumentException ex) {

    }
  }


  public void display_appointments() {
    String qry = "{CALL display_appointment_for_employee (?)}";
    try {
      CallableStatement stmt = conn.prepareCall(qry);
      stmt.setString(1, emp_id);
      ResultSet rsl = stmt.executeQuery();

      System.out.println(yellow+"all appointments are listed:"+reset);
      while (rsl.next()) {
        String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rsl.getTimestamp(2));
        System.out.printf("appointment ID: %1$d,  time: %2$s,  petID: %3$s,   pet: %4$s,"
                        + "family: %5$s,  show-up: %6$s\n\n",
                rsl.getInt(1), s, rsl.getString(3),rsl.getString(4), rsl.getString(5),rsl.getInt(6));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }


  public void display_appointment_detail(Scanner sc) {
    this.display_appointments();
    System.out.println(yellow+"Select an appointment ID from above:"+reset);
    int apointID = sc.nextInt();
    String qry = "{CALL display_appointment_detail(?, ?)}";
    try {
      CallableStatement stmt = conn.prepareCall(qry);
      stmt.setString(1, emp_id);
      stmt.setInt(2, apointID);
      ResultSet rsl = stmt.executeQuery();

      System.out.println(yellow+"basic information on appointment: "+reset+ apointID);
      while (rsl.next()) {
        String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rsl.getTimestamp(2));
        String s1 = "NULL";
        if (rsl.getDate(10) != null) {
          s1 = new SimpleDateFormat("MM/dd/yyyy").format(rsl.getDate(10));
        }
        System.out.printf("appointment: %1$d\ntime: %2$s\npet: %3$s"
                        + "\nfamily: %4$s\nappointment_description: %5$s\nveterinarian: %6$s\n"
                        + "room_id: %7$d\nexpenditure: $%8$.2f\nreceived: $%9$.2f"
                        + "\nreceived date: %10$s\n",
                rsl.getInt(1), s, rsl.getString(3),rsl.getString(4),rsl.getString(5),rsl.getString(6)
                ,rsl.getInt(7), rsl.getDouble(8),rsl.getDouble(9), s1);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private Timestamp readTimeStamp(Scanner sc) {
    int m =0;
    int d =0;
    int h =0;
    int mi =0;
    try {
      System.out.println(yellow + "** Appointment time:" + reset);
      System.out.println(yellow + "Month: " + reset);
      m = sc.nextInt();
      System.out.println(yellow + "Date: " + reset);
      d = sc.nextInt();
      System.out.println(yellow + "Hour: " + reset);
      h = sc.nextInt();
      System.out.println(yellow + "Minute: " + reset);
      mi = sc.nextInt();
    } catch (IllegalFormatException e) {
      System.out.println(red+"invalid input, try again:"+reset);
    }
    return new Timestamp(Year.now().getValue() - 1900, m - 1, d, h, mi, 0, 0);
  }

  public static final String blue = "\u001B[34m";
  public static final String red = "\u001B[31m";
  public static final String reset = "\u001B[0m";
  public static final String yellow = "\u001B[33m";
}

