import org.junit.Before;
import org.junit.Test;

import cs5004.marblesolitaire.model.hw05.EnglishSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState.SlotState;
import cs5004.marblesolitaire.view.MarbleSolitaireTextView;
import cs5004.marblesolitaire.view.MarbleSolitaireView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This is the test for all methods implements in the MarbleSolitaireView,
 * MarbleSolitaireModelState and MarbleSolitaireModel interfaces.
 */
public class TestEnglishSolitaireImpl {
  private MarbleSolitaireModel model1;
  private MarbleSolitaireModel model2;
  private MarbleSolitaireModel model3;
  private MarbleSolitaireModel model4;
  private MarbleSolitaireView board1;
  private MarbleSolitaireView board2;
  private MarbleSolitaireView board3;
  private MarbleSolitaireView board4;

  @Before
  public void setUp() {
    model1 = new EnglishSolitaireModel();
    board1 = new MarbleSolitaireTextView(model1);
    model2 = new EnglishSolitaireModel(0, 4);
    board2 = new MarbleSolitaireTextView(model2);
    model3 = new EnglishSolitaireModel(5);
    board3 = new MarbleSolitaireTextView(model3);
    model4 = new EnglishSolitaireModel(7, 7, 14);
    board4 = new MarbleSolitaireTextView(model4);
  }

  @Test
  public void testCons1() {
    String expected
            = "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O";
    assertEquals(expected, board1.toString());
    assertEquals(7, model1.getBoardSize());
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 3));
  }

  @Test
  public void testCons2() {
    String expected
            = "    O O _\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O";
    assertEquals(expected, board2.toString());
    assertEquals(7, model2.getBoardSize());
    assertEquals(SlotState.Empty, model2.getSlotAt(0, 4));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConst21() {
    new EnglishSolitaireModel(-1, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConst22() {
    new EnglishSolitaireModel(2, 7);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConst23() {
    new EnglishSolitaireModel(1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConst24() {
    new EnglishSolitaireModel(5, 1);
  }

  @Test
  public void testCons3() {
    String expected
            = "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O _ O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O";
    assertEquals(expected, board3.toString());
    assertEquals(13, model3.getBoardSize());
    assertEquals(SlotState.Empty, model3.getSlotAt(6, 6));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConst31() {
    // thickness too small to start a game
    new EnglishSolitaireModel(1);
  }

  @Test (expected = IllegalArgumentException.class)
  // thickness being even
  public void testInvalidConst32() {
    new EnglishSolitaireModel(4);
  }

  @Test (expected = IllegalArgumentException.class)
  // invalid thickness
  public void testInvalidConst33() {
    new EnglishSolitaireModel(-2);
  }

  @Test
  public void testCons4() {
    String expected
            = "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O _ O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O\n"
            + "            O O O O O O O";
    assertEquals(expected, board4.toString());
    assertEquals(19, model4.getBoardSize());
    assertEquals(SlotState.Empty, model4.getSlotAt(7, 14));
  }

  @Test (expected = IllegalArgumentException.class)
  // set empty cell in invalid right-up position
  public void testInvalidConst47() {
    new EnglishSolitaireModel(3, 0, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  // set empty cell in invalid right-down position
  public void testInvalidConst41() {
    new EnglishSolitaireModel(3, 5, 6);
  }

  @Test (expected = IllegalArgumentException.class)
  // set empty cell in invalid left-up position
  public void testInvalidConst46() {
    new EnglishSolitaireModel(3, 1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  // set empty cell in invalid left-down position
  public void testInvalidConst48() {
    new EnglishSolitaireModel(3, 5, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  // set empty cell out of bound
  public void testInvalidConst42() {
    new EnglishSolitaireModel(5, 13, 6);
  }

  @Test (expected = IllegalArgumentException.class)
  // thickness being an even number
  public void testInvalidConst43() {
    new EnglishSolitaireModel(4, 5, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  // invalid thickness
  public void testInvalidConst44() {
    new EnglishSolitaireModel(-4, 5, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  // invalid thickness
  public void testInvalidConst45() {
    new EnglishSolitaireModel(1, 0, 0);
  }

  @Test
  public void testGetBoardSize() {
    assertEquals(7, model1.getBoardSize());
    assertEquals(7, model2.getBoardSize());
    assertEquals(13, model3.getBoardSize());
    assertEquals(19, model4.getBoardSize());
  }

  @Test
  public void testScore() {
    assertEquals(32, model1.getScore());
    assertEquals(32, model2.getScore());
    assertEquals(104, model3.getScore());
    assertEquals(216, model4.getScore());
  }

  @Test
  public void testGetSlot() {
    assertEquals(SlotState.Marble, model1.getSlotAt(2, 1));
    assertEquals(SlotState.Marble, model2.getSlotAt(3, 4));
    assertEquals(SlotState.Marble, model3.getSlotAt(6, 11));
    assertEquals(SlotState.Marble, model4.getSlotAt(6, 14));

    assertEquals(SlotState.Empty, model2.getSlotAt(0, 4));
    assertEquals(SlotState.Invalid, model4.getSlotAt(5, 5));
  }

  @Test (expected = IllegalArgumentException.class)
  public void getInvalidGetSlot1() {
    // negative position
    model1.getSlotAt(6, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void getInvalidGetSlot2() {
    // out of board range
    model3.getSlotAt(13, 13);
  }

  @Test
  public void testMove() {
    String expected
            = "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O";
    assertEquals(SlotState.Marble, model1.getSlotAt(3, 5));
    assertEquals(SlotState.Marble, model1.getSlotAt(3, 4));
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 3));
    model1.move(3, 5, 3, 3);
    assertEquals(expected, board1.toString());
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 5));
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 4));
    assertEquals(SlotState.Marble, model1.getSlotAt(3, 3));
    model1.move(5, 4, 3, 4);
    String expected1
            = "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O O _ O\n"
            + "O O O O _ O O\n"
            + "    O O _\n"
            + "    O O O";
    assertEquals(expected1, board1.toString());
  }

  @Test
  public void testMove2() {
    String expected
            = "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O _ O O O O O O\n"
            + "O O O O O O _ O O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O";
    assertEquals(SlotState.Marble, model3.getSlotAt(8, 6));
    assertEquals(SlotState.Marble, model3.getSlotAt(7, 6));
    assertEquals(SlotState.Empty, model3.getSlotAt(6, 6));
    model3.move(8, 6, 6, 6);
    assertEquals(expected, board3.toString());
    assertEquals(SlotState.Empty, model3.getSlotAt(8, 6));
    assertEquals(SlotState.Empty, model3.getSlotAt(7, 6));
    assertEquals(SlotState.Marble, model3.getSlotAt(6, 6));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMove1() {
    // from is empty
    model2.move(0, 4, 2, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMove4() {
    // from is invalid
    model2.move(2, 7, 2, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMove2() {
    // to is full
    model2.move(2, 4, 4, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMove3() {
    // to is invalid
    model2.move(3, 1, 5, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMove5() {
    model1.move(3, 5, 3, 3);

    // col move is too far
    model1.move(3, 2, 3, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMove6() {
    model1.move(3, 5, 3, 3);

    // row move is too far
    model1.move(6, 4, 3, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMove7() {
    model1.move(3, 5, 3, 3);

    // no marble between from and to
    model1.move(3, 3, 3, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMove8() {
    model1.move(3, 5, 4, 6);
  }

  @Test
  public void testGameOver() {
    model1.move(3, 5, 3, 3);
    model1.move(1, 4, 3, 4);
    model1.move(4, 4, 2, 4);
    model1.move(1, 2, 1, 4);
    model1.move(1, 4, 3, 4);
    model1.move(3, 2, 1, 2);
    model1.move(0, 2, 2, 2);
    model1.move(2, 2, 2, 4);
    model1.move(2, 0, 2, 2);
    model1.move(3, 0, 3, 2);
    model1.move(3, 2, 1, 2);
    assertFalse(model1.isGameOver());
    model1.move(2, 5, 2, 3);
    model1.move(3, 3, 3, 5);
    model1.move(4, 6, 4, 4);
    model1.move(5, 3, 3, 3);
    model1.move(2, 3, 4, 3);
    model1.move(5, 4, 3, 4);
    model1.move(5, 2, 3, 2);
    model1.move(4, 0, 4, 2);
    model1.move(4, 2, 4, 4);
    model1.move(4, 4, 2, 4);
    model1.move(3, 6, 3, 4);
    model1.move(2, 4, 4, 4);
    model1.move(0, 4, 0, 2);
    model1.move(0, 2, 2, 2);
    assertFalse(model1.isGameOver());
    model1.move(3, 2, 1, 2);
    assertTrue(model1.isGameOver());
    assertEquals(6, model1.getScore());
    String expected
            = "    _ _ _\n"
            + "    O _ _\n"
            + "_ _ _ _ _ _ O\n"
            + "_ _ _ _ _ _ _\n"
            + "_ _ _ _ O _ _\n"
            + "    _ _ _\n"
            + "    O O O";

    assertEquals(expected, board1.toString());
  }
}
