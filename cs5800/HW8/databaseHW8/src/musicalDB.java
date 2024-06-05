import java.util.*;
import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;
import java.sql.*;

public class Protocol {
  private String userName;
  private String passWord;
  private final String serverName = "localHost";
  private final String portNum = "OCT16";
  private final String dbName = "protocol";

  public String vendorName;
  public List<String> vendorList = new ArrayList<>();
  public Scanner sc = null;
  public Connection connection;


  public void getConnected() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("Usr", this.userName);
    connectionProps.put("PassWord", this.passWord);

    connection = DriverManager.getConnection("jdbc:mysql//" + this.serverName + ":"
            + this.portNum + "/" + this.dbName
            + "?characterEncoding = UTF-8&useSSL = false", connectionProps);
  }


  public void setUserName(String userName) {
    this.userName = userName;
  }


  public void setUserPassword(String password) {
    this.passWord = password;
  }


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


  public void getVendorList() throws SQLException {
    PreparedStatement ps = connection.prepareStatement("SELECT song_name FROM songs");
    ResultSet rsl = ps.executeQuery();

    while (rsl.next()) {
      vendorList.add(rsl.getString("song_name"));
      System.out.println(rsl.getString(1));
    }
  }


  public void validateVendor() {
    while (true) {
      vendorName = sc.nextLine();
      if (!vendorName.equals("")) {
        System.out.println(vendorName);
      }
      if (vendorList.contains(vendorName)) {
        break;
      }
      System.out.println("Invalid vendor name. Try again");
    }
  }

}
