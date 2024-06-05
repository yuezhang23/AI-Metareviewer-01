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
 * This class tests the RunBlue class that implements ImageTransform interface
 * which extends the Command interface.
 */
public class TestRunBlue {
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
  public void testRunBlueCommand() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "blue-component mock1 mock1-blue\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-blue").getWidth());
    assertEquals(4, modelMap.getImage("mock1-blue").getHeight());

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

    view.renderImage(modelMap.getImage("mock1-blue"));
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
  }

  @Test
  public void testInvalidBlueCommandRenderBeforeLoading() {
    StringReader in = new StringReader("blue-component mock1 mock1-blue\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlueCommandName() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red_component mock1 mock1-blue\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\nwrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlueCommandSourceIDNotFound() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "blue-component  \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlueCommandSourceModelNotFound1() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "blue-component mock1-blue\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "source model is not found\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlueCommandDestIDNotFound() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "blue-component mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of new model ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlueCommandWrongOrder() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "blue-component mock1-blue mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlueCommandMissingCommand() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "blue mock1 mock1-blue\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidBlueCommandWrongOrder1() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "mock1 mock1-blue blue-component\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

}
