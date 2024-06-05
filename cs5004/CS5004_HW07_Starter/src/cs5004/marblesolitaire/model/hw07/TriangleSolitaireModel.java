package cs5004.marblesolitaire.model.hw07;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;

import static java.lang.Math.abs;

/**
 * This class implements the TriangleSolitaireModel with a collection of methods from both
 * MarbleSolitaireModel and MarbleSolitaireModelState interfaces, and by extending
 * from AbstractSolitaireBaseModel.
 */
public class TriangleSolitaireModel extends AbstractSolitaireBaseModel
        implements MarbleSolitaireModel {
  private int edge;

  /**
   * This constructor is initialized by setting triangle edge to 3 and the empty position
   * to the board center, and by rearranging invalid slots.
   */
  public TriangleSolitaireModel() {
    super(5, 0, 0);
    this.edge = 5;
    this.setInvalidSlots(edge);
  }

  /**
   * This constructor is initialized by setting triangle edge to 3 and the empty position
   * by input sRow and sCol and by rearranging invalid slots.
   * @param sRow the row number of the position
   * @param sCol the column number of the position
   * @throws IllegalArgumentException if empty slot is set on an invalid position
   */
  public TriangleSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(5, sRow, sCol);
    this.edge = 5;
    this.setInvalidSlots(edge);

    if (state.get(sRow * dimension + sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
  }

  /**
   * This constructor is initialized by setting input edge and the empty position
   * in the center, and by rearranging invalid slots.
   * @param edge the triangle edge length on the board
   * @throws IllegalArgumentException if edge is not positive
   */
  public TriangleSolitaireModel(int edge) throws IllegalArgumentException {
    super(edge, 0, 0);
    if (isEdgeTooSmall(edge)) {
      throw new IllegalArgumentException("Triangle too small");
    }
    this.edge = edge;
    this.setInvalidSlots(edge);
  }

  /**
   * This constructor is initialized by setting input edge, by setting the empty
   * position at input position, and by rearranging invalid slots.
   * @param edge the triangle edge length on the board
   * @param sRow the row number of the position
   * @param sCol sCol the column number of the position
   * @throws IllegalArgumentException if empty slot is set on an invalid position
   *        or edge is invalid
   */
  public TriangleSolitaireModel(int edge, int sRow, int sCol)
          throws IllegalArgumentException {
    super(edge, sRow, sCol);
    if (isEdgeTooSmall(edge)) {
      throw new IllegalArgumentException("Triangle too small");
    }
    this.edge = edge;
    this.setInvalidSlots(edge);

    if (state.get(sRow * dimension + sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    if (canMove(fromRow, fromCol, toRow, toCol)) {
      setMove(fromRow, fromCol, toRow, toCol);
    } else {
      throw new IllegalArgumentException("move is not possible");
    }
  }

  @Override
  public boolean isGameOver() {
    for (int i = 0; i < state.size(); i++) {
      int row = i / dimension;
      int col = i % dimension;
      if (canMove(row, col, row + 2, col) || canMove(row, col, row - 2, col)
              || canMove(row, col, row, col + 2) || canMove(row, col, row, col - 2)
              || canMove(row, col, row + 2, col + 2)
              || canMove(row, col, row - 2, col - 2)) {
        return false;
      }
    }
    return true;
  }

  // helper method
  private boolean canMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (isValidMove(fromRow, fromCol, toRow, toCol)) {
      return (abs(fromRow - toRow) == 2 && fromCol == toCol)
              || (abs(fromCol - toCol) == 2 && fromRow == toRow)
              || (abs(fromRow - toRow) == 2 && (fromRow - toRow) == (fromCol - toCol));
    }
    return false;
  }

  //helper method
  private void setInvalidSlots(int edge) {
    for (int i = 0; i < dimension; i++) {
      for (int j = dimension - 1; j > i; j--) {
        state.set(i * dimension + j, SlotState.Invalid);
      }
    }
  }

  // helper method
  private boolean isEdgeTooSmall(int edge) {
    return edge <= 0;
  }
}
