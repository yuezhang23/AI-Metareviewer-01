package controller.commands.strategy;

import model.Image;
import model.ImageImpl;
import model.ImageState;
import model.PixelState;
import model.PixelImpl;

/**
 * This class implements the ImageTransform interface with method to render
 * all pixel components with maximum values.
 */
public class RunMax extends RunGreyScaleAbstract implements ImageTransform {

  @Override
  public PixelState transformPixel(PixelState pixel) {
    if (pixel == null) {
      throw new IllegalStateException("single pixel can't be found");
    }
    int value = this.calMax(pixel.getR(), pixel.getG(), pixel.getB());
    return new PixelImpl(value, value, value);
  }

  @Override
  public ImageState transformImage(ImageState source) {
    int h = source.getHeight();
    int w = source.getWidth();
    Image newImage = new ImageImpl(h, w);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int max = this.calMax(source.getRedChannel(i, j),
                source.getGreenChannel(i, j), source.getBlueChannel(i, j));
        newImage.setPixel(i, j, max, max, max);
      }
    }
    return newImage;
  }

  private int calMax(int pixelR, int pixelG, int pixelB) {
    return Math.max(Math.max(pixelR, pixelG), pixelB);
  }
}
