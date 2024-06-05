package cs3500.tictactoe.strategy;

import java.util.Scanner;

import cs3500.tictactoe.model.Piece;
import cs3500.tictactoe.model.TicTacToeModel;

/**
 * A Strategy: ask the user where to play next
 */
public class PromptUser implements PosnStrategy {
  Scanner input;
  PromptUser() { this(new Scanner(System.in)); }
  PromptUser(Scanner input) {
    this.input = input;
  }
  @Override
  public Posn choosePosn(TicTacToeModel model, Piece player) {
    System.out.println("Enter a row and column");
    int r = input.nextInt();
    int c = input.nextInt();
    return new Posn(r, c);
  }
}
