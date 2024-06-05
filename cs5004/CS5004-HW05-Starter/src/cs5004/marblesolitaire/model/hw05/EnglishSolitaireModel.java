package cs5004.marblesolitaire.model.hw05;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;

/**
 * This class implements the MarbleSolitaireModel and MarbleSolitaireModelState interface
 * with a collection of methods.
 */
public class EnglishSolitaireModel implements MarbleSolitaireModel {
  private int thickness;
  private List<SlotState> state;

  /**
   * This constructor is initialized by setting thickness to 3 and the empty position
   * in the center.
   */
  public EnglishSolitaireModel() {
    this.thickness = 3;
    this.state = this.defaultState(thickness);
//    this.state.set(3 * this.getBoardSize() + 3, SlotState.Empty); // 7
    this.state.set(this.getBoardSize() * this.getBoardSize() / 2, SlotState.Empty); // 49, 24
  }

  /**
   * This constructor is initialized by setting thickness to 3 and the empty position
   * by input sRow and sCol.
   * @param sRow the row number of the position
   * @param sCol the column number of the position
   * @throws IllegalArgumentException if input data is invalid by board size
   */
  public EnglishSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    this.thickness = 3;
    this.state = this.defaultState(thickness);
    if (isNotValidPos(thickness, sRow, sCol)
            || this.getSlotAt(sRow, sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
    this.state.set(sRow * this.getBoardSize() + sCol, SlotState.Empty);
  }

  /**
   * This constructor is initialized by setting input thickness and the empty position
   * in the center.
   * @param thickness the length of the largest row/column on the board
   * @throws IllegalArgumentException if thickness is too small or not an odd number
   */
  public EnglishSolitaireModel(int thickness) throws IllegalArgumentException {
    if (isNotValidThickness(thickness)) {
      throw new IllegalArgumentException("thickness should be odd number");
    }
    this.thickness = thickness;
    this.state = this.defaultState(thickness);
    int size = state.size();
    this.state.set(size / 2, SlotState.Empty);
  }

  /**
   * This constructor is initialized by setting input thickness and by setting the empty
   * position at input position.
   * @param thickness the length of the largest row/column on the board
   * @param sRow the row number of the position
   * @param sCol sCol the column number of the position
   * @throws IllegalArgumentException if input thickness is too small and
   *        not an odd number or the input position is out of board range
   */
  public EnglishSolitaireModel(int thickness, int sRow, int sCol)
          throws IllegalArgumentException {
    if (isNotValidThickness(thickness)) {
      throw new IllegalArgumentException("thickness should be odd number");
    }
    this.thickness = thickness;
    this.state = this.defaultState(thickness);
    if (isNotValidPos(thickness, sRow, sCol)
            || this.getSlotAt(sRow, sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
    this.state.set(sRow * this.getBoardSize() + sCol, SlotState.Empty);
  }

  @Override
  public int getBoardSize() {
    return 3 * thickness - 2;
  }

  // helper method
  private List<SlotState> defaultState(int thickness) {
    int size = this.getBoardSize(); // 7
    this.state = new ArrayList<>();

    for (int i = 0; i < size * size; i++) {
      this.state.add(i, SlotState.Marble);
    }

    List<Integer> tempIndex = new ArrayList<>();
    for (int i = 0; i < thickness - 1; i++) {
      tempIndex.add(i);
      tempIndex.add(2 * thickness - 1 + i);
    }
    for (int i = 0; i < tempIndex.size(); i++) {
      for (Integer index : tempIndex) {
        this.state.set(tempIndex.get(i) * size + index, SlotState.Invalid);
      }
    }
    return this.state;
  }

  // helper method
  private boolean isNotValidPos(int thickness, int row, int col) {
    int size = 3 * thickness - 2;
    return (row < 0 || row >= size || col < 0 || col >= size);
  }

  // helper method
  private boolean isNotValidThickness(int thickness) {
    return thickness == 1 || thickness % 2 != 1;
  }

  @Override
  public MarbleSolitaireModelState.SlotState getSlotAt(int row, int col)
          throws IllegalArgumentException {
    if (isNotValidPos(this.thickness, row, col)) {
      throw new IllegalArgumentException(
              "Invalid input position (" + row + "," + col + ")");
    }
    int i = row * this.getBoardSize() + col;
    return this.state.get(i);
  }

  @Override
  public int getScore() {
    return (int)this.state.stream().filter(b -> b == SlotState.Marble).count();
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws
          IllegalArgumentException {
    List<Integer> midpoint = List.of((fromRow + toRow) / 2, (fromCol + toCol) / 2);
    int size = getBoardSize();

    if (isNotValidPos(this.thickness, fromRow, fromCol)
            || isNotValidPos(this.thickness, toRow, toCol)
            || this.getSlotAt(fromRow, fromCol) != SlotState.Marble
            || this.getSlotAt(midpoint.get(0), midpoint.get(1)) != SlotState.Marble
            || this.getSlotAt(toRow, toCol) != SlotState.Empty) {
      throw new IllegalArgumentException(
              "invalid position");
    }

    if ((abs(fromRow - toRow) == 2 && fromCol == toCol)
              || (abs(fromCol - toCol) == 2 && fromRow == toRow)) {
      this.state.set(fromRow * size + fromCol, SlotState.Empty);
      this.state.set(toRow * size + toCol, SlotState.Marble);
      this.state.set(
              midpoint.get(0) * size + midpoint.get(1), SlotState.Empty);
    } else {
      throw new IllegalArgumentException("move is not possible");
    }
  }

  @Override
  public boolean isGameOver() {
    EnglishSolitaireModel copyBoard = new EnglishSolitaireModel(thickness);
    int size = this.getBoardSize();
    for (int i = 0; i < copyBoard.state.size(); i++) {
      copyBoard.state.set(i, this.getSlotAt(i / size, i % size));
    }

    for (int i = 0; i < copyBoard.state.size(); i++) {
      if (copyBoard.state.get(i) == SlotState.Marble) {
        try {
          copyBoard.move(i / size, i % size, i / size + 2, i % size);
        } catch (IllegalArgumentException ignored) {
        }
        try {
          copyBoard.move(i / size, i % size, i / size - 2, i % size);
        } catch (IllegalArgumentException ignored) {
        }
        try {
          copyBoard.move(i / size, i % size, i / size, i % size + 2);
        } catch (IllegalArgumentException ignored) {
        }
        try {
          copyBoard.move(i / size, i % size, i / size, i % size - 2);
        } catch (IllegalArgumentException ignored) {
        }
      }
    }
    return copyBoard.getScore() == this.getScore();
  }

}
