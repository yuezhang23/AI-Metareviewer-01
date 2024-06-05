import java.io.IOException;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;

class SolitaireMockModel implements MarbleSolitaireModel {
  private Appendable log;

  public SolitaireMockModel(Appendable out) {
    log = out;
  }

  public int getBoardSize() {
    return 2;
  }

  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    return SlotState.Marble;
  }

  public int getScore() {
    return 100;
  }

  public void move(int fromRow, int fromCol, int toRow, int toCol) throws
          IllegalArgumentException {
    try {
      log.append(String.format("passed in:\nfromRow: %d, fromCol: %d, toRow: %d, toCol: %d",
              fromRow, fromCol, toRow, toCol)).append(System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalArgumentException();
    }
  }

  public boolean isGameOver() {
    return false;
  }
}



