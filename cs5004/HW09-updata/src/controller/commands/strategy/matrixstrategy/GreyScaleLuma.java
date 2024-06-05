package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

public class GreyScaleLuma extends Sepia {
  @Override
  protected Kernel makeKernel(Scanner sc, int size) {
    Kernel indexMatrix = new KernelImpl(size);

    int i = 0;
    while (i < size) {
      indexMatrix.setKernel(i, 0, 0.2126);
      indexMatrix.setKernel(i, 1, 0.7152);
      indexMatrix.setKernel(i, 2, 0.0722);
      i ++;
    }
    return indexMatrix;
  }
}
