import java.sql.SQLException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws SQLException {
    // Create an object of musicalDB
    musicalDB md = new musicalDB();
    md.sc = new Scanner(System.in);

    // Log in to the given database from input username and password
    md.getLogIn();
    md.getConnected();

    if (md.connection != null) {
      // Retrieve all the genres exist in the DB.
      md.getFiledList("genre_name", "genres");

      // validate the entry genre from user by presenting the genre options.
      System.out.println("Please enter a genre_name from list below:\n" + md.fieldList);
      String entered_genre = md.validateField();

      // call procedure song_has_genre(genre_p) and print out the result set of the songs.
      md.songWithTheGenre(entered_genre);
    }
  }

}