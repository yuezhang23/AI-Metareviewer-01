package cs5004.marblesolitaire.view;

import java.io.IOException;
import java.util.Objects;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState.SlotState;

import static java.lang.Math.max;

/**
 * This class implements MarbleSolitaireView interface with a toString method.
 */
public class MarbleSolitaireTextView implements MarbleSolitaireView {
  protected final MarbleSolitaireModelState board;
  final Appendable out;

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
    this.out = System.out;
  }

  /**
   * This constructor is initialized by setting the field value to a type of
   * the MarbleSolitaireModelState interface.
   * @param model the input model as a type of the MarbleSolitaireModelState interface
   * @param out the output from the system while  running the program
   * @throws IllegalArgumentException if input is null
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState model, Appendable out)
          throws IllegalArgumentException {
    if (model == null || out == null) {
      throw new IllegalArgumentException("null input");
    }
    this.board = model;
    this.out = out;
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

  @Override
  public void renderBoard() throws IOException {
    Objects.requireNonNull(this.board);
    this.out.append(this.toString()).append(System.lineSeparator());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    Objects.requireNonNull(message);
    this.out.append(message);
  }

}
