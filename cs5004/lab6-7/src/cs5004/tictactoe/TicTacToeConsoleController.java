package cs5004.tictactoe;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class implements the TicTacToeController interface.
 */
public class TicTacToeConsoleController implements TicTacToeController {
  final Readable in;
  final Appendable out;
  private int[] pos;

  /**
   * Constructs a tic tac toe game by initializing Readable and Appendable parameters.
   * @param in to read inputs
   * @param out to transmit outputs
   */
  public TicTacToeConsoleController(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
    this.pos = new int[]{0, 0};
  }

  @Override
  public void playGame(TicTacToe m) {
    try {
      Scanner scan = new Scanner(this.in);
      int i = 0;
      this.out.append(m.toString()).append(System.lineSeparator())
              .append(String.format("Enter a move for %s:\n", m.getTurn()));

      while (scan.hasNext()) {
        String temp = scan.next();
        if (temp.equals("q") || temp.equals("Q")) {
          break;
        }
        else if (temp.contains(".") || !Character.isDigit(temp.charAt(0))) {
          this.out.append("input invalid\n");
        }
        else {
          pos[i] = Integer.parseInt(temp);
          i ++;

          if (i == 2) {
            try {
              m.move(pos[0] - 1, pos[1] - 1);
            } catch (IllegalStateException | IllegalArgumentException e) {
              System.out.println(e.getMessage() + System.lineSeparator());
            }
            this.out.append(m.toString()).append(System.lineSeparator());
            if (m.isGameOver()) {
              break;
            }
            this.out.append(String.format("Enter a move for %s:\n", m.getTurn()));
            i = 0;
          }
        }
      }
      if (!m.isGameOver()) {
        this.out.append("Game quit! Ending game state:\n");
        this.out.append(m.toString()).append(System.lineSeparator());
      } else {
        this.out.append("Game is over! ");
        if (m.getWinner() != null) {
          this.out.append(String.format("%s wins", m.getWinner()));
        } else {
          this.out.append("Tie game.");
        }
      }

    } catch (IOException | NoSuchElementException e) {
      throw new IllegalStateException("input invalid\n");
    }
  }


}
