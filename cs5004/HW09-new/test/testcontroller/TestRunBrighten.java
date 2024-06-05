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
 * This class tests the RunBrighten class that implements Command interface, together with
 * method in the BrightenTransform class which implements the ImageTransform interface.
 */
public class TestRunBrighten {
  private ImageDatabase modelMap;
  private StringBuilder log;
  private StringBuilder preLog;
  private View view;
  private View preview;

  @Before
  public void setup() {
    log = new StringBuilder();
    preLog = new StringBuilder();
    modelMap = new ImageData();
    view = new ViewImpl(log);
    preview = new ViewImpl(preLog);
  }

  @Test
  public void testBrightenCommand() {
    StringReader in = new StringReader("loadPPM res/flowers.ppm flower\n"
            + "brighten 40 flower flower-brighter\n");
    new ControllerImpl(modelMap, in, view).processing();

    assertEquals(modelMap.getImage("flower-brighter").getWidth(),
            modelMap.getImage("flower").getWidth());
    assertEquals(modelMap.getImage("flower-brighter").getHeight(),
            modelMap.getImage("flower").getHeight());
    assertEquals(modelMap.getImage("flower-brighter").getRedChannel(0, 0),
            40 + modelMap.getImage("flower").getRedChannel(0, 0));
    assertEquals(modelMap.getImage("flower-brighter").getGreenChannel(0, 0),
            modelMap.getImage("flower").getGreenChannel(0, 0));
    assertEquals(modelMap.getImage("flower-brighter").getBlueChannel(0, 0),
            40 + modelMap.getImage("flower").getBlueChannel(0, 0));
  }

  @Test
  public void testBrightenCommandSmallerImage() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten 150 mock1 mock1-brighter\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-brighter").getWidth());
    assertEquals(4, modelMap.getImage("mock1-brighter").getHeight());

    // image display before greyscale is made
    preview.renderImage(modelMap.getImage("mock1"));
    String expected_mock1 =
              "(0, 10, 15) (25, 45, 25) (45, 124, 56) "
            + "(113, 32, 42) (150, 100, 255) (180, 100, 10) \n"
            + "(25, 25, 25) (45, 124, 56) (113, 32, 42) (150, 100, 255) "
            + "(180, 100, 10) (80, 90, 40) \n"
            + "(45, 124, 56) (113, 32, 42) (150, 100, 255) (180, 100, 10) "
            + "(100, 40, 50) (125, 25, 235) \n"
            + "(113, 32, 42) (150, 100, 255) (180, 100, 10) (0, 0, 0) "
            + "(25, 150, 25) (45, 124, 56) \n";
    assertEquals(expected_mock1, preLog.toString());

    // image display after brighten greyscale is made
    // clamp each component within 0 and 255
    view.renderImage(modelMap.getImage("mock1-brighter"));
    String expected_mock1_brighter =
                "(150, 160, 165) (175, 195, 175) (195, 255, 206) "
              + "(255, 182, 192) (255, 250, 255) (255, 250, 160) \n"
              + "(175, 175, 175) (195, 255, 206) (255, 182, 192) (255, 250, 255) "
              + "(255, 250, 160) (230, 240, 190) \n"
              + "(195, 255, 206) (255, 182, 192) (255, 250, 255) (255, 250, 160) "
              + "(250, 190, 200) (255, 175, 255) \n"
              + "(255, 182, 192) (255, 250, 255) (255, 250, 160) (150, 150, 150) "
              + "(175, 255, 175) (195, 255, 206) \n";
    assertEquals(expected_mock1_brighter, log.toString());
  }

  @Test
  public void testDarkenCommand() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten -50 mock1 mock1-dr\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-dr").getWidth());
    assertEquals(4, modelMap.getImage("mock1-dr").getHeight());

    // image display before greyscale is made
    preview.renderImage(modelMap.getImage("mock1"));
    String expected_mock1 =
                      "(0, 10, 15) (25, 45, 25) (45, 124, 56) "
                    + "(113, 32, 42) (150, 100, 255) (180, 100, 10) \n"
                    + "(25, 25, 25) (45, 124, 56) (113, 32, 42) (150, 100, 255) "
                    + "(180, 100, 10) (80, 90, 40) \n"
                    + "(45, 124, 56) (113, 32, 42) (150, 100, 255) (180, 100, 10) "
                    + "(100, 40, 50) (125, 25, 235) \n"
                    + "(113, 32, 42) (150, 100, 255) (180, 100, 10) (0, 0, 0) "
                    + "(25, 150, 25) (45, 124, 56) \n";
    assertEquals(expected_mock1, preLog.toString());

    view.renderImage(modelMap.getImage("mock1-dr"));
    // image display after darken greyscale is made
    // each component is smaller than 255 but no less than 0
    String expected =
              "(0, 0, 0) (0, 0, 0) (0, 74, 6) "
            + "(63, 0, 0) (100, 50, 205) (130, 50, 0) \n"
            + "(0, 0, 0) (0, 74, 6) (63, 0, 0) (100, 50, 205) "
            + "(130, 50, 0) (30, 40, 0) \n"
            + "(0, 74, 6) (63, 0, 0) (100, 50, 205) (130, 50, 0) "
            + "(50, 0, 0) (75, 0, 185) \n"
            + "(63, 0, 0) (100, 50, 205) (130, 50, 0) (0, 0, 0) "
            + "(0, 100, 0) (0, 74, 6) \n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testDarkenCommandBiggerImage() {
    StringReader in = new StringReader("loadPPM res/flowers.ppm fl\n"
            + "brighten -30 fl fl-darker\n");
    new ControllerImpl(modelMap, in, view).processing();

    assertEquals(modelMap.getImage("fl-darker").getWidth(),
            modelMap.getImage("fl").getWidth());
    assertEquals(modelMap.getImage("fl-darker").getHeight(),
            modelMap.getImage("fl").getHeight());

    // clamp each component within 0 and 255
    assertEquals(0, modelMap.getImage("fl-darker").getRedChannel(0, 0));
    assertEquals(225, modelMap.getImage("fl-darker").getGreenChannel(0, 0));
    assertEquals(0, modelMap.getImage("fl-darker").getBlueChannel(0, 0));
    assertEquals(0, modelMap.getImage("fl-darker").getRedChannel(0, 6));
    assertEquals(225, modelMap.getImage("fl-darker").getGreenChannel(0, 6));
    assertEquals(0, modelMap.getImage("fl-darker").getBlueChannel(0, 6));
  }

  @Test
  public void testInvalidBrightenCommandInvalidSourceID() {
    StringReader in = new StringReader("brighten -100 mock1 mock1-darker\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "source model is not found\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandIntegerNotFound() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten  mock1 mock1-darker\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing integer for increment\nwrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandInvalidNegativeInteger() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten -300 mock1 mock1-darker\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "increment is not valid\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandInvalidPositiveInteger() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten 300 mock1 mock1-darker\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "increment is not valid\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandIncrementNotFound() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten  \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing integer for increment\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandSourceIDNotFound() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten 100 \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandDestIDNotFound() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten 100 mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of new model ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandName() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighter 100 mock1 mock1-brighter\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\n"
            + "wrong input\nwrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandWrongOrderInt() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "100 brighten mock1 mock1-brighter\n");
    // integer in passed before command
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\n"
            + "missing integer for increment\n"
            + "wrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandWrongOrderID() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten 100 mock1-brighter mock1\n");
    // integer in passed before command
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "source model is not found\n"
            + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBrightenCommandWrongOrder() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten mock1 100 mock1-brighter\n");
    new ControllerImpl(modelMap, in, view).processing();
    // integer put in the wrong order can't be recognized
    String expected = "missing integer for increment\n"
            + "wrong input\n"
            + "wrong input\n"
            + "wrong input\n";
    assertEquals(expected, log.toString());
  }
}
