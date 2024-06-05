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
 * This class tests the RunLuma class that implements ImageTransform interface
 * which extends the Command interface.
 */
public class TestRunLuma {
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
  public void testRunLumaCommand() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "luma-component mock1 mock1-luma\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-luma").getWidth());
    assertEquals(4, modelMap.getImage("mock1-luma").getHeight());

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

    view.renderImage(modelMap.getImage("mock1-luma"));
    String expected_mock1_luma =
                      "(8, 8, 8) (39, 39, 39) (102, 102, 102) "
                    + "(49, 49, 49) (121, 121, 121) (110, 110, 110) \n"
                    + "(25, 25, 25) (102, 102, 102) (49, 49, 49) (121, 121, 121) "
                    + "(110, 110, 110) (84, 84, 84) \n"
                    + "(102, 102, 102) (49, 49, 49) (121, 121, 121) (110, 110, 110) "
                    + "(53, 53, 53) (61, 61, 61) \n"
                    + "(49, 49, 49) (121, 121, 121) (110, 110, 110) (0, 0, 0) "
                    + "(114, 114, 114) (102, 102, 102) \n";
    assertEquals(expected_mock1_luma, log.toString());
  }

  @Test
  public void testInvalidLumaCommandRenderBeforeLoading() {
    StringReader in = new StringReader("luma-component mock1 mock1-luma\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLumaCommandName() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "red_component mock1 mock1-luma\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\nwrong input\nwrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLumaCommandSourceIDNotFound() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "luma-component  \n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLumaCommandSourceModelNotFound1() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "luma-component mock1-luma\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "source model is not found\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLumaCommandDestIDNotFound() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "luma-component mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "missing command data of new model ID\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLumaCommandWrongOrder() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "luma-component mock1-luma mock1\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "source model is not found\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLumaCommandMissingCommand() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "luma mock1 mock1-luma\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "wrong input\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testInvalidLumaCommandWrongOrder1() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "mock1 mock1-luma luma-component\n");
    new ControllerImpl(modelMap, in, view).processing();
    String expected =
            "wrong input\n"
                    + "wrong input\n"
                    + "missing command data of source ID\n";
    assertEquals(expected, log.toString());
  }
}
