package cs5004.marblesolitaire.model.hw07;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;

public class EuropeanSolitaireModel extends AbstractSolitaireBaseModel
        implements MarbleSolitaireModel {
  private int side;

  public EuropeanSolitaireModel() {
    super(7, 3, 3);
    this.side = 3;
    this.setInvalidSlots(3);
  }

  public EuropeanSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(7, sRow, sCol);
    this.side = 3;
    this.setInvalidSlots(3);

    if (state.get(sRow * dimension + sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
  }

  public EuropeanSolitaireModel(int side) {
    super(3 * side - 2, (3 * side - 2) / 2, (3 * side - 2) / 2);
    if (isNotValidThickness(side)) {
      throw new IllegalArgumentException("thickness should be odd number");
    }
    this.side = side;
    this.setInvalidSlots(side);
  }

  public EuropeanSolitaireModel(int side, int sRow, int sCol)
          throws IllegalArgumentException {
    super(3 * side - 2, sRow, sCol);
    if (isNotValidThickness(side)) {
      throw new IllegalArgumentException("thickness should be odd number");
    }
    this.side = side;
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

  // helper method
  private boolean isNotValidThickness(int thickness) {
    return thickness == 1 || thickness % 2 != 1;
  }

}
