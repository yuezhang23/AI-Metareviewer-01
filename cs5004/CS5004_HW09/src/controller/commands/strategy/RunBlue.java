package controller.commands.strategy;

import model.Image;
import model.ImageImpl;
import model.ImageState;
import model.PixelImpl;
import model.PixelState;

/**
 * This class implements the ImageTransform interface with method to render
 * all pixel components with blue-component values.
 */
public class RunBlue extends RunGreyScaleAbstract implements ImageTransform {
  @Override
  public PixelState transformPixel(PixelState pixel) {
    if (pixel == null) {
      throw new IllegalStateException("single pixel can't be found");
    }
    return new PixelImpl(pixel.getB(), pixel.getB(), pixel.getB());
  }

  @Override
  public ImageState transformImage(ImageState source) {
    int h = source.getHeight();
    int w = source.getWidth();
    Image newImage = new ImageImpl(h, w);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int blue = source.getBlueChannel(i, j);
        newImage.setPixel(i, j, blue, blue, blue);
      }
    }
    return newImage;
  }
}
