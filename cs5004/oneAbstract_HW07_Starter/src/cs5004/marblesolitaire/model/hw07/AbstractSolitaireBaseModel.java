package cs5004.marblesolitaire.model.hw07;

import java.util.ArrayList;
import java.util.List;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState;

import static java.lang.Math.abs;


public abstract class AbstractSolitaireBaseModel implements MarbleSolitaireModel {
  protected int dimension;
  protected List<SlotState> state;


  /**
   * This constructor is initialized by setting input dimension and by setting the empty
   * position at input position.
   * @param dimension the length of the largest row/column on the board
   * @param sRow the row number of the position
   * @param sCol sCol the column number of the position
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

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws
          IllegalArgumentException {

    // invalid positions include:
    // 1. out of board dimension
    // 2. the position to move from is invalid or empty
    // 3. the position to move is occupied or invalid
    // 4. the position between move from and move to is empty
    if (isNotValidPos(fromRow, fromCol)
            || isNotValidPos(toRow, toCol)
            || state.get(fromRow * dimension + fromCol) != SlotState.Marble
            || state.get(toRow * dimension + toCol) != SlotState.Empty
            || state.get((fromRow + toRow) / 2 * dimension + (fromCol + toCol) / 2)
            != SlotState.Marble) {
      throw new IllegalArgumentException("invalid move");
    }

    if ((abs(fromRow - toRow) == 2 && fromCol == toCol)
            || (abs(fromCol - toCol) == 2 && fromRow == toRow)) {
      this.state.set(fromRow * dimension + fromCol, SlotState.Empty);
      this.state.set(toRow * dimension + toCol, SlotState.Marble);
      this.state.set((fromRow + toRow) / 2 * dimension + (fromCol + toCol) / 2, SlotState.Empty);
    } else {
      // if try to move more than 2 marbles vertically or horizontally
      throw new IllegalArgumentException("move is not possible");
    }
  }

  @Override
  public boolean isGameOver() {
    List<SlotState> copyBoard = new ArrayList<>(state);
    int preScore = this.getScore();
    boolean check = true;

    for (int i = 0; i < state.size(); i++) {
      if (state.get(i) == SlotState.Marble) {
        try {
          move(i / dimension, i % dimension, i / dimension + 2, i % dimension);
        } catch (IllegalArgumentException ignored) {
        }
        try {
          move(i / dimension, i % dimension, i / dimension - 2, i % dimension);
        } catch (IllegalArgumentException ignored) {
        }
        try {
          move(i / dimension, i % dimension, i / dimension, i % dimension + 2);
        } catch (IllegalArgumentException ignored) {
        }
        try {
          move(i / dimension, i % dimension, i / dimension, i % dimension - 2);
        } catch (IllegalArgumentException ignored) {
        }
        if (this.getScore() != preScore) {
          state = new ArrayList<>(copyBoard);
          check = false;
          break;
        }
      }
    }
    return check;
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
