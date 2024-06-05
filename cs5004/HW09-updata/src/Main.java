import java.io.InputStreamReader;

import controller.ControllerImpl;
import model.ImageData;
import view.ViewImpl;

/**
 * This class run the main method.
 */
public class Main {

  /**
   * This is a demo main method.
   * @param args an array of command arguments from the user
   */
  public static void main(String []args) {
    // there should be no contents in main
    new ControllerImpl(new ImageData(),
            new InputStreamReader(System.in), new ViewImpl(System.out)).processing();
  }
}

