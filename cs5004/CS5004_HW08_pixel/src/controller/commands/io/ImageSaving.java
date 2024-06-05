package controller.commands.io;

import java.io.IOException;
import java.util.Objects;

import model.ImageState;

/**
 * This class implements the ImageSaver interface.
 */
public class ImageSaving extends RunSaver implements ImageSaver {

  private ImageState image;
  private Appendable output;

  /**
   * Construct the ImageSaving model by setting field values.
   * @param model the image model to read data from
   * @param out read to write all data about a PPM image
   */
  public ImageSaving(ImageState model, Appendable out) {
    this.image = model;
    this.output = out;
  }

  private void write(String message) {
    Objects.requireNonNull(message);
    try {
      this.output.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("write to view failed");
    }
  }

  @Override
  public void savingPPM() {
    this.write("P3" + System.lineSeparator());
    int width = image.getWidth();
    int height = image.getHeight();
    this.write(width + " ");
    this.write(height + System.lineSeparator());
    this.write("255" + System.lineSeparator());
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        this.write(image.getRedChannel(i, j) + System.lineSeparator());
        this.write(image.getGreenChannel(i, j) + System.lineSeparator());
        this.write(image.getBlueChannel(i, j) + System.lineSeparator());
      }
    }
  }
}
