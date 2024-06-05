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
 * This class tests the RunMax class that implements ImageTransform interface
 * which extends the Command interface.
 */
public class TestRunValue {
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
  public void testRunMaxValCommand() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "value-component mock1 mock1-value\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-value").getWidth());
    assertEquals(4, modelMap.getImage("mock1-value").getHeight());

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

    view.renderImage(modelMap.getImage("mock1-value"));
    String expected_mock1_value =
              "(15, 15, 15) (45, 45, 45) (124, 124, 124) "
            + "(113, 113, 113) (255, 255, 255) (180, 180, 180) \n"
            + "(25, 25, 25) (124, 124, 124) (113, 113, 113) (255, 255, 255) "
            + "(180, 180, 180) (90, 90, 90) \n"
            + "(124, 124, 124) (113, 113, 113) (255, 255, 255) (180, 180, 180) "
            + "(100, 100, 100) (235, 235, 235) \n"
            + "(113, 113, 113) (255, 255, 255) (180, 180, 180) (0, 0, 0) "
            + "(150, 150, 150) (124, 124, 124) \n";
    assertEquals(expected_mock1_value, log.toString());
  }

  @Test
  public void testInvalidMaxCommandRenderBeforeLoading() {
    StringReader in = new StringReader("value-component mock1 mock1-value\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidMaxCommandName() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "red_component mock1 mock1-value\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\nwrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidMaxCommandSourceIDNotFound() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "value-component  \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidMaxCommandSourceModelNotFound1() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "value-component mock1-value\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "source model is not found\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidMaxCommandDestIDNotFound() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "value-component mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of new model ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidMaxCommandWrongOrder() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "value-component mock1-value mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidMaxCommandMissingCommand() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "value mock1 mock1-value\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidMaxCommandWrongOrder1() {
    StringReader in = new StringReader("loadPPM res/mock1.ppm mock1\n"
            + "mock1 mock1-value value-component\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

}
