package testcontroller;


import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;

import controller.ControllerImpl;
import model.ImageData;
import model.ImageDatabase;
import view.View;
import view.ViewImpl;

import static org.junit.Assert.assertEquals;

public class TestLoad {
  private ImageDatabase modelMap;
  private StringBuilder log;
  private View view;

  @Before
  public void setup() {
    log = new StringBuilder();
    modelMap = new ImageData();
    view = new ViewImpl(log);
  }

  @Test
  public void testLoadPngSmallerImage() {
    StringReader in = new StringReader("loadPNG images/mock1.png mock1-pg");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-pg").getWidth());
    assertEquals(4, modelMap.getImage("mock1-pg").getHeight());
    view.renderImage(modelMap.getImage("mock1-pg"));

    // image display by view after loading
    String expected =
            "(0, 10, 15) (25, 45, 25) (45, 124, 56) (113, 32, 42) "
                    + "(150, 100, 255) (180, 100, 10) \n"
                    + "(25, 25, 25) (45, 124, 56) (113, 32, 42) (150, 100, 255) "
                    + "(180, 100, 10) (80, 90, 40) \n"
                    + "(45, 124, 56) (113, 32, 42) (150, 100, 255) (180, 100, 10) "
                    + "(100, 40, 50) (125, 25, 235) \n"
                    + "(113, 32, 42) (150, 100, 255) (180, 100, 10) (0, 0, 0) "
                    + "(25, 150, 25) (45, 124, 56) \n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testLoadJpgImage() {
    StringReader in = new StringReader("loadJPG images/mock1-br.jpeg mock1-j");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-j").getWidth());
    assertEquals(4, modelMap.getImage("mock1-j").getHeight());
  }

  @Test
  public void testLoadOverwrite() {
    StringReader in = new StringReader("loadPNG images/treeflower.png fl\n"
            + "loadPNG images/mock1.png mock1\n"
            + "loadPNG images/mock1.png fl\n");
    new ControllerImpl(modelMap, in, view).processing();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 6; j++) {
        assertEquals(modelMap.getImage("fl").getRedChannel(i, j),
                modelMap.getImage("mock1").getRedChannel(i, j));
        assertEquals(modelMap.getImage("fl").getGreenChannel(i, j),
                modelMap.getImage("mock1").getGreenChannel(i, j));
        assertEquals(modelMap.getImage("fl").getBlueChannel(i, j),
                modelMap.getImage("mock1").getBlueChannel(i, j));
      }
    }
  }
}
