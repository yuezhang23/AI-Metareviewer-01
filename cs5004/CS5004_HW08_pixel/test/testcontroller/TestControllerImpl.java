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
 * Test the ControllerImpl class that implements the Controller interface.
 * Single Command testing is shown in separate class of the same package.
 */
public class TestControllerImpl {
  private ImageDatabase modelMap;
  private StringBuilder log;
  private StringBuilder preLog;
  private View view;
  private View preview;

  @Before
  public void setup() {
    modelMap = new ImageData();
    log = new StringBuilder();
    view = new ViewImpl(log);
    preLog = new StringBuilder();
    preview = new ViewImpl(preLog);
  }

  @Test
  public void testProcessing() {
    StringReader in = new StringReader("1 2 3 4 quit 1 2");
    new ControllerImpl(modelMap, in, view).processing();
    String expected = "wrong input\n"
           + "wrong input\n"
           + "wrong input\n"
           + "wrong input\n"
            + "user terminated";
    assertEquals(expected, log.toString());
  }

  @Test
  //test multiple commands
  public void testMultipleCommands() {
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "brighten 150 mock1 mock1-br\ngreen-component mock1-br mock1-br-green\n"
            + "save images/mock1-brighter-green.ppm mock1-br-green\n");
    new ControllerImpl(modelMap, in, view).processing();
    assertEquals(6, modelMap.getImage("mock1-br-green").getWidth());
    assertEquals(4, modelMap.getImage("mock1-br-green").getHeight());

    preview.renderImage(modelMap.getImage("mock1"));
    // image display after load command only
    String expected_mock1 =
            "(0, 10, 15) (25, 45, 25) (45, 124, 56) (113, 32, 42) "
            + "(150, 100, 255) (180, 100, 10) \n"
            + "(25, 25, 25) (45, 124, 56) (113, 32, 42) (150, 100, 255) "
            + "(180, 100, 10) (80, 90, 40) \n"
            + "(45, 124, 56) (113, 32, 42) (150, 100, 255) (180, 100, 10) "
            + "(100, 40, 50) (125, 25, 235) \n"
            + "(113, 32, 42) (150, 100, 255) (180, 100, 10) (0, 0, 0) "
            + "(25, 150, 25) (45, 124, 56) \n";
    assertEquals(expected_mock1, preLog.toString());

    view.renderImage(modelMap.getImage("mock1-br"));
    view.renderImage(modelMap.getImage("mock1-br-green"));
    // image display after load command, brighten command, green-component command
    String expected_mock1_br_green =
                       // pixels brightened
                       "(150, 160, 165) (175, 195, 175) (195, 255, 206) (255, 182, 192) "
                     + "(255, 250, 255) (255, 250, 160) \n"
                     + "(175, 175, 175) (195, 255, 206) (255, 182, 192) (255, 250, 255) "
                     + "(255, 250, 160) (230, 240, 190) \n"
                     + "(195, 255, 206) (255, 182, 192) (255, 250, 255) (255, 250, 160) "
                     + "(250, 190, 200) (255, 175, 255) \n"
                     + "(255, 182, 192) (255, 250, 255) (255, 250, 160) (150, 150, 150) "
                     + "(175, 255, 175) (195, 255, 206) \n"
                     // pixels greyscale-green after brightened
                     + "(160, 160, 160) (195, 195, 195) (255, 255, 255) (182, 182, 182) "
                     + "(250, 250, 250) (250, 250, 250) \n"
                     + "(175, 175, 175) (255, 255, 255) (182, 182, 182) (250, 250, 250) "
                     + "(250, 250, 250) (240, 240, 240) \n"
                     + "(255, 255, 255) (182, 182, 182) (250, 250, 250) (250, 250, 250) "
                     + "(190, 190, 190) (175, 175, 175) \n"
                     + "(182, 182, 182) (250, 250, 250) (250, 250, 250) (150, 150, 150) "
                     + "(255, 255, 255) (255, 255, 255) \n";
    assertEquals(expected_mock1_br_green, log.toString());
  }


  // test IOE exception
  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    Appendable gameLog = new FailingAppendable();
    StringReader in = new StringReader("load images/mocking.ppm mocking");
    new ControllerImpl(modelMap, in, new ViewImpl(gameLog)).processing();
  }
}
