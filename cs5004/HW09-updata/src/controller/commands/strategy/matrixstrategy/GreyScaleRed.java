package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

public class GreyScaleRed extends Sepia {

  @Override
  protected Kernel makeKernel(Scanner sc, int size) {
    Kernel indexMatrix = new KernelImpl(size);

    int i = 0;
    while (i < size) {
      indexMatrix.setKernel(i, 0, 1);
      indexMatrix.setKernel(i, 1, 0);
      indexMatrix.setKernel(i, 2, 0);
      i ++;
    }
    return indexMatrix;
  }
}
