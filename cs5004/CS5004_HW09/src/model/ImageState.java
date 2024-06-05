package model;

/**
 * This interface represents operations that can be used to read from a state
 * of an Image model without changing it.
 */
public interface ImageState {

  /**
   * Get the red component value of a pixel that set at given position
   * in the image.
   * @param h the row position of given pixel matrix
   * @param w the column position of given pixel matrix
   * @return the red component value
   * @throws IllegalStateException if position is out of image size
   */
  int getRedChannel(int h, int w) throws IllegalStateException;

  /**
   * Get the green component value of a pixel that set at given position
   * in the image.
   * @param h the row position of given pixel matrix
   * @param w the column position of given pixel matrix
   * @return the green component value
   * @throws IllegalStateException if position is out of image size
   */
  int getGreenChannel(int h, int w) throws IllegalStateException;

  /**
   * Get the blue component value of a pixel that set at given position
   * in the image.
   * @param h the row position of given pixel matrix
   * @param w the column position of given pixel matrix
   * @return the blue component value
   * @throws IllegalStateException if position is out of image size
   */
  int getBlueChannel(int h, int w) throws IllegalStateException;

  /**
   * Get the width value of given image, which is the column length of
   * the pixel matrix field.
   * @return the width value
   */
  int getWidth();

  /**
   * Get the height value of given image, which is the row length of
   * the pixel matrix field.
   * @return the height value
   */
  int getHeight();
}
