package controller.commands.strategy;

import model.Image;
import model.ImageImpl;
import model.ImageState;
import model.PixelState;
import model.PixelImpl;

/**
 * This class implements the ImageTransform interface with method to render
 * all pixel components with red-component values.
 */
public class RunRed extends AbstractImageTransform {

  @Override
  public PixelState transformPixel(PixelState pixel) {
    if (pixel == null) {
      throw new IllegalStateException("single pixel can't be found");
    }
    return new PixelImpl(pixel.getR(), pixel.getR(), pixel.getR());
  }

  @Override
  public ImageState transformImage(ImageState source) {
    int h = source.getHeight();
    int w = source.getWidth();
    Image newImage = new ImageImpl(h, w);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int r = source.getRedChannel(i, j);
        newImage.setPixel(i, j, r, r, r);
      }
    }
    return newImage;
  }
}
