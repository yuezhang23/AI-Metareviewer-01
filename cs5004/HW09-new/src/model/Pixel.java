package model;

/**
 * This interface extends the PixelState interface by adding setters that
 * can modify the state of a Pixel model.
 */
public interface Pixel extends PixelState {

  /**
   * Set the red component value of a pixel model.
   * @param r given red component value from user
   * @throws IllegalArgumentException if r > 255 or r < 0
   */
  void setR(int r) throws IllegalArgumentException;

  /**
   * Set the green component value of a pixel model.
   * @param g given green component value from user
   * @throws IllegalArgumentException if g > 255 or g < 0
   */
  void setG(int g) throws IllegalArgumentException;

  /**
   * Set the blue component value of a pixel model.
   * @param b given blue component value from user
   * @throws IllegalArgumentException if b > 255 or b < 0
   */
  void setB(int b) throws IllegalArgumentException;
}
