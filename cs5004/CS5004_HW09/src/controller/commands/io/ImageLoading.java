package controller.commands.io;

import java.util.NoSuchElementException;
import java.util.Scanner;

import model.Image;
import model.ImageImpl;
import model.ImageState;

/**
 * This class implements the ImageLoader interface.
 */
public class ImageLoading extends RunLoader implements ImageLoader {
  private String data;

  public ImageLoading(String data) {
    this.data = data;
  }

  @Override
  public ImageState loading() throws IndexOutOfBoundsException, IllegalStateException {
    Scanner sc = new Scanner(data);
    String type = sc.next();
    int width = sc.nextInt(); // 1024
    int height = sc.nextInt(); // 768
    int maxValue = sc.nextInt();

    if (!type.equals("P3") || maxValue != 255) {
      throw new IndexOutOfBoundsException("wrong pixel data for p3 file");
    }
    if (width < 0 || height < 0) {
      throw new IndexOutOfBoundsException("width or height is invalid");
    }
    Image sourceModel = new ImageImpl(height, width);
    try {
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int r = sc.nextInt();
          int g = sc.nextInt();
          int b = sc.nextInt();
          sourceModel.setPixel(i, j, r, g, b);
        }
      }
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("loading P3 file failed");
    }
    return sourceModel;
  }
}
