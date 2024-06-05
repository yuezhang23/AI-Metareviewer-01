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
 * This class tests the RunRed class that implements ImageTransform interface
 * which extends the Command interface.
 */
public class TestRunRed {
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
  public void testRunRedCommand() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red-component mock1 mock1-red\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-red").getWidth());
    assertEquals(4, modelMap.getImage("mock1-red").getHeight());

    preview.renderImage(modelMap.getImage("mock1"));
    // image display before render red
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

    view.renderImage(modelMap.getImage("mock1-red"));
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
  }

  @Test
  public void testInvalidRedCommandRenderBeforeLoading() {
    StringReader in = new StringReader("red-component mock1 mock1-red\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
             "source model is not found\n"
           + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidRedCommandName() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red_component mock1 mock1-red\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\nwrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidRedCommandSourceIDNotFound() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red-component  \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidRedCommandSourceModelNotFound1() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red-component mock1-red\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "source model is not found\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidRedCommandDestIDNotFound() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red-component mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of new model ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidRedCommandWrongOrder() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red-component mock1-red mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
          + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidRedCommandMissingCommand() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red mock1 mock1-red\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
              "wrong input\n"
            + "wrong input\n"
            + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidRedCommandWrongOrder1() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "mock1 mock1-red red-component\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
              "wrong input\n"
            + "wrong input\n"
            + "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

}
