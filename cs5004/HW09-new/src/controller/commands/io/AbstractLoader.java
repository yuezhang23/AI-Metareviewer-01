package controller.commands.io;

import java.util.Objects;
import java.util.Scanner;
import model.ImageState;
import model.ImageDatabase;

/**
 * This is an abstract class that implements the Command interface by running load command
 * and continuously reading data from a Scanner object.
 */
public abstract class AbstractLoader implements ImageLoader {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {
    Objects.requireNonNull(sc);
    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of argument 1\n");
    }
    String path = sc.next();
    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of argument 2\n");
    }
    String id = sc.next();

    ImageState model = this.loadImage(path);
    modelMap.addImage(id, model);
  }
}
