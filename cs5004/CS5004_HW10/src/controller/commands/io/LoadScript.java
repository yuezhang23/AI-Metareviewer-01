package controller.commands.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Objects;
import java.util.Scanner;

import controller.ControllerImpl;
import controller.commands.Command;
import model.ImageDatabase;
import view.oldview.ViewImpl;

/**
 * This class read a script file of commands valid to read from the program and execute all
 * the commands from the script one line at a time.
 */
public class LoadScript implements Command {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {
    Objects.requireNonNull(sc);
    String file = sc.next();
    try {

      Scanner sc1 = new Scanner(new FileInputStream(file));
      while (sc1.hasNext()) {
        Readable in = new StringReader(sc1.nextLine());
        new ControllerImpl(modelMap, in, new ViewImpl(new StringBuilder())).processing();
      }
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("File " + file + " not found!\n");
    }
  }
}
