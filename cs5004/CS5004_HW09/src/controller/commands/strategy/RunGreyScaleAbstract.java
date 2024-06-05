package controller.commands.strategy;

import java.util.Objects;
import java.util.Scanner;

import model.ImageDatabase;
import model.ImageState;

/**
 * This is an abstract class for overall greyscale related methods
 * that runs given command by continuously reading data from a Scanner object.
 */
public abstract class RunGreyScaleAbstract implements ImageTransform {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {
    Objects.requireNonNull(sc);
    Objects.requireNonNull(modelMap);

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of source ID\n");
    }
    String sourceID = sc.next();

    ImageState sourceModel = modelMap.getImage(sourceID);
    if (sourceModel == null) {
      throw new IllegalStateException("source model is not found\n");
    }
    ImageState destModel = this.transformImage(sourceModel);
    if (destModel == null) {
      throw new IllegalStateException("greyscale tool can't be initialize\n");
    }

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of new model ID\n");
    }
    String destID = sc.next();
    modelMap.addImage(destID, destModel);
  }

}
