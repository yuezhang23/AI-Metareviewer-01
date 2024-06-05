package testmodel;

import org.junit.Before;
import org.junit.Test;
import model.Pixel;
import model.PixelImpl;
import static org.junit.Assert.assertEquals;

/**
 * This class tests all methods in the PixelImpl class that implements
 * both the Pixel and PixelState interfaces.
 */
public class TestPixel {
  private Pixel pixel;

  @Before
  public void setup() {
    pixel = new PixelImpl(105, 115, 125);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructorInvalidRedComponentNegInt() {
    new PixelImpl(-5, 255, 255);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructorInvalidRedComponentOutOfBound() {
    new PixelImpl(256, 255, 255);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructorInvalidGreenComponentNegInt() {
    new PixelImpl(255, -25, 255);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructorInvalidGreenComponentOutOfBound() {
    new PixelImpl(255, 256, 255);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructorInvalidBlueComponentNegInt() {
    new PixelImpl(255, 25, -255);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructorInvalidBlueComponentOutOfBound() {
    new PixelImpl(255, 255, 256);
  }

  @Test
  public void testGetRed() {
    assertEquals(105, pixel.getR());
  }

  @Test
  public void testGetGreen() {
    assertEquals(115, pixel.getG());
  }

  @Test
  public void testGetBlue() {
    assertEquals(125, pixel.getB());
  }

  @Test
  public void testSetRed() {
    assertEquals(105, pixel.getR());
    pixel.setR(10);
    assertEquals(10, pixel.getR());
  }

  @Test
  public void testSetGreen() {
    assertEquals(115, pixel.getG());
    pixel.setG(11);
    assertEquals(11, pixel.getG());
  }

  @Test
  public void testSetBlue() {
    assertEquals(125, pixel.getB());
    pixel.setB(12);
    assertEquals(12, pixel.getB());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetRed() {
    pixel.setR(256);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetRedNegInt() {
    pixel.setR(-5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetGreen() {
    pixel.setG(256);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetGreenNegInt() {
    pixel.setG(-5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetBlue() {
    pixel.setB(256);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetBlueNegInt() {
    pixel.setB(-5);
  }

}
