package view;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface Canvas {

  void imagePainting(Map<String, BufferedImage> map, String id);
  void singlePainting(Map<String, BufferedImage> map, String id, int side)
}
