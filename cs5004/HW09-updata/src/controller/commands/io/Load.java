package controller.commands.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import model.Image;
import model.ImageImpl;
import model.ImageState;

/**
 * This class extends the abstract AbstractLoader class and implements both the
 * ImageLoader and Command interfaces.
 */
public class Load extends AbstractLoader {

  @Override
  public ImageState loadImage(String path)
          throws IllegalStateException {

    BufferedImage imageBuffer = this.getBufferedImage(path);
    int height = imageBuffer.getHeight();
    int width = imageBuffer.getWidth();
    Image sourceModel = new ImageImpl(height, width);
    try {
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          Color pixel = new Color(imageBuffer.getRGB(j, i));
          int pixelR = pixel.getRed();
          int pixelG = pixel.getGreen();
          int pixelB = pixel.getBlue();
          sourceModel.setPixel(i, j, pixelR, pixelG, pixelB);
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalStateException("failed to read pixel data from image buffer");
    }
    return sourceModel;
  }

  protected BufferedImage getBufferedImage(String path) {
    BufferedImage imageBuffer;
    try {
      ImageInputStream imageIn = ImageIO.createImageInputStream(new File(path));
      imageBuffer = ImageIO.read(imageIn);
    } catch (IOException e) {
      throw new IllegalStateException("reading a file failed");
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("image buffer is null");
    }
    return imageBuffer;
  }
}
