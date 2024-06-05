package cs5004.marblesolitaire.controller;

import java.io.IOException;
import java.util.Scanner;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.view.MarbleSolitaireView;

/**
 * This class implements the MarbleSolitaireController interface.
 */
public class MarbleSolitaireControllerImpl implements MarbleSolitaireController {
  private final MarbleSolitaireModel model;
  private final MarbleSolitaireView view;
  final Readable in;
  private final int[] pos;

  /**
   * Constructs the controller by coupling a MarbleSolitaire model with
   * a MarbleSolitaireView and a Readable object to read input.
   * @param model a Marble Solitaire model to play with
   * @param view  a MarbleSolitaireView object to transmit output
   * @param input a Readable object to read input
   * @throws IllegalArgumentException if any input parameter is null
   */
  public MarbleSolitaireControllerImpl(MarbleSolitaireModel model,
                                       MarbleSolitaireView view, Readable input)
          throws IllegalArgumentException {
    if (model == null || view == null || input == null) {
      throw new IllegalArgumentException("");
    }
    this.in = input;
    this.model = model;
    this.view = view;
    this.pos = new int[]{-1, -1, -1, -1};
  }

  @Override
  public void playGame() throws IllegalStateException {
    this.renderView();
    Scanner scan = new Scanner(this.in);

    String t = "";
    int i = 0;
    while (scan.hasNext()) {
      String temp = scan.next();
      if (temp.equals("q") || temp.equals("Q")) {
        t = "q";
        this.renderQuit();
        break;
      }
      else {
        try {
          pos[i] = Integer.parseInt(temp);
          i++;
        } catch (NumberFormatException ignored) {
        }

        // read four integers each time
        if (i == 4) {
          try {
            this.model.move(pos[0] - 1, pos[1] - 1, pos[2] - 1, pos[3] - 1);
            this.renderView();
          } catch (IllegalArgumentException e) {
            System.out.printf("Invalid move. Play again. %s\n", e.getMessage());
          }
          if (model.isGameOver()) {
            this.renderGameOver();
            break;
          } else {
            i = 0;
          }
        }
      }
    }
    // when out of loop not by quitting or game over, without any readable input
    if (!scan.hasNext() && !model.isGameOver() && !t.equals("q")) {
      throw new IllegalStateException("Failed to read from input");
    }
  }

  // helper method to render state of board in each move
  private void renderView() throws IllegalStateException {
    try {
      this.view.renderBoard();
      this.view.renderMessage(String.format("Score: %d" + System.lineSeparator(),
              this.model.getScore()));
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to view.");
    }
  }

  // helper method to render state of board when game is over
  private void renderGameOver() throws IllegalStateException {
    try {
      this.view.renderMessage("Game over!\n");
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to view.");
    }
    this.renderView();
  }

  // helper method to render state of board when quit
  private void renderQuit() throws IllegalStateException {
    try {
      this.view.renderMessage("Game quit!\n");
      this.view.renderMessage("State of game when quit:\n");
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to view.");
    }
    this.renderView();
  }

}
