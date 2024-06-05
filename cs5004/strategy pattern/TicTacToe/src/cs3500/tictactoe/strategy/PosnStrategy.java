package cs3500.tictactoe.strategy;

import cs3500.tictactoe.model.Piece;
import cs3500.tictactoe.model.TicTacToeModel;

/**
 * A Strategy interface for choosing where to play next for the given player
 */
public interface PosnStrategy {
  Posn choosePosn(TicTacToeModel model, Piece player);
}
