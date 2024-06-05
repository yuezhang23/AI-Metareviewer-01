package cs5004.marblesolitaire.model.hw07;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;

/**
 * This class implements the EuropeanSolitaireModel with a collection of methods from both
 * MarbleSolitaireModel and MarbleSolitaireModelState interfaces, and by extending
 * from AbstractSolitaireModel.
 */
public class EuropeanSolitaireModel extends AbstractSolitaireModel
        implements MarbleSolitaireModel {

  /**
   * This constructor is initialized by setting side length to 3 and the empty position
   * to the board center, and by rearranging invalid slots.
   */
  public EuropeanSolitaireModel() {
    super(3, 3, 3);
    this.setInvalidSlots(3);
  }

  /**
   * This constructor is initialized by setting side length to 3 and the empty position
   * by input sRow and sCol and by rearranging invalid slots.
   * @param sRow the row number of the position
   * @param sCol the column number of the position
   * @throws IllegalArgumentException if empty slot is set on an invalid position
   */
  public EuropeanSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(3, sRow, sCol);
    this.setInvalidSlots(3);

    if (state.get(sRow * dimension + sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
  }

  /**
   * This constructor is initialized by setting input side and the empty position
   * in the center, and by rearranging invalid slots.
   * @param side the length of the largest row/column on the board
   */
  public EuropeanSolitaireModel(int side) {
    super(side, (3 * side - 2) / 2, (3 * side - 2) / 2);
    this.setInvalidSlots(side);
  }

  /**
   * This constructor is initialized by setting input side, by setting the empty
   * position at input position, and by rearranging invalid slots.
   * @param side the side length of the largest row/column on the board
   * @param sRow the row number of the position
   * @param sCol sCol the column number of the position
   * @throws IllegalArgumentException if empty slot is set on an invalid position
   */
  public EuropeanSolitaireModel(int side, int sRow, int sCol)
          throws IllegalArgumentException {
    super(side, sRow, sCol);
    this.setInvalidSlots(side);

    if (state.get(sRow * dimension + sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
  }

  // helper method
  private void setInvalidSlots(int side) {
    for (int i = 0; i < side; i++) {
      for (int j = side - 2 - i; j >= 0; j --) {
        state.set(i * dimension + j, SlotState.Invalid);
        state.set(i * dimension + j + i + 2 * side - 1, SlotState.Invalid);
      }

      for (int k = 0; k < i; k++) {
        state.set((2 * side - 2 + i) * dimension + k, SlotState.Invalid);
        state.set((2 * side - 1 + i) * dimension - 1 - k, SlotState.Invalid);
      }
    }
  }

}
