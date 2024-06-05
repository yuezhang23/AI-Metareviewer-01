package cs3500.tictactoe.strategy;

import cs3500.tictactoe.model.Piece;
import cs3500.tictactoe.model.TicTacToeModel;

/**
 * A Strategy: find the first (topmost-leftmost) available spot
 */
public class FirstAvailableOpening implements PosnStrategy {
  @Override
  public Posn choosePosn(TicTacToeModel model, Piece player) {
    for (int r = 0; r < model.getHeight(); r++) {
      for (int c = 0; c < model.getWidth(); c++) {
        if (model.getPieceAt(r, c) == Piece.EMPTY) {
          return new Posn(r, c);
        }
      }
    }
    return null;
  }
}
