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

/**
 * This class tests the RunLoader class that implements Command interface, together with
 * method in the ImageLoading class which implements the ImageLoader interface.
 */
public class TestRunLoader {
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
  public void testLoadCommand() {
    StringReader in = new StringReader("loadPPM images/flowers.ppm flower");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(279, modelMap.getImage("flower").getWidth());
    assertEquals(652, modelMap.getImage("flower").getHeight());
    assertEquals(0, modelMap.getImage("flower").getRedChannel(0, 0));
    assertEquals(255, modelMap.getImage("flower").getGreenChannel(0, 0));
    assertEquals(0, modelMap.getImage("flower").getBlueChannel(0, 0));
  }

  @Test
  public void testLoadCommandSmallerImage() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1").getWidth());
    assertEquals(4, modelMap.getImage("mock1").getHeight());

    view.renderImage(modelMap.getImage("mock1"));
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
    assertEquals(6, modelMap.getImage("mock1").getWidth());
    assertEquals(4, modelMap.getImage("mock1").getHeight());
    assertEquals(113, modelMap.getImage("mock1").getRedChannel(2, 1));
    assertEquals(32, modelMap.getImage("mock1").getGreenChannel(2, 1));
    assertEquals(42, modelMap.getImage("mock1").getBlueChannel(2, 1));
    assertEquals(45, modelMap.getImage("mock1").getRedChannel(0, 2));
    assertEquals(124, modelMap.getImage("mock1").getGreenChannel(0, 2));
    assertEquals(56, modelMap.getImage("mock1").getBlueChannel(0, 2));
  }

  @Test
  public void testLoadOverwrite() {
    StringReader in = new StringReader("loadPPM images/flowers.ppm fl\n"
            + "loadPPM images/mock1.ppm mock1\n"
            + "loadPPM images/mock1.ppm fl\n");
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

  @Test
  public void testInvalidLoadWrongFilePath() {
    StringReader in = new StringReader("loadPPM images/mocking.ppm mock1");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "File images/mocking.ppm not found!\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLoadCommandIDNotFound() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm ");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of argument 2\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLoadCommandPathNotFound() {
    StringReader in = new StringReader("loadPPM ");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of argument 1\n";
    assertEquals(expected, log.toString());
  }

  @Test
  // wrong order in command line
  public void testInvalidLoadWrongOrderOfCommandName() {
    StringReader in = new StringReader("images/mock1.ppm loadPPM mock1");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\n"
            + "missing command data of argument 2\n";
    assertEquals(expected, log.toString());
  }

  @Test
  // wrong order in command line
  public void testInvalidLoadInWrongOrder() {
    StringReader in = new StringReader("loadPPM mock1 images/mock1.ppm");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "File mock1 not found!\n";
    assertEquals(expected, log.toString());
  }

  @Test
  // wrong spelling of command name
  public void testInvalidLoadCommandName() {
    StringReader in = new StringReader("loading images/mock1.ppm mock1");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\n"
            + "wrong input\n"
            + "wrong input\n";
    assertEquals(expected, log.toString());
  }

}
