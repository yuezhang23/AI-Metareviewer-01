import cs5004.marblesolitaire.model.hw05.EnglishSolitaireModel;
import cs5004.marblesolitaire.model.hw07.TriangleSolitaireModel;

/**
 * Do not modify this file. This file should compile correctly with your code!
 */
public class Hw07TypeChecks {

  public static void main(String[] args) {
    Readable rd = null;
    Appendable ap = null;
    helperMarble(new EnglishSolitaireModel(),rd, ap);
    helperTriangle(new TriangleSolitaireModel(3, 3),rd, ap);
  }

  private static void helperMarble(cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel model,Readable rd,Appendable ap) {
    new cs5004.marblesolitaire.controller.MarbleSolitaireControllerImpl(model,new cs5004.marblesolitaire.view.MarbleSolitaireTextView(model,ap),rd);
  }

  private static void helperTriangle(cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel model,Readable rd,Appendable ap) {
    new cs5004.marblesolitaire.controller.MarbleSolitaireControllerImpl(model, new cs5004.marblesolitaire.view.MarbleSolitaireTextView(model,ap),rd);
  }

}
