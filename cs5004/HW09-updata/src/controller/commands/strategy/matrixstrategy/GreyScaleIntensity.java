package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

public class GreyScaleIntensity extends Sepia {
  @Override
  protected Kernel makeKernel(Scanner sc, int size) {
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
