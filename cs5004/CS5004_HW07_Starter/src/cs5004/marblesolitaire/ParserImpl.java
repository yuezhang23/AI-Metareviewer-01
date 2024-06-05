package cs5004.marblesolitaire;

/**
 * This class implements the Parser interface.
 */
public class ParserImpl implements Parser {
  private final int[] data;

  /**
   * Construct the ParserImpl object by setting field values to specific data from parameter.
   * @param config input array of Strings
   */
  public ParserImpl(String[] config) {
    int armLength = -1;
    int row = -1;
    int col = -1;
    for (int i = 1; i < config.length; i++) {
      if (config[i].equals("size")) {
        armLength = Integer.parseInt(config[i + 1]);
      }
      if (config[i].equals("hole")) {
        row = Integer.parseInt(config[i + 1]);
        col = Integer.parseInt(config[i + 2]);
      }
    }
    this.data = setData(config[0], armLength, row, col);
  }

  @Override
  public int getArmLength() {
    return data[0];
  }

  @Override
  public int getPosRow() {
    return data[1];
  }

  @Override
  public int getPosCol() {
    return data[2];
  }

  private int[] setData(String model, int arm, int row, int col) {
    int[] data = new int[]{arm, row, col};
    if (model.equals("Triangle")) {
      if (arm == -1) {
        data[0] = 5;
      }
      if (row == -1) {
        data[1] = 0;
        data[2] = 0;
      }
    }
    if (!model.equals("Triangle")) {
      if (arm == -1) {
        data[0] = 3;
      }
      if (row == -1) {
        data[1] = (3 * data[0] - 2) / 2;
        data[2] = (3 * data[0] - 2) / 2;
      }
    }
    return data;
  }
}
