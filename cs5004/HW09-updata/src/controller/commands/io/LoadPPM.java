package controller.commands.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * This class implements both the ImageLoader and Command interfaces by extends Load class and
 * overriding the getBufferedImage method.
 *
 */
public class LoadPPM extends Load {

  @Override
  public BufferedImage getBufferedImage(String path) {
    StringBuilder imageData = new StringBuilder();
    try {
      Scanner sc = new Scanner(new FileInputStream(path));
      while (sc.hasNextLine()) {
        String s = sc.nextLine();
        if (s.charAt(0) != '#') {
          imageData.append(s + System.lineSeparator());
        }
      }
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("File " + path + " not found!\n");
    }

    Scanner sc = new Scanner(imageData.toString());
    String type = sc.next(); // P3
    int width = sc.nextInt(); // 1024
    int height = sc.nextInt(); // 768
    int maxValue = sc.nextInt(); //225
    BufferedImage imageBuffer = new BufferedImage(width, height, TYPE_INT_RGB);

    if (!type.equals("P3") || maxValue != 255 || width < 0 || height < 0) {
      throw new IndexOutOfBoundsException("width or height is invalid");
    }
    try {
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int r = sc.nextInt();
          int g = sc.nextInt();
          int b = sc.nextInt();
          int pixel = new Color(r, g, b).getRGB();
          imageBuffer.setRGB(j, i, pixel);
        }
      }
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("loading P3 file failed");
    }
    return imageBuffer;
  }
}
