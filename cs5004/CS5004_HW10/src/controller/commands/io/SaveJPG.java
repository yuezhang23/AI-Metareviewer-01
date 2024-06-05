package controller.commands.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.ImageState;

/**
 * This class implements the ImageSaver interface by extending and overriding method
 * saveImage, together creating a JPG image file.
 */
public class SaveJPG extends AbstractSaver {
  @Override
  public BufferedImage saveImage(ImageState model, String path)
          throws IllegalArgumentException, IllegalStateException {
    if (model == null || path == null) {
      throw new IllegalArgumentException("dest model or path is null");
    }
    BufferedImage image = this.getBufferedImage(model);
    try {
      ImageIO.write(image, "jpeg", new File(path));
    } catch (IOException e) {
      throw new IllegalStateException("writing to jpg file failed");
    }
    return image;
  }
}
