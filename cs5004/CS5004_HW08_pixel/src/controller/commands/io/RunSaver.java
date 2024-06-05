package controller.commands.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import controller.commands.Command;
import model.ImageState;
import model.ImageDatabase;

/**
 * This class implements the Command interface by running save command
 * and continuously reading data from a Scanner object and export data
 * to a standard PPM file.
 */
public class RunSaver implements Command {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {
    Objects.requireNonNull(sc);
    Objects.requireNonNull(modelMap);

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of argument 1");
    }
    String destPath = sc.next();
    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of argument 2");
    }
    String destID = sc.next();

    ImageState destImage = modelMap.getImage(destID);
    if (destImage == null) {
      throw new IllegalStateException("image not found");
    }
    StringBuilder log = new StringBuilder();
    new ImageSaving(destImage, log).savingPPM();

    this.toFile(destPath, log.toString());
  }

  protected void toFile(String newPath, String data) {
    try {
      FileWriter wr = new FileWriter(newPath);
      wr.write(data);
      wr.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
