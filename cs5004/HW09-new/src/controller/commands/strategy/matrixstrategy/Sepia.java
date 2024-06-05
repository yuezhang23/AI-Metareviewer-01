package controller.commands.strategy.matrixstrategy;

import java.util.Scanner;

import model.Image;
import model.ImageImpl;
import model.ImageState;

/**
 * This class extends the AbstractColorChange class and implements the filtering of
 * image pixels with a fixed 3x3 sized kernel matrix with custom values.
 */
public class Sepia extends AbstractColorChange {

  @Override
  public ImageState colorChange(Kernel matrix, ImageState image) {
    int h = image.getHeight();
    int w = image.getWidth();

    Image newImage = new ImageImpl(h, w);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int oldR = image.getRedChannel(i, j);
        int oldG = image.getGreenChannel(i, j);
        int oldB = image.getBlueChannel(i, j);
        int[] arr = new int[] {oldR, oldG, oldB};
        double[] newArr = matrix.kernelScale(arr);
        newImage.setPixel(i, j, clampDouble(newArr[0]),
                clampDouble(newArr[1]), clampDouble(newArr[2]));
      }
    }
    return newImage;
  }

  @Override
  protected Kernel makeKernel(Scanner sc, int size) {
    Kernel indexMatrix = new KernelImpl(size);
    indexMatrix.setKernel(0, 0, 0.393);
    indexMatrix.setKernel(0, 1, 0.769);
    indexMatrix.setKernel(0, 2, 0.189);
    indexMatrix.setKernel(1, 0, 0.349);
    indexMatrix.setKernel(1, 1, 0.686);
    indexMatrix.setKernel(1, 2, 0.168);
    indexMatrix.setKernel(2, 0, 0.272);
    indexMatrix.setKernel(2, 1, 0.534);
    indexMatrix.setKernel(2, 2, 0.131);
    return indexMatrix;
  }
}
