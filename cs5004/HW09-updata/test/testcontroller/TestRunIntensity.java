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
 * This class tests the RunIntensity class that implements ImageTransform interface
 * which extends the Command interface.
 */
public class TestRunIntensity {
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
  public void testRunIntensityCommand() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "intensity-component mock1 mock1-intense\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-intense").getWidth());
    assertEquals(4, modelMap.getImage("mock1-intense").getHeight());

    preView.renderImage(modelMap.getImage("mock1"));
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

    view.renderImage(modelMap.getImage("mock1-intense"));
    String expected_mock1_intense =
                      "(8, 8, 8) (32, 32, 32) (75, 75, 75) (62, 62, 62) (168, 168, 168) (97, 97, 97) \n" +
                              "(25, 25, 25) (75, 75, 75) (62, 62, 62) (168, 168, 168) (97, 97, 97) (70, 70, 70) \n" +
                              "(75, 75, 75) (62, 62, 62) (168, 168, 168) (97, 97, 97) (63, 63, 63) (128, 128, 128) \n" +
                              "(62, 62, 62) (168, 168, 168) (97, 97, 97) (0, 0, 0) (67, 67, 67) (75, 75, 75) \n";
    assertEquals(expected_mock1_intense, log.toString());
  }

  @Test
  public void testInvalidIntenseCommandRenderBeforeLoading() {
    StringReader in = new StringReader("intensity-component mock1 mock1-intensity\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidIntenseCommandName() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "red_component mock1 mock1-intensity\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\nwrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidIntenseCommandSourceIDNotFound() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "intensity-component  \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidIntenseCommandSourceModelNotFound1() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "intensity-component mock1-intensity\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "source model is not found\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidIntenseCommandDestIDNotFound() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "intensity-component mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of new model ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidIntenseCommandWrongOrder() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "intensity-component mock1-intensity mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidIntenseCommandMissingCommand() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "intensity mock1 mock1-intensity\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidIntenseCommandWrongOrder1() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "mock1 mock1-intensity intensity-component\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

}
