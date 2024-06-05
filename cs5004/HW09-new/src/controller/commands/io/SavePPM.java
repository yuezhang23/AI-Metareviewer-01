package controller.commands.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import model.ImageState;

/**
 * This class implements the ImageSaver interface by extending and overriding method
 * saveImage, together creating a PPM image file.
 */
public class SavePPM extends AbstractSaver {

  @Override
  public BufferedImage saveImage(ImageState model, String path)
          throws IllegalArgumentException {
    if (model == null || path == null) {
      throw new IllegalArgumentException("dest model or path is null");
    }
    BufferedImage image = this.getBufferedImage(model);

    StringBuilder log = new StringBuilder();
    log.append("P3").append(System.lineSeparator());
    int width = image.getWidth();
    int height = image.getHeight();
    log.append(width).append(" ");
    log.append(height + System.lineSeparator());
    log.append("255" + System.lineSeparator());
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color pixel = new Color(image.getRGB(j, i));
        log.append(pixel.getRed() + System.lineSeparator());
        log.append(pixel.getGreen() + System.lineSeparator());
        log.append(pixel.getBlue() + System.lineSeparator());
      }
    }
    try {
      FileWriter wr = new FileWriter(path);
      wr.write(log.toString());
      wr.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return image;
  }
}
