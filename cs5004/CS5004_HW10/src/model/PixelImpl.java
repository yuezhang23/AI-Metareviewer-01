package model;

/**
 * This class implements both Pixel interface and PixelState interface
 * with all the methods that can read and modify a Pixel model.
 */
public class PixelImpl implements Pixel {
  private int r;
  private int g;
  private int b;

  /**
   * Construct the Pixel model by setting the three component values
   * that represent a pixel.
   * @param r the red component value of a pixel
   * @param g the green component value of a pixel
   * @param b the blue component value of a pixel
   */
  public PixelImpl(int r, int g, int b) {
    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
      throw new IllegalArgumentException("invalid pixel setting");
    }
    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  public int getR() {
    return r;
  }

  @Override
  public int getG() {
    return g;
  }

  @Override
  public int getB() {
    return b;
  }

  @Override
  public void setR(int r) {
    if (r < 0 || r > 255) {
      throw new IllegalArgumentException("invalid pixel setting at red");
    }
    this.r = r;
  }

  @Override
  public void setG(int g) {
    if (g < 0 || g > 255) {
      throw new IllegalArgumentException("invalid pixel setting at green");
    }
    this.g = g;
  }

  @Override
  public void setB(int b) {
    if (b < 0 || b > 255) {
      throw new IllegalArgumentException("invalid pixel setting at blue");
    }
    this.b = b;
  }
}
