import org.junit.Before;
import org.junit.Test;

import cs5004.marblesolitaire.model.hw05.EnglishSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.view.MarbleSolitaireTextView;
import cs5004.marblesolitaire.view.MarbleSolitaireView;

import static org.junit.Assert.assertEquals;

/**
 * This is the test for all methods implements in the MarbleSolitaireView interfaces.
 */
public class TestMarbleSolitaireView {
  private MarbleSolitaireModel model1;
  private MarbleSolitaireView view1;

  @Before
  public void setUp() {
    model1 = new EnglishSolitaireModel();
    view1 = new MarbleSolitaireTextView(model1);
  }

  @Test
  public void testConstruct() {
    assertEquals("    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O", view1.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstruct1() {
    new MarbleSolitaireTextView(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstruct2() {
    new MarbleSolitaireTextView(model1, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstruct3() {
    new MarbleSolitaireTextView(null, null);
  }
}
