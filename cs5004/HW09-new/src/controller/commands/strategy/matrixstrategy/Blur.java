package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

/**
 * This class extends the Filter class and implements the filtering of
 * image pixels with a fixed 3x3 sized kernel matrix with custom values.
 */
public class Blur extends Filter {

  @Override
  protected Kernel makeKernel(Scanner sc, int size) {
    if (size != 3) {
      throw new IllegalArgumentException("this is gaussian blur at size 3");
    }
    Kernel indexMatrix = new KernelImpl(size);
    indexMatrix.setKernel(0, 0, 1.0 / 16.0);
    indexMatrix.setKernel(0, 1, 1.0 / 8.0);
    indexMatrix.setKernel(0, 2, 1.0 / 16.0);
    indexMatrix.setKernel(1, 0, 1.0 / 8.0);
    indexMatrix.setKernel(1, 1, 1.0 / 4.0);
    indexMatrix.setKernel(1, 2, 1.0 / 8.0);
    indexMatrix.setKernel(2, 0, 1.0 / 16.0);
    indexMatrix.setKernel(2, 1, 1.0 / 8.0);
    indexMatrix.setKernel(2, 2, 1.0 / 16.0);
    return indexMatrix;
  }
}
