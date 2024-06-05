package testmodel;

import org.junit.Before;
import org.junit.Test;
import model.Image;
import model.ImageImpl;
import model.PixelImpl;
import model.PixelState;
import static org.junit.Assert.assertEquals;

/**
 * This class tests all methods in the ImageImpl class that implements
 * both Image interface and ImageState interface.
 */
public class TestImageImpl {
  private Image image;
  private Image image1;

  @Before
  public void setup() {
    PixelState c01 = new PixelImpl(80, 80, 180);
    PixelState c02 = new PixelImpl(125, 45, 100);
    PixelState c03 = new PixelImpl(0, 0, 0);
    PixelState[] row1 = new PixelState[] {c01, c02, c03};

    PixelState c11 = new PixelImpl(10, 10, 80);
    PixelState c12 = new PixelImpl(125, 145, 10);
    PixelState c13 = new PixelImpl(255, 255, 255);
    PixelState[] row2 = new PixelState[] {c11, c12, c13};

    PixelState c21 = new PixelImpl(220, 180, 20);
    PixelState c22 = new PixelImpl(25, 245, 10);
    PixelState c23 = new PixelImpl(40, 40, 40);
    PixelState[] row3 = new PixelState[] {c21, c22, c23};

    PixelState c31 = new PixelImpl(80, 150, 20);
    PixelState c32 = new PixelImpl(25, 45, 100);
    PixelState c33 = new PixelImpl(150, 50, 150);
    PixelState[] row4 = new PixelState[] {c31, c32, c33};

    PixelState[][] matrix = new PixelState[][] {row1, row2, row3, row4};
    image = new ImageImpl.Builder().setPixel(matrix).build();
    image1 = new ImageImpl(5, 6);
  }

  @Test
  public void testConstructorNonEmptyModel() {
    assertEquals(4, image.getHeight());
    assertEquals(3, image.getWidth());
    assertEquals(150, image.getRedChannel(3, 2));
    assertEquals(50, image.getGreenChannel(3, 2));
    assertEquals(150, image.getBlueChannel(3, 2));
  }

  @Test
  public void testConstructorEmptyModel() {
    assertEquals(5, image1.getHeight());
    assertEquals(6, image1.getWidth());
  }

  @Test
  public void testGetRedChannel() {
    assertEquals(80, image.getRedChannel(0, 0));
    assertEquals(40, image.getRedChannel(2, 2));
  }

  @Test
  public void testGetGreenChannel() {
    assertEquals(80, image.getGreenChannel(0, 0));
    assertEquals(40, image.getGreenChannel(2, 2));
  }

  @Test
  public void testGetBlueChannel() {
    assertEquals(180, image.getBlueChannel(0, 0));
    assertEquals(40, image.getBlueChannel(2, 2));
  }

  @Test
  public void testGetWidth() {
    assertEquals(3, image.getWidth());
  }

  @Test
  public void testGetHeight() {
    assertEquals(4, image.getHeight());
  }

  @Test
  public void testSetPixel() {
    assertEquals(80, image.getRedChannel(0, 0));
    assertEquals(80, image.getGreenChannel(0, 0));
    assertEquals(180, image.getBlueChannel(0, 0));
    image.setPixel(0, 0, 30, 130, 230);
    assertEquals(30, image.getRedChannel(0, 0));
    assertEquals(130, image.getGreenChannel(0, 0));
    assertEquals(230, image.getBlueChannel(0, 0));

    image1.setPixel(0, 0, 30, 130, 230);
    assertEquals(30, image1.getRedChannel(0, 0));
    assertEquals(130, image1.getGreenChannel(0, 0));
    assertEquals(230, image1.getBlueChannel(0, 0));
  }

  @Test (expected = IllegalStateException.class)
  public void testInvalidConstructorHeightOutOfBound() {
    // non-empty model
    image.getRedChannel(4, 2);
  }

  @Test (expected = IllegalStateException.class)
  public void testInvalidConstructorWidthOutOfBound() {
    // non-empty model
    image.getRedChannel(3, 3);
  }

  @Test (expected = IllegalStateException.class)
  public void testInvalidConstructorNullRedChannel() {
    // empty model
    image1.getRedChannel(2, 2);
  }

  @Test (expected = IllegalStateException.class)
  public void testInvalidConstructorNullGreenChannel() {
    // empty model
    image1.getGreenChannel(2, 2);
  }

  @Test (expected = IllegalStateException.class)
  public void testInvalidConstructorNullBlueChannel() {
    // empty model
    image1.getBlueChannel(2, 2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstructorZero() {
    new ImageImpl(0, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstructorNegativeSize() {
    new ImageImpl(5, -1);
  }

  @Test (expected = IllegalStateException.class)
  public void testInvalidSetPixelHeightOutOfBound() {
    // non-empty model
    image.setPixel(4, 2, 10, 10, 10);
  }

  @Test (expected = IllegalStateException.class)
  public void testInvalidSetPixelWidthOutOfBound() {
    // non-empty model
    image.setPixel(3, 3, 10, 10, 10);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetPixelRedInvalid() {
    // non-empty model
    image.setPixel(3, 2, 256, 10, 10);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetPixelGreenInvalid() {
    // non-empty model
    image.setPixel(3, 2, 26, 256, 10);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetPixelBlueInvalid() {
    // non-empty model
    image.setPixel(3, 2, 26, 10, 256);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetPixelRedInvalidNegative() {
    // non-empty model
    image.setPixel(3, 2, -25, 10, 10);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetPixelGreenInvalidNegative() {
    // non-empty model
    image.setPixel(3, 2, 25, -10, 10);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSetPixelBlueInvalidNegative() {
    // non-empty model
    image.setPixel(3, 2, 25, 10, -10);
  }
}
