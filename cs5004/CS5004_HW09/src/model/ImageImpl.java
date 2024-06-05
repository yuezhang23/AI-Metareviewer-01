package model;

/**
 * This class implements both ImageState interface and Image interface
 * with all the methods that can read and modify an Image model.
 */
public class ImageImpl extends ImageData implements Image {
  /**
   * This static class is a helper to implement an ImageImpl object that is not empty.
   */
  public static class Builder {
    PixelState[][] pixelMatrix;

    public Builder setPixel(PixelState[][] matrix) {
      this.pixelMatrix = matrix;
      return this;
    }

    public ImageImpl build() {
      return new ImageImpl(pixelMatrix);
    }
  }

  private final PixelState[][] pixelMatrix;
  private final int width;
  private final int height;

  /**
   * Construct the Image model by setting the pixelState field value.
   * Note this constructor is only use for testing.
   * @param matrix the state of an Image model that represent all pixel data
   *               in the image.
   */
  private ImageImpl(PixelState[][] matrix) {
    this.pixelMatrix = matrix;
    this.height = matrix.length;
    this.width = matrix[0].length;
  }

  /**
   * Construct the Image model by setting the height and width of the image.
   * @param h height value of the model
   * @param w width value of the model
   * @throws IllegalArgumentException if inputs are not positive
   */
  public ImageImpl(int h, int w) {
    if (h <= 0 || w <= 0) {
      throw new IllegalArgumentException("image size is not right");
    }
    this.pixelMatrix = new PixelState[h][w];
    this.height = h;
    this.width = w;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getRedChannel(int h, int w) throws IllegalStateException {
    if (!isValidPosition(h, w) || !isValidPixel(h, w)) {
      throw new IllegalStateException("pixel at given position is invalid");
    }
    return pixelMatrix[h][w].getR();
  }

  @Override
  public int getGreenChannel(int h, int w) throws IllegalStateException {
    if (!isValidPosition(h, w) || !isValidPixel(h, w)) {
      throw new IllegalStateException("pixel at given position is invalid");
    }
    return pixelMatrix[h][w].getG();
  }

  @Override
  public int getBlueChannel(int h, int w) throws IllegalStateException {
    if (!isValidPosition(h, w) || !isValidPixel(h, w)) {
      throw new IllegalStateException("pixel at given position is invalid");
    }
    return pixelMatrix[h][w].getB();
  }

  @Override
  public void setPixel(int h, int w, int r, int g, int b)
          throws IllegalStateException, IllegalArgumentException {
    if (!isValidPosition(h, w)) {
      throw new IllegalStateException("invalid position: h:"
              + h + "<" + height + ", w:" + w + "> " + width);
    }
    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
      throw new IllegalArgumentException("invalid pixel setting");
    }
    pixelMatrix[h][w] = new PixelImpl(r, g, b);
  }

  private boolean isValidPosition(int h, int w) {
    return h <= height - 1 && w <= width - 1 && h >= 0 && w >= 0;
  }

  private boolean isValidPixel(int h, int w) {
    return pixelMatrix[h][w] != null;
  }
}