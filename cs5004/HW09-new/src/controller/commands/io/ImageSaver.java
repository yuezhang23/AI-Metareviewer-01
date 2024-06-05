package controller.commands.io;

import java.awt.image.BufferedImage;
import controller.commands.Command;
import model.ImageState;

/**
 * This represents the ImageSaver interface to keep reading data from an image model
 * and export the data to different format of images.
 */
public interface ImageSaver extends Command {

  /**
   * Continuously read data from the model and save all required data to a BufferedImage
   * object and at the same time export to given file path.
   * @param model the destination model
   * @param path the destination path to save model
   * @return a BufferedImage object
   * @throws IllegalArgumentException is model is null or path is null
   */
  BufferedImage saveImage(ImageState model, String path) throws IllegalArgumentException;
}
