package model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the ImageDatabase interface with all the methods.
 */
public class ImageData implements ImageDatabase {

  /**
   * This static class is a helper class to create an ImageData object that is not empty.
   */
  public static class Builder {
    Map<String, ImageState> map;

    public ImageData.Builder add(Map<String, ImageState> model) {
      this.map = model;
      return this;
    }

    public ImageData build() {
      return new ImageData(map);
    }
  }

  private Map<String, ImageState> imagePool;

  /**
   * Construct the ImageData model by setting the imagePool field value.
   * Note this constructor is only used in testing.
   * @param model the image model added to the ImageData model
   */
  private ImageData(Map<String, ImageState> model) {
    this.imagePool = model;
  }

  /**
   * Construct the ImageData model by initializing the imagePool field value.
   */
  public ImageData() {
    this.imagePool = new HashMap<>();
  }

  @Override
  public ImageState getImage(String id) {
    return imagePool.get(id);
  }

  @Override
  public void addImage(String id, ImageState destImage) throws IllegalArgumentException {
    if (id == null || destImage == null) {
      throw new IllegalArgumentException("id is null or destImage is null");
    }
    imagePool.put(id, destImage);
  }
}
