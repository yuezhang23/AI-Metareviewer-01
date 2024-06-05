import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws SQLException {
    musicalDB md = new musicalDB();
    md.sc = new Scanner(System.in);

    md.getLogIn();
    md.getConnected();
    // search a particular genre
    List<String> genre_in_table = md.getFiledList("genre_name", "genres");
    System.out.println("Please enter a genre_name from list below:\n"+genre_in_table);
    String entered_genre = md.validateField();
    md.songWithTheGenre(entered_genre);
  }



}