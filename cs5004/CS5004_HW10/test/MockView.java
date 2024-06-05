import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import controller.ViewListener;
import view.IView;
import view.View;

/**
 * This class implements the IView interface with a number of methods.
 */
class MockView implements IView {
  private Map<String, BufferedImage> bufferMap;
  private Appendable out;

  /**
   * Construct the MockView object by initializing the View object that renders the ImageState
   * model and an Appendable object to render the BufferedImage model.
   * @param view the View object that renders the ImageState model
   * @param log the Appendable object
   */
  public MockView(View view, Appendable log) {
    bufferMap = new HashMap<>();
    out = log;
  }

  @Override
  public void viewUpdate(BufferedImage image, String id) {
    bufferMap.put(id, image);
    try {
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          Color pixel = new Color(image.getRGB(j, i));
          this.out.append("(").append(String.valueOf(pixel.getRed())).
                  append(", ").append(String.valueOf(pixel.getGreen())).
                  append(", ").append(String.valueOf(pixel.getBlue())).
                  append(")").append(" ");
        }
        out.append(System.lineSeparator());
      }
    } catch (IOException e) {
      throw new IllegalStateException("buffered image can't be processed");
    }
  }

  @Override
  public void addViewListener(ViewListener subscriber) {
    this.notifyAll();
  }

  @Override
  public String actSave() {
    return "mock1  res/testcase  ppm";
  }

  @Override
  public String actLoad() {
    return "mock1.ppm  res/mock1.ppm";
  }

  @Override
  public void getFrameFocus() {
    this.getFrameFocus();
  }

  @Override
  public void setVisible(boolean b) {
    this.setVisible(false);
  }
}
