package cs5004.marblesolitaire.model.hw07;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;

import static java.lang.Math.abs;

/**
 * This abstract class implements the MarbleSolitaireModel interface
 * with a collection of methods and by extending methods in AbstractSolitaireBaseModel.
 */
public abstract class AbstractSolitaireModel extends AbstractSolitaireBaseModel
        implements MarbleSolitaireModel {
  protected int thickness;

  /**
   * This constructor is initialized by setting input thickness and by setting the empty
   * position at input position.
   * @param thickness the length of the largest row/column on the board
   * @param sRow the row number of the position
   * @param sCol sCol the column number of the position
   * @throws IllegalArgumentException if input thickness is too small or even
   */
  public AbstractSolitaireModel(int thickness, int sRow, int sCol)
          throws IllegalArgumentException {
    super(3 * thickness - 2, sRow, sCol);

    if (isNotValidThickness(thickness)) {
      throw new IllegalArgumentException("thickness should be odd number");
    }
    this.thickness = thickness;
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
          || canMove(row, col, row, col + 2) || canMove(row, col, row, col - 2)) {
        return false;
      }
    }
    return true;
  }

  // helper method
  private boolean canMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (isValidMove(fromRow, fromCol, toRow, toCol)) {
      return (abs(fromRow - toRow) == 2 && fromCol == toCol)
              || (abs(fromCol - toCol) == 2 && fromRow == toRow);
    }
    return false;
  }

  // helper method
  private boolean isNotValidThickness(int thickness) {
    return thickness == 1 || thickness % 2 != 1;
  }

}
