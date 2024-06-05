package view;

import java.io.IOException;
import model.ImageState;

/**
 * This interface represents the view to read all error messages from
 * running the program and render a given Image model as well.
 */
public interface View {

  /**
   * Read and present message from the program.
   * @param message output data from running the program
   * @throws IOException if output is not formatted
   */
  void renderMessage(String message) throws IOException;

  /**
   * Read and present message from a given Image model.
   * @param model the model to be rendered
   */
  void renderImage(ImageState model);
}
