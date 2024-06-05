package controller.commands.strategy.matrixstrategy;

/**
 * This class implements the Kernel interface.
 */
public class KernelImpl implements Kernel {
  private int size;
  private double[][] matrix;

  /**
   * Construct a Kernel object by setting size of the kernel matrix and
   * initialize the matrix.
   * @param size kernel matrix size
   */
  public KernelImpl(int size) {
    this.size = size;
    this.matrix = new double[size][size];
  }

  @Override
  public int getKernelSize() {
    return size;
  }

  @Override
  public double getKernelValue(int h, int w) {
    return matrix[h][w];
  }

  @Override
  public Kernel[][] kernelFilterMatrix(int[][] source) {
    int height = source.length;
    int width = source[0].length;
    Kernel[][] kernelMatrix = new Kernel[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        kernelMatrix[i][j] = new KernelImpl(size);
        for (int k = - (size / 2); k <= size / 2; k++) {
          for (int h = - (size / 2); h <= size / 2; h++) {
            int sourceData;
            try {
              sourceData = source[i + k][j + h];
            } catch (IndexOutOfBoundsException e) {
              sourceData = 0;
            }
            kernelMatrix[i][j].setKernel(size / 2 + k, size / 2 + h, sourceData);
          }
        }
      }
    }
    return kernelMatrix;
  }

  @Override
  public void setKernel(int h, int w, double value) {
    this.matrix[h][w] = value;
  }

  @Override
  public double kernelMultiply(Kernel kernel) {
    double result = 0;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        result += matrix[i][j] * kernel.getKernelValue(i, j);
      }
    }
    return result;
  }

  @Override
  public double[] kernelScale(int[] array) {
    if (size != array.length) {
      throw new IllegalArgumentException("array size is incompatible with kernel");
    }
    double[] result = new double[size];
    for (int i = 0; i < size; i++) {
      result[i] = 0;
      for (int j = 0; j < size; j++) {
        result[i] += array[j] * matrix[i][j];
      }
    }
    return result;
  }

  @Override
  public String toStringM() {
    int size = this.getKernelSize();
    StringBuilder log = new StringBuilder();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        log.append(this.getKernelValue(i, j) + " ");
      }
      log.append("\n");
    }
    return log.toString();
  }
}
