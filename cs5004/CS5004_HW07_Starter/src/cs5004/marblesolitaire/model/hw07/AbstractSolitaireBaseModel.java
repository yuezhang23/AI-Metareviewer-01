package cs5004.marblesolitaire.model.hw07;

import java.util.ArrayList;
import java.util.List;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState;

/**
 * This abstract class implements the MarbleSolitaireModelState interface
 * with a collection of methods.
 */
public abstract class AbstractSolitaireBaseModel implements MarbleSolitaireModelState {
  protected int dimension;
  protected List<SlotState> state;

  /**
   * This constructor is initialized by setting board dimension, the row position of
   * the empty slot and the col position of the empty slot.
   * @param dimension the length of the largest row/column on the board
   * @param sRow the row number of the position
   * @param sCol sCol the column number of the position
   * @throws IllegalArgumentException if inputs are invalid
   */
  public AbstractSolitaireBaseModel(int dimension, int sRow, int sCol)
          throws IllegalArgumentException {
    if (dimension < 1) {
      throw new IllegalArgumentException("invalid board dimension");
    }
    this.dimension = dimension;
    this.state = this.fullMarbleState(dimension);

    if (isNotValidPos(sRow, sCol)) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
    this.state.set(sRow * dimension + sCol, SlotState.Empty);
  }

  @Override
  public int getBoardSize() {
    return dimension;
  }

  @Override
  public SlotState getSlotAt(int row, int col)
          throws IllegalArgumentException {
    if (isNotValidPos(row, col)) {
      throw new IllegalArgumentException(
              "Invalid input position (" + row + "," + col + ")");
    }
    int i = row * dimension + col;
    return this.state.get(i);
  }

  @Override
  public int getScore() {
    return (int)this.state.stream().filter(b -> b == SlotState.Marble).count();
  }

  // helper method
  protected void setMove(int fromRow, int fromCol, int toRow, int toCol) {
    this.state.set(fromRow * dimension + fromCol, SlotState.Empty);
    this.state.set(toRow * dimension + toCol, SlotState.Marble);
    this.state.set((fromRow + toRow) / 2 * dimension + (fromCol + toCol) / 2, SlotState.Empty);
  }

  // helper method
  protected boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
    // invalid positions include:
    // 1. out of board dimension
    // 2. the position to canMove from is invalid or empty
    // 3. the position to canMove is occupied or invalid
    // 4. the position between canMove from and canMove to is empty
    return !isNotValidPos(fromRow, fromCol) && !isNotValidPos(toRow, toCol)
            && state.get(fromRow * dimension + fromCol) == SlotState.Marble
            && state.get(toRow * dimension + toCol) == SlotState.Empty
            && state.get((fromRow + toRow) / 2 * dimension
            + (fromCol + toCol) / 2) == SlotState.Marble;
  }

  // helper method
  private List<SlotState> fullMarbleState(int dimension) {
    List<SlotState> board = new ArrayList<>();

    for (int i = 0; i < dimension * dimension; i++) {
      board.add(i, SlotState.Marble);
    }
    return board;
  }

  // helper method
  private boolean isNotValidPos(int row, int col) {
    return (row < 0 || row >= dimension || col < 0 || col >= dimension);
  }
}
