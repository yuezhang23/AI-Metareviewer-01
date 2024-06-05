package HashTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Histogram {
  int[] arr;
  int x;
  int point;

  public Histogram(int[] arr, int point) {
    this.arr = arr;
    x = arr.length + 5;
    this.point = point;

  }

  public void drawHistogram() {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new BorderLayout());
      frame.add(new JScrollPane(new Graph(arr)));
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    }

    protected class Graph extends JPanel {

      protected static final int MIN_BAR_WIDTH = 4;
      private int[] mapHistory;

      public Graph(int[] arr) {
        this.mapHistory = arr;
        int width = (arr.length * MIN_BAR_WIDTH) + 11;
        Dimension minSize = new Dimension(width,300);
        Dimension prefSize = new Dimension(width, 500);
        setMinimumSize(minSize);
        setPreferredSize(prefSize);
      }

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapHistory != null) {
          int xOffset = 5;
          int yOffset = 5;
          int width = getWidth() - 1 - (xOffset * 2);
          int height = getHeight() - 1 - (yOffset * 2);
          Graphics2D g2d = (Graphics2D) g.create();
          g2d.setColor(Color.DARK_GRAY);
          g2d.drawRect(xOffset, yOffset, width, height);
          int barWidth = Math.max(MIN_BAR_WIDTH,
                  (int) Math.floor((float) width
                          / (float) mapHistory.length));

          int maxValue = Arrays.stream(arr).max().getAsInt() + 10;

          int xPos = xOffset;
          for (int i =0; i<arr.length;i++) {
            int value = mapHistory[i];
            int barHeight = Math.round(((float) value
                    / (float) maxValue) * height);
            int yPos = height + yOffset - barHeight;

            Rectangle2D bar = new Rectangle2D.Float(
                    xPos, yPos, barWidth, barHeight);
            g2d.setColor(Color.black);
            g2d.draw(bar);
            if (value <= point) {
              g2d.setColor(Color.gray);
            } else {
              g2d.setColor(Color.BLUE);
            }
            g2d.fill(bar);
            xPos += barWidth;
          }
          g2d.dispose();
        }
      }
    }
}
