import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;

/**
 * This class illustrates a number of methods to implement queries on data connected to
 * SQL database music1_db.
 */
public class musicalDB {
  private String userName;
  private String passWord;
  private final String serverName = "localhost";
  private final String portNum = "3306";
  private final String dbName = "music1_db";



  public String fieldName = "";
  public List<String> fieldList = new ArrayList<>();
  public Scanner sc = null;
  public Connection connection = null;

  /**
   * This is the constructor.
   */
  public musicalDB() {
    this.userName = null;
    this.passWord = null;
  }


  /**
   * Given input username and password, connect this program to SQL database.
   * @throws SQLException when access denied from the host of SQL
   */
  public void getConnected() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.passWord);
    try {
      connection = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":"
                      + this.portNum + "/" + this.dbName
                      + "?characterEncoding=utf8&useSSL = false&allowPublicKeyRetrieval=true",
              connectionProps);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }


  /**
   * Assign username to the field userName.
   * @param userName user input string
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }


  /**
   * Assign password to the field passWord.
   * @param password user input string
   */
  public void setUserPassword(String password) {
    this.passWord = password;
  }


  /**
   * Provide the prompts for log-in.
   */
  public void getLogIn () {
    System.out.println("Please enter your MySql userName:");
    if (sc.hasNext()) {
      setUserName(sc.next());
    }

    System.out.println("Please enter your MySql password:");
    if (sc.hasNext()) {
      setUserPassword(sc.next());
    }
  }


  /**
   * Return a list of all values drawn from the given column of the given table.
   * @param items target field name in the DB
   * @param tableName target table in the DB
   * @return all field values
   * @throws SQLException if field or table is not found in the DB
   */
  public void getFiledList(String items, String tableName) throws SQLException {
    String qr = "SELECT " + items + " FROM " + tableName;
    try {
      PreparedStatement ps = connection.prepareStatement(qr);
      ResultSet rsl = ps.executeQuery();
      while (rsl.next()) {
        fieldList.add(rsl.getString(1));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }


  /**
   * Return a valid genre name from user input.
   * @return genre name
   */
  public String validateField() {
    String start  =sc.nextLine();
    while (sc.hasNext()) {
      fieldName = sc.nextLine();
      if (fieldList.contains(fieldName)) {
        System.out.println("reserved: " + fieldName);
        break;
      }
      if (!fieldName.isEmpty()) {
        System.out.println("Invalid vendor name: "+ fieldName+ ""
                + ",Try write genre name in the right format:");
      }
    }
    return fieldName;
  }


  /**
   * Given genre_name, print out the result set that contains the songs with that genre.
   * @param genre string of genre name
   */
  public void songWithTheGenre(String genre) {
    String qry = "{CALL song_has_genre (?)}";
    try {
      CallableStatement stmt = connection.prepareCall(qry);
      stmt.setString(1, genre);
      ResultSet rsl = stmt.executeQuery();
      System.out.println("printing out all the songs that have the same genre of "+genre+":");
      int count = 0;
      while (rsl.next()) {
        count += 1;
        System.out.printf("song_id: %1$s, song_name: %2$s, album_name: %3$s\n",
                rsl.getInt(1), rsl.getString(2), rsl.getString(3));
      }
      if (count==0) {
        System.out.println("Sorry, None result");
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

}
