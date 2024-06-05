package controller.commands.strategy;

import controller.commands.Command;
import model.ImageState;
import model.PixelState;

/**
 * This represents the ImageTransform interface that extends the Command interface
 * with all the methods required to operate on an Image model.
 */
public interface ImageTransform extends Command {
  /**
   * Return a state of one Pixel model after applying the commands
   * related to transformation. Note this method is saved for future use.
   * @param pixel the pixel to be transformed
   * @return the pixel state after transformation
   */
  PixelState transformPixel(PixelState pixel);

  /**
   * Return a state of the Image model after applying the commands
   * related to transformation.
   * @param source the model to be transformed
   * @return the state of model after transformation
   */
  ImageState transformImage(ImageState source);
}
