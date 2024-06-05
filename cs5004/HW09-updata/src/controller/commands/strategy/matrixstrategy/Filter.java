package controller.commands.strategy.matrixstrategy;

import java.util.HashMap;
import java.util.Map;

import model.Image;
import model.ImageImpl;
import model.ImageState;

public class Filter extends AbstractColorChange {
  private Map<String, int[][]> pixelComponentIntMatrix;

  public Filter() {
    this.pixelComponentIntMatrix = new HashMap<>();
  }

  @Override
  public ImageState colorChange(Kernel matrix, ImageState image) {

    this.getSourceComponentMatrix(image);
    int h = image.getHeight();
    int w = image.getWidth();
    Image newImage = new ImageImpl(h, w);

    Kernel[][] kernelRedMatrix = matrix.kernelFilterMatrix(pixelComponentIntMatrix.get("red"));
    Kernel[][] kernelGreenMatrix = matrix.kernelFilterMatrix(pixelComponentIntMatrix.get("green"));
    Kernel[][] kernelBlueMatrix = matrix.kernelFilterMatrix(pixelComponentIntMatrix.get("blue"));

    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int newRed = clampDouble(matrix.kernelMultiply(kernelRedMatrix[i][j]));
        int newGreen = clampDouble(matrix.kernelMultiply(kernelGreenMatrix[i][j]));
        int newBlue = clampDouble(matrix.kernelMultiply(kernelBlueMatrix[i][j]));
        newImage.setPixel(i, j, newRed, newGreen, newBlue);
      }
    }
    return newImage;
  }

  private void getSourceComponentMatrix(ImageState image) {
    int h = image.getHeight();
    int w = image.getWidth();

    int[][] sourceRed = new int[h][w];
    int[][] sourceGreen = new int[h][w];
    int[][] sourceBlue = new int[h][w];

    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        sourceRed[i][j] = image.getRedChannel(i, j);
        sourceGreen[i][j] = image.getGreenChannel(i, j);
        sourceBlue[i][j] = image.getBlueChannel(i, j);
      }
    }
    pixelComponentIntMatrix.put("red", sourceRed);
    pixelComponentIntMatrix.put("green", sourceGreen);
    pixelComponentIntMatrix.put("blue", sourceBlue);
  }
}
