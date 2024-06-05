package testcontroller;

import org.junit.Before;
import org.junit.Test;
import controller.commands.strategy.matrixstrategy.Kernel;
import controller.commands.strategy.matrixstrategy.KernelImpl;
import static org.junit.Assert.assertEquals;

/**
 * This class tests all methods in the KernelImpl class that implements
 * both the Kernel interface.
 */
public class TestKernel {
  private Kernel kernel;
  private Kernel kernel1;
  private int[] arr;

  @Before
  public void setup() {
    kernel1 = new KernelImpl(3);
    kernel1.setKernel(0, 0, 2);
    kernel1.setKernel(0, 1, 2);
    kernel1.setKernel(0, 2, 2);
    kernel1.setKernel(1, 0, 2);
    kernel1.setKernel(1, 1, 2);
    kernel1.setKernel(1, 2, 2);
    kernel1.setKernel(2, 0, 2);
    kernel1.setKernel(2, 1, 2);
    kernel1.setKernel(2, 2, 2);
    kernel = new KernelImpl(3);
    kernel.setKernel(0, 0, 2);
    kernel.setKernel(0, 1, 1.5);
    kernel.setKernel(0, 2, 2);
    kernel.setKernel(1, 0, 1.5);
    kernel.setKernel(1, 1, 2);
    kernel.setKernel(1, 2, 1.5);
    kernel.setKernel(2, 0, 2);
    kernel.setKernel(2, 1, 1);
    kernel.setKernel(2, 2, 2);
    arr = new int[] {10, 20, 30};
  }

  @Test
  public void testGetSize() {
    assertEquals(3, kernel1.getKernelSize());
  }

  @Test
  public void testGetKernelValue() {
    assertEquals(1.5, kernel.getKernelValue(1, 2), 0.01);
    assertEquals(1, kernel.getKernelValue(2, 1), 0.01);
  }

  @Test
  public void testSetKernelValue() {
    assertEquals(1.5, kernel.getKernelValue(1, 2), 0.01);
    kernel.setKernel(1, 2, 0.5);
    assertEquals(0.5, kernel.getKernelValue(1, 2), 0.01);
  }

  @Test
  public void testFilterMatrix() {
    int[][] source = new int[][] {{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1},};
    int size = kernel.getKernelSize();
    Kernel[][] sourceMatrix = kernel.kernelFilterMatrix(source);
    String expected =
              "0.0 0.0 0.0 \n"
            + "0.0 1.0 1.0 \n"
            + "0.0 1.0 1.0 \n";
    assertEquals(expected, sourceMatrix[0][0].toStringM());
    String expected1 =
            "1.0 1.0 1.0 \n"
          + "1.0 1.0 1.0 \n"
          + "1.0 1.0 1.0 \n";
    assertEquals(expected1, sourceMatrix[2][2].toStringM());
    String expected2 =
              "1.0 1.0 0.0 \n"
            + "1.0 1.0 0.0 \n"
            + "0.0 0.0 0.0 \n";
    assertEquals(expected2, sourceMatrix[3][3].toStringM());
    assertEquals(4, sourceMatrix.length);
    assertEquals(4, sourceMatrix[0].length);
  }

  @Test
  public void testMultiplication() {
    assertEquals(31.0, kernel.kernelMultiply(kernel1), 0.01);
  }

  @Test
  public void testScale() {
    double[] result = kernel1.kernelScale(arr);
    int sum = arr[0] + arr[1] + arr[2];
    for (int i = 0; i < arr.length; i++) {
      assertEquals(sum * 2, result[i], 0.01);
    }
  }
}
