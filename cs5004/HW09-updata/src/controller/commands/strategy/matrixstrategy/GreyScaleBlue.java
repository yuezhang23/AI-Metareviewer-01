package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

public class GreyScaleBlue extends Sepia {
  @Override
  protected Kernel makeKernel(Scanner sc, int size) {
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
