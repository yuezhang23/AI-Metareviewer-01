package cs3500.tictactoe.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.tictactoe.model.*;
import cs3500.tictactoe.strategy.Posn;

/**
 * Simple TicTacToe controller
 */
public class TTTController implements TicTacToeController {
  private final TicTacToeModel model;
  private int playerIndex;
  private final List<Player> players;
  private final Appendable output;

  public TTTController(TicTacToeModel model, Appendable output) {
    this.model = Objects.requireNonNull(model);
    // create a copy of the players
    this.players = new ArrayList<Player>();
    this.output = Objects.requireNonNull(output);
  }

  // what is the usage of add player to a list.
  // check who is next?
  @Override
  public void addPlayer(Player player) {
    this.players.add(Objects.requireNonNull(player));
  }

  @Override
  public void play() {
    this.playerIndex = 0;
    while (this.model.gameStatus() == Status.Playing) {
      this.printBoard();
      Posn pos = this.players.get(this.playerIndex).play(new ReadonlyTicTacToeModel(this.model));

      // why exception?????
      try {
        // this is the only place that model can be changed
        this.model.setPieceAt(pos.r, pos.c, this.players.get(this.playerIndex).getPiece());
        this.playerIndex = (this.playerIndex + 1) % this.players.size();
      } catch (Exception e) {

      }
    }
    this.printBoard();
    try {
      if (this.model.gameStatus() == Status.Won)
        output.append("Player " + this.model.getWinner().toString() + " won");
      else
        output.append("Tie game");
    } catch (IOException e) {

    }
  }

  public void printBoard() {
    try {
      output.append("\n");
      for (int r = 0; r < this.model.getHeight(); r++) {
        if (r > 0) {
          for (int c = 0; c < this.model.getWidth(); c++) {
            if (c > 0)
              output.append("+");
            output.append("-");
          }
          output.append("\n");
        }
        for (int c = 0; c < this.model.getWidth(); c++) {
          if (c > 0) output.append("|");
          output.append(this.model.getPieceAt(r, c).toString());
        }
        output.append("\n");
      }
    } catch (IOException e) {

    }
  }
}
