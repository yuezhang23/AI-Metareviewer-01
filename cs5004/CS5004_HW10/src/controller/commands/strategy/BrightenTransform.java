package controller.commands.strategy;

import model.Image;
import model.ImageImpl;
import model.ImageState;
import model.PixelState;
import model.PixelImpl;

/**
 * This class implements the ImageTransform interface with method to render
 * all pixel components with given component increment.
 */
public class BrightenTransform extends RunBrighten implements ImageTransform {
  private int incr;

  /**
   * Construct the model by setting the increment value for pixel transformation.
   * @param incr the increment value for brighten or darken
   */
  public BrightenTransform(int incr) {
    if (incr > 255 || incr < -255) {
      throw new IllegalStateException("increment is not valid\n");
    }
    this.incr = incr;
  }

  @Override
  public PixelState transformPixel(PixelState pixel) {
    if (pixel == null) {
      throw new IllegalStateException("single pixel can't be found\n");
    }
    return new PixelImpl(clamp(pixel.getR() + incr),
            clamp(pixel.getG() + incr), clamp(pixel.getB() + incr));
  }

  @Override
  public ImageState transformImage(ImageState source) {
    int h = source.getHeight();
    int w = source.getWidth();
    Image newImage = new ImageImpl(h, w);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int r = clamp(source.getRedChannel(i, j) + incr);
        int g = clamp(source.getGreenChannel(i, j) + incr);
        int b = clamp(source.getBlueChannel(i, j) + incr);
        newImage.setPixel(i, j, r, g, b);
      }
    }
    return newImage;
  }

  private int clamp(int value) {
    if (value < 0) {
      return 0;
    }
    return Math.min(value, 255);
  }
}
