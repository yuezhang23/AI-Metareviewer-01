package testmodel;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import model.ImageData;
import model.ImageDatabase;
import model.ImageImpl;
import model.ImageState;
import model.PixelImpl;
import model.PixelState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * This class tests all methods in the ImageData class that implements
 * the ImageDataBase interface.
 */
public class TestDataBase {
  private ImageState image;
  private ImageState image1;
  private ImageDatabase dataMap;
  private ImageDatabase dataMapE;

  @Before
  public void setup() {
    PixelState c1 = new PixelImpl(80, 80, 180);
    PixelState c2 = new PixelImpl(125, 45, 100);
    PixelState c3 = new PixelImpl(0, 0, 0);
    PixelState[] row1 = new PixelState[] {c1, c2, c3};
    PixelState[][] matrix = new PixelState[][] {row1};
    image = new ImageImpl.Builder().setPixel(matrix).build();
    image1 = new ImageImpl.Builder().setPixel(matrix).build();
    Map<String, ImageState> data = new HashMap<>();
    data.put("m", image);
    dataMap = new ImageData.Builder().add(data).build();
    dataMapE = new ImageData();
  }

  @Test
  public void testConstructor() {
    assertNull(new ImageData().getImage("m"));
  }

  @Test
  public void testGetImage() {
    // empty database
    assertNull(dataMapE.getImage(null));
    assertNull(dataMapE.getImage("m"));

    //non-empty database
    assertNull(dataMap.getImage(null));
    assertEquals(image, dataMap.getImage("m"));
    assertNull(dataMap.getImage("mm"));
  }

  @Test
  public void testAddImage() {
    assertNull(dataMap.getImage("mm"));
    dataMap.addImage("mm", image1);
    assertEquals(image1, dataMap.getImage("mm"));
    dataMap.addImage("mmm", image1);
    assertEquals(image1, dataMap.getImage("mm"));
    assertEquals(image1, dataMap.getImage("mmm"));
    dataMap.addImage("mmm", image);
    assertEquals(image, dataMap.getImage("mmm"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddID() {
    dataMap.addImage(null, image1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddModel() {
    dataMap.addImage("mm", null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddNull() {
    dataMap.addImage(null, null);
  }

}
