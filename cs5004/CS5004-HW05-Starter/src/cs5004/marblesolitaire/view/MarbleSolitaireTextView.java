package cs5004.marblesolitaire.view;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState.SlotState;

import static java.lang.Math.max;

/**
 * This class implements MarbleSolitaireView interface with a toString method.
 */
public class MarbleSolitaireTextView implements MarbleSolitaireView {
  private final MarbleSolitaireModelState board;

  /**
   * This constructor is initialized by setting the field value to a type of
   * the MarbleSolitaireModelState interface.
   * @param model the input type of the MarbleSolitaireModelState interface
   * @throws IllegalArgumentException if input is null
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState model)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("null input");
    }
    this.board = model;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    int size = this.board.getBoardSize();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (this.board.getSlotAt(i, j) == SlotState.Marble) {
          result.append("O ");
        } else if (this.board.getSlotAt(i, j) == SlotState.Empty) {
          result.append("_ ");
        } else {
          result.append("  ");
        }
      }
      int temp = max(result.lastIndexOf("O"), result.lastIndexOf("_"));
      result.delete(temp + 1, result.length());
      result.append("\n");
    }
    result.deleteCharAt(result.length() - 1);
    return result.toString();
  }
}
