package controller.commands.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.ImageState;

/**
 *
 */
public class SavePNG extends AbstractSaver {
  @Override
  public BufferedImage saveImage(ImageState model, String path)
          throws IllegalArgumentException, IllegalStateException {
    if (model == null || path == null) {
      throw new IllegalArgumentException("dest model or path is null");
    }
    BufferedImage image = this.getBufferedImage(model, path);
    try {
      ImageIO.write(image, "png", new File(path));
    } catch (IOException e) {
      throw new IllegalStateException("writing to png file failed");
    }
    return image;
  }
}
