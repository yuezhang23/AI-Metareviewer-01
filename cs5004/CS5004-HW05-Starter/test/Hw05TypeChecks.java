import cs5004.marblesolitaire.model.hw05.EnglishSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;

/**
 * Do not modify this file. This file should compile correctly with your code!
 */
public class Hw05TypeChecks {

  /**
   * A sample main method.
   *
   * @param args the program arguments
   */
  public static void main(String[] args) {
    helper(new EnglishSolitaireModel());
    helper(new EnglishSolitaireModel(2, 2));
    helper(new EnglishSolitaireModel(5));
    helper(new EnglishSolitaireModel(3, 0, 4));
  }

  private static void helper(MarbleSolitaireModel model) {
    model.move(1, 3, 3, 3);
  }

}
