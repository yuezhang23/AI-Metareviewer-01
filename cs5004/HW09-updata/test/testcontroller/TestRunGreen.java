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
 * This class tests the RunGreen class that implements ImageTransform interface
 * which extends the Command interface.
 */
public class TestRunGreen {
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
  public void testRunGreenCommand() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "green-component mock1 mock1-green\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-green").getWidth());
    assertEquals(4, modelMap.getImage("mock1-green").getHeight());

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

    view.renderImage(modelMap.getImage("mock1-green"));
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
  }

  @Test
  public void testInvalidGreenCommandRenderBeforeLoading() {
    StringReader in = new StringReader("green-component mock1 mock1-green\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidGreenCommandName() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "red_component mock1 mock1-green\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\nwrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidGreenCommandSourceIDNotFound() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "green-component  \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidGreenCommandSourceModelNotFound1() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "green-component mock1-green\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "source model is not found\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidGreenCommandDestIDNotFound() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "green-component mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of new model ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidGreenCommandWrongOrder() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "green-component mock1-green mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidGreenCommandMissingCommand() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "green mock1 mock1-green\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidGreenCommandWrongOrder1() {
    StringReader in = new StringReader("loadPPM images/mock1.ppm mock1\n"
            + "mock1 mock1-green green-component\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }
}
