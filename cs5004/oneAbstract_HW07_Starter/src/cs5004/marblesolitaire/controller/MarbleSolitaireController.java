package cs5004.marblesolitaire.controller;

/**
 * This interface represents the operations offered by the marble solitaire
 * model. One object of the model represents one game of marble solitaire.
 */
public interface MarbleSolitaireController {

  /**
   * Execute a single game of the marble solitaire given a EnglishSolitaire Model.
   * When the game is over, the playGame method ends.
   * @throws IllegalStateException if the controller fails to read input or transmit output.
   */
  void playGame() throws IllegalStateException;
}
