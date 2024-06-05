package controller.commands.io;

import controller.commands.Command;

/**
 * This represents the ImageSaver interface to keep writing data from an image model
 * required for a standard PPM image file.
 */
public interface ImageSaver extends Command {
  /**
   * Continuously read data from the model and present all required
   * data for a PPM image.
   */
  void savingPPM();

  void savingPNG();

  void savingJPG();

  void savingBMP();
}
