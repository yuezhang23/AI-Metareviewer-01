package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.*;

public class Canvas extends JPanel {
  private final JLabel[] imageLabel;

  public Canvas() {
    imageLabel = new JLabel[2];
    JScrollPane[] imageScrollPane = new JScrollPane[2];
    for (int i = 0; i < imageLabel.length; i++) {
      imageLabel[i] = new JLabel();
      imageScrollPane[i] = new JScrollPane(imageLabel[i]);
      imageScrollPane[i].setBackground(Color.gray);
      imageScrollPane[i].setPreferredSize(new Dimension(200, 400));
      this.add(imageScrollPane[i]);
    }
  }

  public void imagePainting(Map<String, BufferedImage> map, String id) {
    // paint rendered image on the left
    imageLabel[0].setIcon(new ImageIcon(map.get(id)));
    // paint original image on the right
    imageLabel[1].setIcon(new ImageIcon(map.get(id.split("-")[0])));
    this.setVisible(true);
  }

  public void singlePainting(Map<String, BufferedImage> map, String id, int side) {
    if (map.get(id) == null) {
      imageLabel[side].setIcon(new ImageIcon());
    } else {
      imageLabel[side].setIcon(new ImageIcon(map.get(id)));
    }
  }
}
