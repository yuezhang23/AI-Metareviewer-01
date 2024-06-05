package controller.commands.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Scanner;

import model.ImageState;
import model.ImageDatabase;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * This abstract class implements the Command interface by running save command
 * and continuously reading data from a Scanner object and export data
 * to a standard image file.
 */
public abstract class AbstractSaver implements ImageSaver {

  @Override
  public void runCommand(Scanner sc, ImageDatabase modelMap)
          throws IllegalStateException {
    Objects.requireNonNull(sc);
    Objects.requireNonNull(modelMap);

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of argument 1");
    }
    String destPath = sc.next();

    if (!sc.hasNext()) {
      throw new IllegalStateException("missing command data of argument 2");
    }
    String destID = sc.next();

    ImageState destImage = modelMap.getImage(destID);
    if (destImage == null) {
      throw new IllegalStateException("image not found");
    }
    // dynamic dispatch
    this.saveImage(destImage, destPath);
  }

  protected BufferedImage getBufferedImage(ImageState model) {
    Objects.requireNonNull(model);
    int height = model.getHeight();
    int width = model.getWidth();
    BufferedImage newImageBuffer = new BufferedImage(width, height, TYPE_INT_RGB );
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newImageBuffer.setRGB(j, i, new Color(model.getRedChannel(i, j),
                model.getGreenChannel(i, j), model.getBlueChannel(i, j)).getRGB());
      }
    }
    return newImageBuffer;
  }
}
