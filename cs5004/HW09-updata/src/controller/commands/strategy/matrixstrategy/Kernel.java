package controller.commands.strategy.matrixstrategy;

public interface Kernel {

  int getKernelSize();

  double getKernelValue(int h, int w);

  /**
   * Given a 2D matrix of integer, return the kernel matrix of the same size
   * that represents each element in the source.
   * @param source
   * @return
   */
  Kernel[][] kernelFilterMatrix(int[][] source);

  /**
   * Return the multiplication value of two kernels with same size
   * @param kernel
   * @return
   */
  double kernelMultiply(Kernel kernel);

  /**
   * Given an array of integer, return rendered array after scaling by the matrix
   * @param array
   * @return
   */
  double[] kernelScale(int[] array);

  void setKernel(int height, int width, double value);

  String toStringM();
}
