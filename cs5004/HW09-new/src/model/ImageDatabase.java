package model;

/**
 * This interface represents operations that can be used to read and map
 * the state of an Image model to a String name.
 */
public interface ImageDatabase {

  /**
   * Get the name of an Image model if it exists in the ImageData model.
   * @param id name of the ImageState model
   * @return the ImageState model or null
   */
  ImageState getImage(String id);

  /**
   * Add an Image to the database coupled with given name.
   * @param id the String name of an Image model
   * @param destImage the state of given Image model
   * @throws IllegalArgumentException if id is null or destImage is null
   */
  void addImage(String id, ImageState destImage) throws IllegalArgumentException;
}
