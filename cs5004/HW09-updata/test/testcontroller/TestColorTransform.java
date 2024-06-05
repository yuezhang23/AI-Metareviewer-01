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

public class TestColorTransform {
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
  public void testSepiaPPM() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "sepia 3 mock1 mock1-sepia\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-sepia").getWidth());
    assertEquals(4, modelMap.getImage("mock1-sepia").getHeight());

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

    view.renderImage(modelMap.getImage("mock1-sepia"));
    String expected_mock1_green =
                      "(11, 9, 7) (49, 44, 34) (124, 110, 86) " +
                              "(77, 68, 53) (184, 164, 128) (150, 133, 104) \n" +
                              "(34, 30, 23) (124, 110, 86) (77, 68, 53) " +
                              "(184, 164, 128) (150, 133, 104) (108, 96, 75) \n" +
                              "(124, 110, 86) (77, 68, 53) (184, 164, 128) " +
                              "(150, 133, 104) (80, 71, 55) (113, 100, 78) \n" +
                              "(77, 68, 53) (184, 164, 128) (150, 133, 104) " +
                              "(0, 0, 0) (130, 116, 90) (124, 110, 86) \n";
    assertEquals(expected_mock1_green, log.toString());
  }

  @Test
  public void testInvalidSepiaPPM() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "sepia mock1 mock1-sepia\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "missing dimension size for pixel filter or transform\n"
                    + "wrong input\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testSepiaPNG() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "savePNG images/mock1.png mock1\nloadPNG images/mock1.png mock1-pg\n"
            + "sepia 3 mock1-pg mock1-pg-sepia\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-pg-sepia").getWidth());
    assertEquals(4, modelMap.getImage("mock1-pg-sepia").getHeight());

    preview.renderImage(modelMap.getImage("mock1-pg"));

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

    view.renderImage(modelMap.getImage("mock1-pg-sepia"));
    String expected_mock1_green =
            "(11, 9, 7) (49, 44, 34) (124, 110, 86) " +
                    "(77, 68, 53) (184, 164, 128) (150, 133, 104) \n" +
                    "(34, 30, 23) (124, 110, 86) (77, 68, 53) " +
                    "(184, 164, 128) (150, 133, 104) (108, 96, 75) \n" +
                    "(124, 110, 86) (77, 68, 53) (184, 164, 128) " +
                    "(150, 133, 104) (80, 71, 55) (113, 100, 78) \n" +
                    "(77, 68, 53) (184, 164, 128) (150, 133, 104) " +
                    "(0, 0, 0) (130, 116, 90) (124, 110, 86) \n";
    assertEquals(expected_mock1_green, log.toString());
  }

  @Test
  public void testRunLumaMatrix() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "luma-component mock1 mock1-luma\ngreyscale-luma 3 mock1 mock1-lumaMatrix");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-lumaMatrix").getWidth());
    assertEquals(4, modelMap.getImage("mock1-lumaMatrix").getHeight());

    view.renderImage(modelMap.getImage("mock1-lumaMatrix"));
    String expected_luma =
            "(8, 8, 8) (39, 39, 39) (102, 102, 102) (50, 50, 50) (122, 122, 122) (111, 111, 111) \n" +
                    "(25, 25, 25) (102, 102, 102) (50, 50, 50) (122, 122, 122) (111, 111, 111) (84, 84, 84) \n" +
                    "(102, 102, 102) (50, 50, 50) (122, 122, 122) (111, 111, 111) (53, 53, 53) (61, 61, 61) \n" +
                    "(50, 50, 50) (122, 122, 122) (111, 111, 111) (0, 0, 0) (114, 114, 114) (102, 102, 102) \n";
    assertEquals(expected_luma, log.toString());
    preview.renderImage(modelMap.getImage("mock1-luma"));
    assertEquals(preLog.toString(), log.toString());
  }

  @Test
  public void testRunIntensityCommand() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "intensity-component mock1 mock1-intense\n"
            + "greyscale-intensity 3 mock1 mock1-intenseMatrix\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-intenseMatrix").getWidth());
    assertEquals(4, modelMap.getImage("mock1-intenseMatrix").getHeight());


    view.renderImage(modelMap.getImage("mock1-intenseMatrix"));
    String expected_intense =
            "(8, 8, 8) (32, 32, 32) (75, 75, 75) (62, 62, 62) (168, 168, 168) (97, 97, 97) \n" +
                    "(25, 25, 25) (75, 75, 75) (62, 62, 62) (168, 168, 168) (97, 97, 97) (70, 70, 70) \n" +
                    "(75, 75, 75) (62, 62, 62) (168, 168, 168) (97, 97, 97) (63, 63, 63) (128, 128, 128) \n" +
                    "(62, 62, 62) (168, 168, 168) (97, 97, 97) (0, 0, 0) (67, 67, 67) (75, 75, 75) \n";
    assertEquals(expected_intense, log.toString());
    preview.renderImage(modelMap.getImage("mock1-intense"));
    assertEquals(preLog.toString(), log.toString());
  }

  @Test
  public void testInvalidGreyScale() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "greyscale-intensity mock1 mock1-intense\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "missing dimension size for pixel filter or transform\n"
                    + "wrong input\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidSepiaSize() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "sepia 4 mock1 mock1-intense\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "dimension is not an odd number\n" +
                    "wrong input\n" +
                    "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidSepiaNeg() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "sepia -3 mock1 mock1-intense\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "dimension is not positive integer\n" +
                    "wrong input\n" +
                    "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidSepiaName() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "sepia-change 4 mock1 mock1-intense\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n" +
                    "wrong input\n" +
                    "wrong input\n" +
                    "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testRunRedCommand() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "red-component mock1 mock1-red\n"
            + "greyscale-red 3 mock1 mock1-redMatrix\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-redMatrix").getWidth());
    assertEquals(4, modelMap.getImage("mock1-redMatrix").getHeight());

    view.renderImage(modelMap.getImage("mock1-redMatrix"));
    // image display after render red
    String expected_mock1_red =
            "(0, 0, 0) (25, 25, 25) (45, 45, 45) "
                    + "(113, 113, 113) (150, 150, 150) (180, 180, 180) \n"
                    + "(25, 25, 25) (45, 45, 45) (113, 113, 113) (150, 150, 150) "
                    + "(180, 180, 180) (80, 80, 80) \n"
                    + "(45, 45, 45) (113, 113, 113) (150, 150, 150) (180, 180, 180) "
                    + "(100, 100, 100) (125, 125, 125) \n"
                    + "(113, 113, 113) (150, 150, 150) (180, 180, 180) "
                    + "(0, 0, 0) (25, 25, 25) (45, 45, 45) \n";
    assertEquals(expected_mock1_red, log.toString());
    preview.renderImage(modelMap.getImage("mock1-red"));
    assertEquals(preLog.toString(), log.toString());
  }

  @Test
  public void testRunGreenCommand() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "green-component mock1 mock1-green\n"
            + "greyscale-green 3 mock1 mock1-greenMatrix\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-greenMatrix").getWidth());
    assertEquals(4, modelMap.getImage("mock1-green").getHeight());


    view.renderImage(modelMap.getImage("mock1-greenMatrix"));
    String expected_mock1_green =
            "(10, 10, 10) (45, 45, 45) (124, 124, 124) "
                    + "(32, 32, 32) (100, 100, 100) (100, 100, 100) \n"
                    + "(25, 25, 25) (124, 124, 124) (32, 32, 32) (100, 100, 100) "
                    + "(100, 100, 100) (90, 90, 90) \n"
                    + "(124, 124, 124) (32, 32, 32) (100, 100, 100) (100, 100, 100) "
                    + "(40, 40, 40) (25, 25, 25) \n"
                    + "(32, 32, 32) (100, 100, 100) (100, 100, 100) "
                    + "(0, 0, 0) (150, 150, 150) (124, 124, 124) \n";
    assertEquals(expected_mock1_green, log.toString());

    preview.renderImage(modelMap.getImage("mock1-green"));
    assertEquals(preLog.toString(), log.toString());
  }

  @Test
  public void testRunBlueCommand() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "blue-component mock1 mock1-blue\n"
            + "greyscale-blue 3 mock1 mock1-blueMatrix\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-blueMatrix").getWidth());
    assertEquals(4, modelMap.getImage("mock1-blueMatrix").getHeight());


    view.renderImage(modelMap.getImage("mock1-blueMatrix"));
    String expected_mock1_blue =
            "(15, 15, 15) (25, 25, 25) (56, 56, 56) "
                    + "(42, 42, 42) (255, 255, 255) (10, 10, 10) \n"
                    + "(25, 25, 25) (56, 56, 56) (42, 42, 42) (255, 255, 255) "
                    + "(10, 10, 10) (40, 40, 40) \n"
                    + "(56, 56, 56) (42, 42, 42) (255, 255, 255) (10, 10, 10) "
                    + "(50, 50, 50) (235, 235, 235) \n"
                    + "(42, 42, 42) (255, 255, 255) (10, 10, 10) "
                    + "(0, 0, 0) (25, 25, 25) (56, 56, 56) \n";
    assertEquals(expected_mock1_blue, log.toString());
    preview.renderImage(modelMap.getImage("mock1-blue"));
    assertEquals(preLog.toString(), log.toString());
  }
}
