package controller.commands.strategy;

import model.Image;
import model.ImageImpl;
import model.ImageState;
import model.PixelState;
import model.PixelImpl;

/**
 * This class implements the ImageTransform interface with method to render
 * all pixel components with average values.
 */
public class RunIntensity extends AbstractImageTransform {

  @Override
  public PixelImpl transformPixel(PixelState pixel) {
    if (pixel == null) {
      throw new IllegalStateException("single pixel can't be found");
    }
    int value = this.calAver(pixel.getR(), pixel.getG(), pixel.getB());
    return new PixelImpl(value, value, value);
  }

  @Override
  public ImageState transformImage(ImageState source) {
    int h = source.getHeight();
    int w = source.getWidth();
    Image newImage = new ImageImpl(h, w);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int aver = this.calAver(source.getRedChannel(i, j),
                source.getGreenChannel(i, j), source.getBlueChannel(i, j));
        newImage.setPixel(i, j, aver, aver, aver);
      }
    }
    return newImage;
  }

  private int calAver(int pixelR, int pixelG, int pixelB) {
    int sum = pixelR + pixelG + pixelB;
    return (int)Math.round(sum / 3.0);
  }
}
