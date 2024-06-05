import cs3500.tictactoe.controller.*;
import cs3500.tictactoe.model.*;
import cs3500.tictactoe.strategy.*;

/**
 * Driver for the TicTacToe game
 */
public class TicTacToe {
  public static void main(String[] args) {
    int h = 3;
    int w = 3;
    int g = 3;
    if (args.length == 3) {
      h = Integer.parseInt(args[0]);
      w = Integer.parseInt(args[1]);
      g = Integer.parseInt(args[2]);
    }
    TicTacToeModel model = new TTTModelImpl.Builder().setHeight(h).setWidth(w).setGoal(g).build();
    TicTacToeController control = new TTTController(model, System.out);
    control.addPlayer(new PlayerImpl(Piece.X, new FirstAvailableOpening()));
    control.addPlayer(new PlayerImpl(Piece.O, new BlockWin()));
    control.play();
  }
}
