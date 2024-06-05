package testcontroller;

import org.junit.Before;
import org.junit.Test;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Scanner;
import controller.ControllerImpl;
import controller.commands.io.ImageSaving;
import model.ImageData;
import model.ImageDatabase;
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

  @Before
  public void setup() {
    StringBuilder log = new StringBuilder();
    modelMap = new ImageData();
    view = new ViewImpl(log);
  }

  @Test
  public void testSaverCommand() {
    StringBuilder saverLog = new StringBuilder();
    StringReader in = new StringReader("load images/mock1.ppm mock1\n"
            + "brighten 150 mock1 mock1-brighter\nsave images/mock1-br.ppm mock1-brighter");
    new ControllerImpl(modelMap, in, view).processing();
    new ImageSaving(modelMap.getImage("mock1-brighter"), saverLog).savingPPM();

    // test the saving data by appendable parameter saverLog
    String expected = "P3\n6 4\n255\n150\n160\n165\n175\n195\n175\n195\n255\n206\n"
           + "255\n182\n192\n255\n250\n255\n255\n250\n160\n175\n175\n175\n"
           + "195\n255\n206\n255\n182\n192\n255\n250\n255\n255\n250\n160\n"
           + "230\n240\n190\n195\n255\n206\n255\n182\n192\n255\n250\n255\n"
           + "255\n250\n160\n250\n190\n200\n255\n175\n255\n255\n182\n192\n"
           + "255\n250\n255\n255\n250\n160\n150\n150\n150\n175\n255\n175\n"
           + "195\n255\n206\n";
    assertEquals(expected, saverLog.toString());

    // test file written to the hard drive
    try {
      Scanner sc = new Scanner(new FileInputStream("images/mock1-br.ppm"));
      StringBuilder builder = new StringBuilder();
      while (sc.hasNextLine()) {
        String s = sc.nextLine();
        if (s.charAt(0) != '#') {
          builder.append(s + System.lineSeparator());
        }
      }
      assertEquals(saverLog.toString(), builder.toString());
    } catch (FileNotFoundException e) {
      System.out.println("File mock1-br.ppm not found!");
    }
  }

}
