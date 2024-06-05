package controller.commands.io;

import controller.commands.Command;
import model.ImageState;

/**
 * This represents the ImageLoader interface to load an image file and
 * return a state of Image model.
 */
public interface ImageLoader extends Command {

  /**
   * Return a state of Image model by reading a collection of data from a PPM file.
   * @return an ImageState model
   * @throws IllegalStateException if file is not found or reading from file failed
   */
  ImageState loadImage(String path) throws IllegalStateException;
}
