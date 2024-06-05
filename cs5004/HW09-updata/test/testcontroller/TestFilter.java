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

public class TestFilter {
  private ImageDatabase modelMap;
  private StringBuilder log;
  private StringBuilder preLog;
  private View view;
  private View preView;

  @Before
  public void setup() {
    log = new StringBuilder();
    preLog = new StringBuilder();
    modelMap = new ImageData();
    view = new ViewImpl(log);
    preView = new ViewImpl(preLog);

  }

  @Test
  public void testBlurTwoTimes() {
    StringReader in = new StringReader("loadPPM images/mock.ppm mock\n"
            + "greyscale-red 3 mock mock-r\nblur 3 mock-r mock-blur\n"
            + "blur 3 mock-blur mock-blur2\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(9, modelMap.getImage("mock-blur").getWidth());
    assertEquals(3, modelMap.getImage("mock-blur").getHeight());

    preView.renderImage(modelMap.getImage("mock-r"));
    String expected_mock =
                      "(32, 32, 32) (16, 16, 16) (4, 4, 4) "
                    + "(32, 32, 32) (16, 16, 16) (4, 4, 4) "
                    + "(32, 32, 32) (16, 16, 16) (4, 4, 4) \n"
                    + "(48, 48, 48) (32, 32, 32) (8, 8, 8) "
                    + "(48, 48, 48) (32, 32, 32) (8, 8, 8) "
                    + "(48, 48, 48) (32, 32, 32) (8, 8, 8) \n"
                    + "(56, 56, 56) (0, 0, 0) (48, 48, 48) "
                    + "(56, 56, 56) (0, 0, 0) (48, 48, 48) "
                    + "(56, 56, 56) (0, 0, 0) (48, 48, 48) \n";
    assertEquals(expected_mock, preLog.toString());

    view.renderImage(modelMap.getImage("mock-blur"));
    view.renderImage(modelMap.getImage("mock-blur2"));
    String expected_blur2 =
                      // blur one time
                      "(18, 18, 18) (16, 16, 16) (13, 13, 13) (19, 19, 19) (16, 16, 16) "
                    + "(13, 13, 13) (19, 19, 19) (16, 16, 16) (6, 6, 6) \n"
                    + "(28, 28, 28) (26, 26, 26) (25, 25, 25) (32, 32, 32) (26, 26, 26) "
                    + "(25, 25, 25) (32, 32, 32) (26, 26, 26) (14, 14, 14) \n"
                    + "(22, 22, 22) (21, 21, 21) (25, 25, 25) (29, 29, 29) (21, 21, 21) "
                    + "(25, 25, 25) (29, 29, 29) (21, 21, 21) (15, 15, 15) \n"
                    // blur two times
                    + "(12, 12, 12) (14, 14, 14) (14, 14, 14) (16, 16, 16) (15, 15, 15) "
                    + "(14, 14, 14) (16, 16, 16) (13, 13, 13) (7, 7, 7) \n"
                    + "(18, 18, 18) (23, 23, 23) (24, 24, 24) (25, 25, 25) "
                    + "(24, 24, 24) (24, 24, 24) (25, 25, 25) (21, 21, 21) (12, 12, 12) \n"
                    + "(13, 13, 13) (18, 18, 18) (19, 19, 19) (20, 20, 20) (19, 19, 19) "
                    + "(19, 19, 19) (20, 20, 20) (17, 17, 17) (10, 10, 10) \n";
    assertEquals(expected_blur2, log.toString());
  }

  @Test
  public void testSharpenTwoTimes() {
    StringReader in = new StringReader("loadPPM images/mock.ppm mock\n"
            + "greyscale-red 3 mock mock-r\nsharpen 5 mock-r mock-sharp\n"
            + "sharpen 5 mock-sharp mock-sharp2\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(9, modelMap.getImage("mock-sharp2").getWidth());
    assertEquals(3, modelMap.getImage("mock-sharp2").getHeight());

    preView.renderImage(modelMap.getImage("mock-r"));
    String expected_mock =
            "(32, 32, 32) (16, 16, 16) (4, 4, 4) "
                    + "(32, 32, 32) (16, 16, 16) (4, 4, 4) "
                    + "(32, 32, 32) (16, 16, 16) (4, 4, 4) \n"
                    + "(48, 48, 48) (32, 32, 32) (8, 8, 8) "
                    + "(48, 48, 48) (32, 32, 32) (8, 8, 8) "
                    + "(48, 48, 48) (32, 32, 32) (8, 8, 8) \n"
                    + "(56, 56, 56) (0, 0, 0) (48, 48, 48) "
                    + "(56, 56, 56) (0, 0, 0) (48, 48, 48) "
                    + "(56, 56, 56) (0, 0, 0) (48, 48, 48) \n";
    assertEquals(expected_mock, preLog.toString());

    view.renderImage(modelMap.getImage("mock-sharp"));
    view.renderImage(modelMap.getImage("mock-sharp2"));
    String expected_sharp2 =
            // sharpen one time
            "(42, 42, 42) (17, 17, 17) (2, 2, 2) (33, 33, 33) (10, 10, 10) "
                   + "(2, 2, 2) (33, 33, 33) (27, 27, 27) (0, 0, 0) \n"
                   + "(75, 75, 75) (68, 68, 68) (44, 44, 44) (84, 84, 84) (61, 61, 61) "
                   + "(44, 44, 44) (84, 84, 84) (78, 78, 78) (16, 16, 16) \n"
                   + "(63, 63, 63) (25, 25, 25) (55, 55, 55) (70, 70, 70) (17, 17, 17) "
                   + "(55, 55, 55) (70, 70, 70) (34, 34, 34) (39, 39, 39) \n"
                    // sharpen two times
                   + "(58, 58, 58) (34, 34, 34) (11, 11, 11) (39, 39, 39) (12, 12, 12) "
                   + "(2, 2, 2) (54, 54, 54) (49, 49, 49) (0, 0, 0) \n"
                   + "(116, 116, 116) (125, 125, 125) (99, 99, 99) (131, 131, 131) (104, 104, 104) "
                   + "(86, 86, 86) (152, 152, 152) (141, 141, 141) (37, 37, 37) \n"
                   + "(85, 85, 85) (70, 70, 70) (88, 88, 88) (103, 103, 103) (54, 54, 54) "
                   + "(78, 78, 78) (118, 118, 118) (86, 86, 86) (44, 44, 44) \n";

    assertEquals(expected_sharp2, log.toString());
  }

  @Test
  public void testFilterMockKernel() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "filter 3 1 1 1 1 1 1 1 1 1 mock2 mock2-filter\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(5, modelMap.getImage("mock2-filter").getWidth());
    assertEquals(5, modelMap.getImage("mock2-filter").getHeight());

    preView.renderImage(modelMap.getImage("mock2"));
    String mock2 =
            "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n";
    assertEquals(mock2, preLog.toString());

    view.renderImage(modelMap.getImage("mock2-filter"));
    String expected =
            // sharpen one time
            "(40, 40, 40) (60, 60, 60) (60, 60, 60) (60, 60, 60) (40, 40, 40) \n" +
                    "(60, 60, 60) (90, 90, 90) (90, 90, 90) (90, 90, 90) (60, 60, 60) \n" +
                    "(60, 60, 60) (90, 90, 90) (90, 90, 90) (90, 90, 90) (60, 60, 60) \n" +
                    "(60, 60, 60) (90, 90, 90) (90, 90, 90) (90, 90, 90) (60, 60, 60) \n" +
                    "(40, 40, 40) (60, 60, 60) (60, 60, 60) (60, 60, 60) (40, 40, 40) \n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void testSharpenOnetime() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharpen 5 mock2 mock2-sharp\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(5, modelMap.getImage("mock2-sharp").getWidth());
    assertEquals(5, modelMap.getImage("mock2-sharp").getHeight());

    preView.renderImage(modelMap.getImage("mock2"));
    String mock2 =
            "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n";
    assertEquals(mock2, preLog.toString());

    view.renderImage(modelMap.getImage("mock2-sharp"));
    String expected =
            // sharpen one time
            "(11, 11, 11) (15, 15, 15) (11, 11, 11) (15, 15, 15) (11, 11, 11) \n" +
                    "(15, 15, 15) (21, 21, 21) (16, 16, 16) (21, 21, 21) (15, 15, 15) \n" +
                    "(11, 11, 11) (16, 16, 16) (10, 10, 10) (16, 16, 16) (11, 11, 11) \n" +
                    "(15, 15, 15) (21, 21, 21) (16, 16, 16) (21, 21, 21) (15, 15, 15) \n" +
                    "(11, 11, 11) (15, 15, 15) (11, 11, 11) (15, 15, 15) (11, 11, 11) \n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void testBlurOnetime() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "blur 3 mock2 mock2-blur\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(5, modelMap.getImage("mock2-blur").getWidth());
    assertEquals(5, modelMap.getImage("mock2-blur").getHeight());

    preView.renderImage(modelMap.getImage("mock2"));
    String mock2 =
            "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n" +
                    "(10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) (10, 10, 10) \n";
    assertEquals(mock2, preLog.toString());

    view.renderImage(modelMap.getImage("mock2-blur"));
    String expected =
            // blur one time
            "(6, 6, 6) (8, 8, 8) (8, 8, 8) (8, 8, 8) (6, 6, 6) \n" +
                    "(8, 8, 8) (10, 10, 10) (10, 10, 10) (10, 10, 10) (8, 8, 8) \n" +
                    "(8, 8, 8) (10, 10, 10) (10, 10, 10) (10, 10, 10) (8, 8, 8) \n" +
                    "(8, 8, 8) (10, 10, 10) (10, 10, 10) (10, 10, 10) (8, 8, 8) \n" +
                    "(6, 6, 6) (8, 8, 8) (8, 8, 8) (8, 8, 8) (6, 6, 6) \n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidFilterNumberNull() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharpen mock2 mock2-sharp\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing dimension size for pixel filter or transform\n"
           + "wrong input\n"
           + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidFilterNumberNeg() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharpen -5 mock2 mock2-sharp\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "dimension is not positive integer\n" +
            "wrong input\n" +
            "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidFilterNumberEven() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharpen 6 mock2 mock2-sharp\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "dimension is not an odd number\n" +
            "wrong input\n" +
            "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidFilterID() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharpen 5 mock2 \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of new model ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidFilterCommand() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "blur\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing dimension size for pixel filter or transform\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidFilterID1() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharpen 5 \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

  @Test  (expected = IllegalArgumentException.class)
  public void testInvalidBlurSize() {
    // fixed size of 3
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "blur 5 mock2 mock2-blur\n");
    new ControllerImpl(modelMap, in, view).processing();
  }

  @Test  (expected = IllegalArgumentException.class)
  public void testInvalidSharpenSize() {
    // fixed size of 5
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharpen 3 mock2 mock2-sharp\n");
    new ControllerImpl(modelMap, in, view).processing();
  }

  @Test
  public void testInvalidSharpenName() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharp 5 mock2 mock2-sharp\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\n" +
            "wrong input\n" +
            "wrong input\n" +
            "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlurName() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "blurring 3 mock2 mock2-sharp\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\n" +
            "wrong input\n" +
            "wrong input\n" +
            "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidSharpenOrder() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "sharpen mock2 5 mock2-sharp\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing dimension size for pixel filter or transform\n" +
            "wrong input\n" +
            "wrong input\n" +
            "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlurOrder() {
    StringReader in = new StringReader("loadPPM images/mock2.ppm mock2\n"
            + "mock2 mock2-blur blur 3\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\n" +
            "wrong input\n" +
            "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }
}
