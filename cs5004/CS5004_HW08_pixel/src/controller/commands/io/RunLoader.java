package controller.commands.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import controller.commands.Command;
import model.ImageDatabase;
import model.ImageState;

/**
 * This class implements the Command interface by running load command
 * and continuously reading data from a Scanner object.
 */
public class RunLoader implements Command {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {
    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of argument 1\n");
    }
    String path = sc.next();
    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of argument 2\n");
    }
    String id = sc.next();

    StringBuilder imageData = new StringBuilder();
    try {
      Scanner sc1 = new Scanner(new FileInputStream(path));
      while (sc1.hasNextLine()) {
        String s = sc1.nextLine();
        if (s.charAt(0) != '#') {
          imageData.append(s + System.lineSeparator());
        }
      }
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("File " + path + " not found!\n");
    }
    String str = imageData.toString();
    ImageState model = new ImageLoading(str).loading();
    modelMap.addImage(id, model);
  }
}
