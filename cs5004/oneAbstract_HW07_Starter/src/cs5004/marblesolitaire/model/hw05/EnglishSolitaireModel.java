package cs5004.marblesolitaire.model.hw05;

import java.util.ArrayList;
import java.util.List;

import cs5004.marblesolitaire.model.hw07.AbstractSolitaireBaseModel;

/**
 * This class implements the MarbleSolitaireModel and MarbleSolitaireModelState interface
 * with a collection of methods.
 */
public class EnglishSolitaireModel extends AbstractSolitaireBaseModel
        implements MarbleSolitaireModel {
  private int thickness;

  public EnglishSolitaireModel() {
    super(7, 3, 3);
    this.thickness = 3;
    this.setInvalidSlots(3);
  }

  /**
   * This constructor is initialized by setting thickness to 3 and the empty position
   * by input sRow and sCol.
   * @param sRow the row number of the position
   * @param sCol the column number of the position
   * @throws IllegalArgumentException if input data is invalid by board size
   */
  public EnglishSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(7, sRow, sCol);
    this.thickness = 3;
    this.setInvalidSlots(3);

    if (state.get(sRow * dimension + sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
  }

  /**
   * This constructor is initialized by setting input thickness and the empty position
   * in the center.
   * @param thickness the length of the largest row/column on the board
   */
  public EnglishSolitaireModel(int thickness) {
    super(3 * thickness - 2, (3 * thickness - 2) / 2, (3 * thickness - 2) / 2);
    if (isNotValidThickness(thickness)) {
      throw new IllegalArgumentException("thickness should be odd number");
    }
    this.thickness = thickness;
    this.setInvalidSlots(thickness);
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
    super(3 * thickness - 2, sRow, sCol);
    if (isNotValidThickness(thickness)) {
      throw new IllegalArgumentException("thickness should be odd number");
    }
    this.thickness = thickness;
    this.setInvalidSlots(thickness);

    if (state.get(sRow * dimension + sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
  }

  // helper method
  private void setInvalidSlots(int thickness) {
    List<Integer> tempIndex = new ArrayList<>();
    for (int i = 0; i < thickness - 1; i++) {
      tempIndex.add(i);
      tempIndex.add(2 * thickness - 1 + i);
    }
    for (int i = 0; i < tempIndex.size(); i++) {
      for (Integer index : tempIndex) {
        this.state.set(tempIndex.get(i) * dimension + index, SlotState.Invalid);
      }
    }
  }

  // helper method
  private boolean isNotValidThickness(int thickness) {
    return thickness == 1 || thickness % 2 != 1;
  }
}
