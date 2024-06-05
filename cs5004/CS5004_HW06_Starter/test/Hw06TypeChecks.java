import cs5004.marblesolitaire.model.hw05.EnglishSolitaireModel;

/**
 * Do not modify this file. This file should compile correctly with your code!
 */
public class Hw06TypeChecks {

  /**
   * Run the main method.
   * @param args command line arguments as an array of String objects
   */
  public static void main(String[] args) {
    Readable rd = null;
    Appendable ap = null;
    helper(new EnglishSolitaireModel(), rd, ap);
    helper(new EnglishSolitaireModel(3, 3), rd, ap);
  }

  private static void helper(cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel model,
           Readable rd,Appendable ap) {
    new cs5004.marblesolitaire.controller.MarbleSolitaireControllerImpl(model,
            new cs5004.marblesolitaire.view.MarbleSolitaireTextView(model,ap),rd);
  }

}
