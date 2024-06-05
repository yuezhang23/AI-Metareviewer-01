package controller.commands.strategy;

import java.util.Objects;
import java.util.Scanner;
import controller.commands.Command;
import model.ImageDatabase;
import model.ImageState;

/**
 * This class implements the Command interface by running brighten command
 * and continuously reading data from a Scanner object.
 */
public class RunBrighten implements Command {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {
    Objects.requireNonNull(sc);
    Objects.requireNonNull(modelMap);
    if (!sc.hasNextInt()) {
      throw new IllegalStateException("missing integer for increment\n");
    }
    int incr = sc.nextInt();

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of source ID\n");
    }
    String sourceID = sc.next();

    ImageState sourceModel = modelMap.getImage(sourceID);
    if (sourceModel == null) {
      throw new IllegalStateException("source model is not found\n");
    }
    ImageState destModel = new BrightenTransform(incr).transformImage(sourceModel);

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of new model ID\n");
    }
    String destID = sc.next();
    modelMap.addImage(destID, destModel);
  }
}
