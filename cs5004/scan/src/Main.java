import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    try {
      Scanner sc = new Scanner(new FileInputStream("empty.txt"));
      System.out.println(sc.nextLine() == "");
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}