package controller.commands.strategy.matrixstrategy;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import model.ImageDatabase;
import model.ImageState;

/**
 * This abstract class implements the ColorChange interface by continuously reading
 * data from a scanner.
 */
public abstract class AbstractColorChange implements ColorChange {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {
    Objects.requireNonNull(sc);
    Objects.requireNonNull(modelMap);
    if (!sc.hasNextInt()) {
      throw new IllegalStateException("missing dimension size for pixel filter or transform\n");
    }
    int dimension = sc.nextInt();
    if (dimension % 2 == 0) {
      throw new IllegalStateException("dimension is not an odd number\n");
    }
    if (dimension <= 0) {
      throw new IllegalStateException("dimension is not positive integer\n");
    }

    // this goes to dynamic dispatching
    // kernel from filter/sepia/blur/greyscale
    Kernel kernel = this.makeKernel(sc, dimension);

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of source ID\n");
    }
    String sourceID = sc.next();

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of new model ID\n");
    }
    String destID = sc.next();

    ImageState sourceModel = modelMap.getImage(sourceID);
    if (sourceModel == null) {
      throw new IllegalStateException("source model is not found\n");
    }
    // dynamic dispatch
    ImageState destModel = this.colorChange(kernel, sourceModel);
    modelMap.addImage(destID, destModel);
  }


  protected static int clampDouble(double value) {
    if (value < 0) {
      return 0;
    }
    return Math.min((int) Math.round(value), 255);
  }

  // user input for kernel
  protected Kernel makeKernel(Scanner sc, int size) {
    Kernel indexMatrix = new KernelImpl(size);
    int i = 0;
    while (sc.hasNext() && i < size) {
      // exceptions about input double
      try {
        for (int j = 0; j < size; j++) {
          String s = sc.next();
          double data;
          if (s.contains("/")) {
            String[] sd = s.split("/");
            data = Double.parseDouble(sd[0]) / Double.parseDouble(sd[1]);
          } else {
            data = Double.parseDouble(s);
          }
          indexMatrix.setKernel(i, j, data);
        }
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("lack of required numbers for index matrix");
      }
      i ++;
    }
    return indexMatrix;
  }
}
