package cs5004.marblesolitaire.view;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState.*;

import static java.lang.Math.max;

public class TriangleSolitaireTextView extends MarbleSolitaireTextView
        implements MarbleSolitaireView {

  public TriangleSolitaireTextView(MarbleSolitaireModelState model) {
    super(model);
  }

  public TriangleSolitaireTextView(MarbleSolitaireModelState model, Appendable out) {
    super(model, out);
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    int size = board.getBoardSize();
    for (int i = 0; i < size; i++) {
      result.append(" ".repeat (size - i - 1));
      for (int j = 0; j <= i; j++) {
        if (this.board.getSlotAt(i, j) == SlotState.Marble) {
          result.append("O ");
        } else {
          result.append("_ ");
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
