package model;

/**
 * This interface represents operations that can be used to read from a state
 * of a Pixel model without changing it.
 */
public interface PixelState {
  /**
   * Get red component value of the Pixel model.
   * @return the red component value
   */
  int getR();

  /**
   * Get green component value of the Pixel model.
   * @return the green component value
   */
  int getG();

  /**
   * Get blue component value of the Pixel model.
   * @return the blue component value
   */
  int getB();
}
