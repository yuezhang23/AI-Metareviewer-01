package controller.commands.strategy.matrixstrategy;

/**
 * This represents Kernel class that include a collection of matrix multiplication,
 * scaling on array by a matrix, set and get specific value on a kernel, creating
 * a kernel matrix by filtering rules of image transformation, etc.
 */
public interface Kernel {

  /**
   * Get the size of the kernel square.
   * @return kernel size
   */
  int getKernelSize();

  /**
   * Get value stored in the kernel at given position of the matrix.
   * @param h the x direction position of the matrix
   * @param w the y direction position of the matrix
   * @return stored value
   */
  double getKernelValue(int h, int w);

  /**
   * Given a 2D matrix of integers as source, return the kernel matrix
   * that represents a filtering related kernel for each value in the source.
   * @param source the given matrix of integers
   * @return the kernel matrix
   */
  Kernel[][] kernelFilterMatrix(int[][] source);

  /**
   * Return the multiplication value of two kernels with same size.
   * @param kernel the other kernel of same size
   * @return double value after mathematical matrix multiplication
   */
  double kernelMultiply(Kernel kernel);

  /**
   * Given an array of integer, return rendered array after scaling by the kernel matrix.
   * @param array the array to be rendered
   * @return a new array of doubles
   */
  double[] kernelScale(int[] array);

  /**
   * Set value at given position of the kernel matrix.
   * @param height the x-dir of the matrix
   * @param width the y-dir of the matrix
   * @param value new value
   */
  void setKernel(int height, int width, double value);

  /**
   * Return a specific representation of the kernel matrix.
   * @return a specific matrix
   */
  String toStringM();
}
