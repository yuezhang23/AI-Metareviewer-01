package cs5004.marblesolitaire;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.model.hw05.EnglishSolitaireModel;
import cs5004.marblesolitaire.model.hw07.EuropeanSolitaireModel;
import cs5004.marblesolitaire.model.hw07.TriangleSolitaireModel;
import cs5004.marblesolitaire.view.MarbleSolitaireTextView;
import cs5004.marblesolitaire.view.MarbleSolitaireView;
import cs5004.marblesolitaire.view.TriangleSolitaireTextView;

public final class MarbleSolitaire {
  public static void main(String[] args) {
    MarbleSolitaireModel model1 = new EnglishSolitaireModel(3);
    MarbleSolitaireModel model2 = new EuropeanSolitaireModel(5);
    MarbleSolitaireModel model3 = new TriangleSolitaireModel(6, 3, 1);

    MarbleSolitaireView board1 = new MarbleSolitaireTextView(model1);
    MarbleSolitaireView board2 = new MarbleSolitaireTextView(model2);
    MarbleSolitaireView board3 = new TriangleSolitaireTextView(model3);


    System.out.println(board1.toString());
    System.out.println("\n");
    System.out.println(board2.toString());
    System.out.println("\n");
    System.out.println(board3.toString());
  }
}
