package controller.commands.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Scanner;

import controller.ControllerImpl;
import controller.commands.Command;
import model.ImageDatabase;
import view.ViewImpl;

/**
 *
 */
public class LoadScript implements Command {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {

    // this may change as the commandline format changes
    String filename = sc.next().split("-")[1];

    try {
      Scanner sc1 = new Scanner(new FileInputStream(filename + ".txt"));
      while (sc1.hasNext()) {
        Readable in = new StringReader(sc1.nextLine()); // next() not work
        new ControllerImpl(modelMap, in, new ViewImpl(new StringBuilder())).processing();
      }
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("File " + filename + " not found!\n");
    }
  }
}
