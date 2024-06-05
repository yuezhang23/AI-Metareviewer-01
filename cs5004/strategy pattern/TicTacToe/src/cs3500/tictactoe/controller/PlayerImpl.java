package cs3500.tictactoe.controller;

import cs3500.tictactoe.model.Piece;
import cs3500.tictactoe.model.TicTacToeModel;
import cs3500.tictactoe.strategy.Posn;
import cs3500.tictactoe.strategy.PosnStrategy;

/**
 * A simple Player implementation that delegates most of its
 * complexity to a {@link cs3500.tictactoe.strategy.PosnStrategy}
 * for choosing where to play next
 */
public class PlayerImpl implements Player {
  private final Piece piece;
  private PosnStrategy posnStrategy;

  public PlayerImpl(Piece piece, PosnStrategy strategy) {
    this.piece = piece;
    this.posnStrategy = strategy;
  }

  @Override
  public Posn play(TicTacToeModel model) {
    return posnStrategy.choosePosn(model, this.piece);
  }

  @Override
  public Piece getPiece() {
    return this.piece;
  }
}
