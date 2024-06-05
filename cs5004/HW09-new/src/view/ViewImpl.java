package view;

import java.io.IOException;
import java.util.Objects;
import model.ImageState;

/**
 * This class implements the View interface.
 */
public class ViewImpl implements View {
  private final Appendable out;

  /**
   * Construct the view model by setting field value.
   * @param out present appendable data in a form of a String
   */
  public ViewImpl(Appendable out) {
    this.out = out;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    Objects.requireNonNull(message);
    this.out.append(message);
  }

  @Override
  public void renderImage(ImageState model) {
    try {
      for (int i = 0; i < model.getHeight(); i++) {
        for (int j = 0; j < model.getWidth(); j++) {
          this.out.append("(" + model.getRedChannel(i, j) + ", "
                  + model.getGreenChannel(i, j) + ", "
                  + model.getBlueChannel(i, j) + ")" + " ");
        }
        out.append(System.lineSeparator());
      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
