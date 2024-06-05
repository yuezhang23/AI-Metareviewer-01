package controller.commands.strategy;

import model.Image;
import model.ImageImpl;
import model.ImageState;
import model.PixelState;
import model.PixelImpl;

/**
 * This class implements the ImageTransform interface with method to render
 * all pixel components with green-component values.
 */
public class RunGreen extends AbstractImageTransform {

  @Override
  public PixelState transformPixel(PixelState pixel) {
    if (pixel == null) {
      throw new IllegalStateException("single pixel can't be found");
    }
    return new PixelImpl(pixel.getG(), pixel.getG(), pixel.getG());
  }

  @Override
  public ImageState transformImage(ImageState source) {
    int h = source.getHeight();
    int w = source.getWidth();
    Image newImage = new ImageImpl(h, w);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int g = source.getGreenChannel(i, j);
        newImage.setPixel(i, j, g, g, g);
      }
    }
    return newImage;
  }
}
