package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

public class Sharpen extends Filter {


  @Override
  protected Kernel makeKernel(Scanner sc, int size) {
    if (size != 5) {
      throw new IllegalArgumentException("this is sharpen image at size 5");
    }
    Kernel indexMatrix = new KernelImpl(size);
    for (int i = 1; i <=3; i++) {
      indexMatrix.setKernel(i, 0, -(1.0 / 8.0));
      indexMatrix.setKernel(i, 1, 1.0 / 4.0);
      indexMatrix.setKernel(i, 2, 1.0 / 4.0);
      indexMatrix.setKernel(i, 3, 1.0 / 4.0);
      indexMatrix.setKernel(i, 4, -(1.0 / 8.0));
    }
    for (int i = 0; i < 5; i++) {
      indexMatrix.setKernel(0, i, -(1.0 / 8.0));
      indexMatrix.setKernel(4, i, -(1.0 / 8.0));
    }
    indexMatrix.setKernel(2, 2, 1);
    return indexMatrix;
  }

}
