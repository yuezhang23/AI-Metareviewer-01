package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

/**
 * This class extends the Sepia class as given kernel matrix is 3x3 sized and
 * transform the values of given RGB array all into the blue component value.
 */
public class GreyScaleBlue extends Sepia {
  @Override
  public Kernel makeKernel(Scanner sc, int size) {
    Kernel indexMatrix = new KernelImpl(size);

    int i = 0;
    while (i < size) {
      indexMatrix.setKernel(i, 0, 0);
      indexMatrix.setKernel(i, 1, 0);
      indexMatrix.setKernel(i, 2, 1);
      i ++;
    }
    return indexMatrix;
  }
}
