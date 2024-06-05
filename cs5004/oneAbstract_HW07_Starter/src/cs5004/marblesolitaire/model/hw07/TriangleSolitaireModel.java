package cs5004.marblesolitaire.model.hw07;

import java.util.ArrayList;
import java.util.List;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;

import static java.lang.Math.abs;

public class TriangleSolitaireModel extends AbstractSolitaireBaseModel
        implements MarbleSolitaireModel {
  private int edge;

  public TriangleSolitaireModel() {
    super(5, 0, 0);
    this.edge = 5;
    this.setInvalidSlots(edge);
  }

  public TriangleSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(5, sRow, sCol);
    this.edge = 5;
    this.setInvalidSlots(edge);

    if (state.get(sRow * dimension + sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
  }

  public TriangleSolitaireModel(int edge) throws IllegalArgumentException {
    super(edge, 0, 0);
    this.edge = edge;
    this.setInvalidSlots(edge);
  }

  public TriangleSolitaireModel(int edge, int sRow, int sCol)
          throws IllegalArgumentException {
    super(edge, sRow, sCol);
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
    try {
      super.move(fromRow, fromCol, toRow, toCol);
    } catch (IllegalArgumentException e) {
      if (abs(fromRow - toRow) == 2 && (fromRow - toRow) == (fromCol - toCol)) {
        this.state.set(fromRow * dimension + fromCol, SlotState.Empty);
        this.state.set(toRow * dimension + toCol, SlotState.Marble);
        this.state.set((fromRow + toRow) / 2 * dimension + (fromCol + toCol) / 2, SlotState.Empty);
      } else {
        // if try to move more than 2 marbles vertically or horizontally
        throw new IllegalArgumentException("move is not possible");
      }
    }
  }

  @Override
  public boolean isGameOver() {
    if (!super.isGameOver()) {
      return false;
    }

    // try diagonal move on current board
    boolean check = true;
    List<SlotState> copyBoard = new ArrayList<>(state);
    int preScore = this.getScore();
    for (int i = 0; i < state.size(); i++) {
      if (state.get(i) == SlotState.Marble) {
        try {
          move(i / dimension, i % dimension, i / dimension + 2, i % dimension + 2);
        } catch (IllegalArgumentException ignored) {
        }
        try {
          move(i / dimension, i % dimension, i / dimension - 2, i % dimension - 2);
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

  //helper
  private void setInvalidSlots(int edge) {
    for (int i = 0; i < dimension; i++) {
      for (int j = dimension - 1; j > i; j--) {
        state.set(i * dimension + j, SlotState.Invalid);
      }
    }
  }
}
