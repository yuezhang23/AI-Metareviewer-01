package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

/**
 * This class extends the Sepia class as given kernel matrix is 3x3 sized and
 * transform the values of given RGB array all into the average of the array sum.
 */
public class GreyScaleIntensity extends Sepia {
  @Override
  public Kernel makeKernel(Scanner sc, int size) {
    Kernel indexMatrix = new KernelImpl(size);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        double index = 1.0 / 3.0;
        indexMatrix.setKernel(i, j, index);
      }
    }
    return indexMatrix;
  }
}
