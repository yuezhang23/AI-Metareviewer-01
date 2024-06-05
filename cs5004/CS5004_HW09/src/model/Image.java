package model;

/**
 * This interface extends the ImageState interface by adding setPixel method that
 * can modify the state of an Image model.
 */
public interface Image extends ImageState {
  /**
   * Set each field value of a Pixel model at given position of a given
   * Image model.
   * @param h the row position of given pixel in given image
   * @param w the column position of given pixel in given image
   * @param r the red component value that writes to a pixel at given position
   * @param g the green component value that writes to a pixel at given position
   * @param b the blue component value that writes to a pixel at given position
   * @throws IllegalStateException if given position is out of image size
   * @throws IllegalArgumentException if given pixel field value is bigger than 255 or negative
   */
  void setPixel(int h, int w, int r, int g, int b)
          throws IllegalStateException, IllegalArgumentException;
}
