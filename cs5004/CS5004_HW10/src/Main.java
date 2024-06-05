import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.*;

import controller.ControllerGUI;
import controller.ControllerImpl;
import controller.commands.io.LoadScript;
import model.ImageData;
import view.ViewGUI;
import view.oldview.ViewImpl;

/**
 * This class run the main method.
 */
public class Main {

  /**
   * This is a demo main method.
   * @param args array of command arguments from the user
   */
  public static void main(String []args) {
    Scanner input = new Scanner(System.in);
    String hint0 = input.nextLine();

    if (hint0.contains("-text")) {
      new ControllerImpl(new ImageData(),
              new InputStreamReader(System.in), new ViewImpl(System.out)).processing();
    }

    if (hint0.contains("script")) {
      Scanner sc = new Scanner(hint0.split(" ")[1]);
      new LoadScript().runCommand(sc, new ImageData());
    }
    if (hint0.equals(" ")) {
      ViewGUI.setDefaultLookAndFeelDecorated(false);
      ViewGUI frame = new ViewGUI();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      new ControllerGUI(frame).run();
    }
  }
}