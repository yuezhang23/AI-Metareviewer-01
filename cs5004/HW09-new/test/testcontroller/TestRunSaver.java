package testcontroller;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Scanner;
import controller.ControllerImpl;
import model.ImageData;
import model.ImageDatabase;
import model.ImageState;
import view.View;
import view.ViewImpl;
import static org.junit.Assert.assertEquals;

/**
 * This class tests the RunSaver class that implements Command interface, together with
 * method in the ImageSaving class which implements the ImageSaver interface.
 */
public class TestRunSaver {
  private ImageDatabase modelMap;
  private View view;
  private StringBuilder log;

  @Before
  public void setup() {
    log = new StringBuilder();
    modelMap = new ImageData();
    view = new ViewImpl(log);
  }

  @Test
  // from PPM to PPM
  public void testSaveCommand() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten 150 mock1 mock1-brighter\nsavePPM res/mock1-br.ppm mock1-brighter\n");
    new ControllerImpl(modelMap, in, view).processing();
    view.renderImage(modelMap.getImage("mock1-brighter"));
    String expected =
            "(150, 160, 165) (175, 195, 175) (195, 255, 206) "
            + "(255, 182, 192) (255, 250, 255) (255, 250, 160) \n"
            + "(175, 175, 175) (195, 255, 206) (255, 182, 192) (255, 250, 255) "
            + "(255, 250, 160) (230, 240, 190) \n"
            + "(195, 255, 206) (255, 182, 192) (255, 250, 255) (255, 250, 160) "
            + "(250, 190, 200) (255, 175, 255) \n"
            + "(255, 182, 192) (255, 250, 255) (255, 250, 160) (150, 150, 150) "
            + "(175, 255, 175) (195, 255, 206) \n";
    assertEquals(expected, log.toString());

    // test file written to the hard drive
    try {
      Scanner sc = new Scanner(new FileInputStream("res/mock1-br.ppm"));
      StringBuilder builder = new StringBuilder();
      while (sc.hasNextLine()) {
        String s = sc.nextLine();
        if (s.charAt(0) != '#') {
          builder.append(s + System.lineSeparator());
        }
      }
      String expected1 = "P3\n6 4\n255\n150\n160\n165\n175\n195\n175\n195\n255\n206\n"
              + "255\n182\n192\n255\n250\n255\n255\n250\n160\n175\n175\n175\n"
              + "195\n255\n206\n255\n182\n192\n255\n250\n255\n255\n250\n160\n"
              + "230\n240\n190\n195\n255\n206\n255\n182\n192\n255\n250\n255\n"
              + "255\n250\n160\n250\n190\n200\n255\n175\n255\n255\n182\n192\n"
              + "255\n250\n255\n255\n250\n160\n150\n150\n150\n175\n255\n175\n"
              + "195\n255\n206\n";
      assertEquals(expected1, builder.toString());
    } catch (FileNotFoundException e) {
      System.out.println("File mock1-br.ppm not found!");
    }
  }


  @Test
  // save from PPM to PNG
  public void testPPMSaveToPNG() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten 150 mock1 mock1-brighter\nsavePPM res/mock1-br.ppm mock1-brighter\n"
            + "savePNG res/mock1-br.png mock1-brighter\n"
            + "loadPNG res/mock1-br.png mock1-brighter-png\n");
    new ControllerImpl(modelMap, in, view).processing();

    ImageState modelPPM = modelMap.getImage("mock1-brighter");
    ImageState modelPNG = modelMap.getImage("mock1-brighter-png");
    assertEquals(modelPPM.getHeight(), modelPNG.getHeight());
    assertEquals(modelPPM.getWidth(), modelPNG.getWidth());

    for (int i = 0; i < modelPPM.getHeight(); i++) {
      for (int j = 0; j < modelPNG.getWidth(); j++) {
        assertEquals(modelPPM.getRedChannel(i, j), modelPNG.getRedChannel(i, j));
        assertEquals(modelPPM.getGreenChannel(i, j), modelPNG.getGreenChannel(i, j));
        assertEquals(modelPPM.getBlueChannel(i, j), modelPNG.getBlueChannel(i, j));
      }
    }
  }

  @Test
  // save from PPM to JPG
  public void testPPMSaveToJPG() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "brighten 150 mock1 mock1-brighter\nsavePPM res/mock1-br.ppm mock1-brighter\n"
            + "saveJPG res/mock1-br.jpeg mock1-brighter\n");
    new ControllerImpl(modelMap, in, view).processing();

    // test file written to the hard drive is created
    StringReader in1 = new StringReader("loadJPG res/mock1-br.jpeg mock1-br-jpg\n");
    new ControllerImpl(modelMap, in1, view).processing();
    ImageState modelJPG = modelMap.getImage("mock1-br-jpg");
    assertEquals(4, modelJPG.getHeight());
    assertEquals(6, modelJPG.getWidth());
  }

  @Test
  // save from PNG to PPM ,JPG
  public void testPNGSaveToPPMAndJPG() {
    StringReader in = new StringReader("loadPNG res/treeflower.png fl\nsepia 3 fl fl-sp\n"
            + "savePPM res/treeflower-sepia.ppm fl-sp\n"
            + "savePNG res/treeflower-sepia.png fl-sp\n"
            + "saveJPG res/treeflower-sepia.jpeg fl-sp\n");
    new ControllerImpl(modelMap, in, view).processing();


    // test file written to the hard drive
    StringReader in1 = new StringReader("loadPPM res/treeflower-sepia.ppm fl-ppm\n"
            + "loadPNG res/treeflower-sepia.png fl-png\n"
            + "loadJPG res/treeflower-sepia.jpeg fl-jpg\n");
    new ControllerImpl(modelMap, in1, view).processing();

    ImageState modelPPM = modelMap.getImage("fl-ppm");
    ImageState modelPNG = modelMap.getImage("fl-png");
    ImageState modelJPG = modelMap.getImage("fl-jpg");
    assertEquals(modelJPG.getHeight(), modelPNG.getHeight());
    assertEquals(modelJPG.getWidth(), modelPNG.getWidth());

    for (int i = 0; i < modelPPM.getHeight(); i++) {
      for (int j = 0; j < modelPNG.getWidth(); j++) {
        assertEquals(modelPPM.getRedChannel(i, j), modelPNG.getRedChannel(i, j));
        assertEquals(modelPPM.getGreenChannel(i, j), modelPNG.getGreenChannel(i, j));
        assertEquals(modelPPM.getBlueChannel(i, j), modelPNG.getBlueChannel(i, j));
      }
    }
  }

  @Test
  // save from JPG to PPM, PNG
  public void testJPGSaveToPPMAndPNG() {
    StringReader in = new StringReader("loadJPG res/louie.jpeg lou\n"
            + "greyscale-red 3 lou lou-red\n"
            + "saveJPG res/red-louie.jpeg lou-red\n"
            + "savePNG res/red-louie.png lou-red\n"
            + "savePPM res/red-louie.ppm lou-red\n");
    new ControllerImpl(modelMap, in, view).processing();

    // test file written to the hard drive
    StringReader in1 = new StringReader("loadPPM res/red-louie.ppm lou-ppm\n" +
            "loadJPG res/red-louie.jpeg lou-jpg \nloadPNG res/red-louie.png lou-png \n");
    new ControllerImpl(modelMap, in1, view).processing();

    ImageState modelPPM = modelMap.getImage("lou-ppm");
    ImageState modelJPG = modelMap.getImage("lou-jpg");
    ImageState modelPNG = modelMap.getImage("lou-png");
    assertEquals(modelPPM.getHeight(), modelJPG.getHeight());
    assertEquals(modelPPM.getWidth(), modelJPG.getWidth());

    for (int i = 0; i < modelPPM.getHeight(); i++) {
      for (int j = 0; j < modelPNG.getWidth(); j++) {
        assertEquals(modelPPM.getRedChannel(i, j), modelPNG.getRedChannel(i, j));
        assertEquals(modelPPM.getGreenChannel(i, j), modelPNG.getGreenChannel(i, j));
        assertEquals(modelPPM.getBlueChannel(i, j), modelPNG.getBlueChannel(i, j));
      }
    }
  }

  @Test
  public void testInvalidSaveWrongID() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n" +
            "saveJPG res/mock1.jpeg mockingbird\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "image not found";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidSaveLackArgus2() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n" +
            "savePNG res/mock1.jpeg\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of argument 2";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidSaveLackArgus1() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n" +
            "savePNG\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of argument 1";
    assertEquals(expected, log.toString());
  }

}
