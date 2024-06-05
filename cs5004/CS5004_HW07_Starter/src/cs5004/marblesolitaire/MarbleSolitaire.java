package cs5004.marblesolitaire;

import java.io.InputStreamReader;

import cs5004.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import cs5004.marblesolitaire.model.hw05.EnglishSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.model.hw07.EuropeanSolitaireModel;
import cs5004.marblesolitaire.model.hw07.TriangleSolitaireModel;
import cs5004.marblesolitaire.view.MarbleSolitaireTextView;
import cs5004.marblesolitaire.view.MarbleSolitaireView;
import cs5004.marblesolitaire.view.TriangleSolitaireTextView;

/**
 * This class implements the main method.
 */
public final class MarbleSolitaire {

  /**
   * This constructor implements the main method that runs the specific model.
   * @param args the String argument from user input
   */
  public static void main(String[] args) {
    MarbleSolitaireModel model;
    ParserImpl data = new ParserImpl(args);
    Readable in = new InputStreamReader(System.in);
    int arm = data.getArmLength();
    int row = data.getPosRow();
    int col = data.getPosCol();

    if (args[0].equals("Triangle")) {
      model = new TriangleSolitaireModel(arm, row, col);
      MarbleSolitaireView view = new TriangleSolitaireTextView(model);
      new MarbleSolitaireControllerImpl(model, view, in).playGame();
    }
    if (args[0].equals("English")) {
      model = new EnglishSolitaireModel(arm, row, col)  ;
      System.out.println(new MarbleSolitaireTextView(model));
    }
    if (args[0].equals("European")) {
      model = new EuropeanSolitaireModel(arm, row, col) ;
      System.out.println(new MarbleSolitaireTextView(model));
    }
  }
}
