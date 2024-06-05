package cs5004.tictactoe;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * this class implements all the methods in the TicTcToe interface.
 */
public class TicTacToeModel implements TicTacToe {
  private Player[][] game;
  // add your implementation here

  /**
   * Construct the game by initializing the board with all element being null.
   */
  public TicTacToeModel() {
    this.game = new Player[][]{{null, null, null}, {null, null, null}, {null, null, null}};
  }

  @Override
  public String toString() {
    // Using Java stream API to save code:
    return Arrays.stream(getBoard()).map(
      row -> " " + Arrays.stream(row).map(
        p -> p == null ? " " : p.toString()).collect(Collectors.joining(" | ")))
          .collect(Collectors.joining("\n-----------\n"));
    // This is the equivalent code as above, but using iteration, and still using 
    // the helpful built-in String.join method.
  }

  @Override
  public void move(int r, int c) throws IllegalArgumentException, IllegalStateException {
    if (isNotValidPos(r, c)
            || this.getMarkAt(r, c) == Player.X || this.getMarkAt(r, c) == Player.O) {
      throw new IllegalArgumentException("space position invalid or occupied");
    }
    if (this.isGameOver()) {
      throw new IllegalStateException("game over");
    }
    game[r][c] = this.getTurn();
  }

  private boolean isNotValidPos(int r, int c) {
    return r < 0 || r > 2 || c < 0 || c > 2;
  }

  @Override
  public Player getTurn() {
    if (this.countNull() % 2 == 0) {
      return Player.O;
    } else {
      return Player.X;
    }
  }

  private int countNull() {
    int countNull = 0;
    for (int i = 0; i <= 2; i++) {
      countNull += (int) Arrays.stream(game[i]).filter(Objects::isNull).count();
    }
    return countNull;
  }

  @Override
  public boolean isGameOver() {
    return this.countNull() == 0 || getWinner() != null;
  }

  @Override
  public Player getWinner() {
    Player winner = null;
    for (Player player : Player.values()) {
      if ((game[0][0] == player && game[1][1] == player && game[2][2] == player)
              || (game[0][2] == player && game[1][1] == player && game[2][0] == player)) {
        winner = player;
        break;
      }

      for (int i = 0; i <= 2; i++) {
        if ((game[i][0] == player && game[i][1] == player && game[i][2] == player)
                || (game[0][i] == player && game[1][i] == player && game[2][i] == player)) {
          winner = player;
          break;
        }
      }
    }
    return winner;
  }

  @Override
  public Player[][] getBoard() {
    Player[][] board = new Player[][]{{null, null, null},{null, null, null},{null, null, null}};
    for (int i = 0; i <= 2; i++) {
      System.arraycopy(game[i], 0, board[i], 0, 3);
    }
    return board;
  }

  @Override
  public Player getMarkAt(int r, int c) {
    if (isNotValidPos(r, c)) {
      throw new IllegalArgumentException("space position invalid");
    }
    return this.game[r][c];
  }
}
