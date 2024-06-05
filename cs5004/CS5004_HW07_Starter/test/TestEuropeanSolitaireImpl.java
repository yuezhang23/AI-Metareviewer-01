import org.junit.Before;
import org.junit.Test;

import cs5004.marblesolitaire.model.hw07.EuropeanSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState.SlotState;
import cs5004.marblesolitaire.view.MarbleSolitaireTextView;
import cs5004.marblesolitaire.view.MarbleSolitaireView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This is the test for all methods that implement the EuropeanSolitaireModel class.
 */
public class TestEuropeanSolitaireImpl {
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
    model1 = new EuropeanSolitaireModel();
    board1 = new MarbleSolitaireTextView(model1);
    model2 = new EuropeanSolitaireModel(0, 4);
    board2 = new MarbleSolitaireTextView(model2);
    model3 = new EuropeanSolitaireModel(7);
    board3 = new MarbleSolitaireTextView(model3);
    model4 = new EuropeanSolitaireModel(5, 6, 8);
    board4 = new MarbleSolitaireTextView(model4);
  }

  @Test
  public void testConstrNoParameter() {
    String expected
            = "    O O O\n"
            + "  O O O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(expected, board1.toString());
    assertEquals(7, model1.getBoardSize());
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 3));
  }

  @Test
  public void testConstrTwoParameter() {
    String expected
            = "    O O _\n"
            + "  O O O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(expected, board2.toString());
    assertEquals(7, model2.getBoardSize());
    assertEquals(SlotState.Empty, model2.getSlotAt(0, 4));
  }

  @Test
  public void testConstrOneParameter() {
    String expected
            = "            O O O O O O O\n"
            + "          O O O O O O O O O\n"
            + "        O O O O O O O O O O O\n"
            + "      O O O O O O O O O O O O O\n"
            + "    O O O O O O O O O O O O O O O\n"
            + "  O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O _ O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O O O O O O O\n"
            + "  O O O O O O O O O O O O O O O O O\n"
            + "    O O O O O O O O O O O O O O O\n"
            + "      O O O O O O O O O O O O O\n"
            + "        O O O O O O O O O O O\n"
            + "          O O O O O O O O O\n"
            + "            O O O O O O O";
    assertEquals(expected, board3.toString());
    assertEquals(19, model3.getBoardSize());
    assertEquals(SlotState.Empty, model3.getSlotAt(9, 9));
  }

  @Test
  public void testConstrThreeParameter() {
    String expected =
                      "        O O O O O\n"
                    + "      O O O O O O O\n"
                    + "    O O O O O O O O O\n"
                    + "  O O O O O O O O O O O\n"
                    + "O O O O O O O O O O O O O\n"
                    + "O O O O O O O O O O O O O\n"
                    + "O O O O O O O O _ O O O O\n"
                    + "O O O O O O O O O O O O O\n"
                    + "O O O O O O O O O O O O O\n"
                    + "  O O O O O O O O O O O\n"
                    + "    O O O O O O O O O\n"
                    + "      O O O O O O O\n"
                    + "        O O O O O";
    assertEquals(expected, board4.toString());
    assertEquals(13, model4.getBoardSize());
    assertEquals(SlotState.Empty, model4.getSlotAt(6, 8));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstInvalidSide() {
    // side length too small to start a game
    new EuropeanSolitaireModel(1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstEvenSide() {
    // side length being even
    new EuropeanSolitaireModel(6);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstInvalidRowPos() {
    // invalid row position
    new EuropeanSolitaireModel(-1, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstInvalidColPos() {
    // invalid col position
    new EuropeanSolitaireModel(0, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstPosOutOfBound() {
    new EuropeanSolitaireModel(5, 7);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstEmptySlotInvalid() {
    new EuropeanSolitaireModel(3, 0, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstEmptySlotOutOfBound() {
    new EuropeanSolitaireModel(3, 7, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  // side length being an even number
  public void testInvalidConstSideEven() {
    new EuropeanSolitaireModel(4, 3, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  // invalid side length
  public void testInvalidConstSideTooSmall() {
    new EuropeanSolitaireModel(1, 3, 3);
  }

  @Test
  public void testGetBoardSize() {
    assertEquals(7, model1.getBoardSize());
    assertEquals(7, model2.getBoardSize());
    assertEquals(19, model3.getBoardSize());
    assertEquals(13, model4.getBoardSize());
  }

  @Test
  public void testScore() {
    assertEquals(36, model1.getScore());
    assertEquals(36, model2.getScore());
    assertEquals(276, model3.getScore());
    assertEquals(128, model4.getScore());

    model4.move(8, 8, 6, 8);
    assertEquals(127, model4.getScore());
  }

  @Test
  public void testGetSlot() {
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 3));
    assertEquals(SlotState.Empty, model2.getSlotAt(0, 4));
    assertEquals(SlotState.Empty, model3.getSlotAt(9, 9));
    assertEquals(SlotState.Empty, model4.getSlotAt(6, 8));


    assertEquals(SlotState.Marble, model1.getSlotAt(1, 1));
    assertEquals(SlotState.Invalid, model2.getSlotAt(0, 1));
    assertEquals(SlotState.Invalid, model3.getSlotAt(2, 2));
    assertEquals(SlotState.Marble, model4.getSlotAt(2, 2));
  }

  @Test (expected = IllegalArgumentException.class)
  public void getInvalidGetSlot1() {
    // negative position
    model1.getSlotAt(6, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void getInvalidGetSlot2() {
    // out of board range
    model3.getSlotAt(18, 19);
  }

  @Test
  public void testMove() {
    // move right
    model1.move(3, 5, 3, 3);
    String expected
            = "    O O O\n"
            + "  O O O O O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 5));
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 4));
    assertEquals(SlotState.Marble, model1.getSlotAt(3, 3));
    assertEquals(35, model1.getScore());
    assertEquals(expected, board1.toString());

    // move down
    model1.move(1, 4, 3, 4);
    String expected1
            = "    O O O\n"
            + "  O O O _ O\n"
            + "O O O O _ O O\n"
            + "O O O O O _ O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(SlotState.Empty, model1.getSlotAt(1, 4));
    assertEquals(SlotState.Empty, model1.getSlotAt(2, 4));
    assertEquals(SlotState.Marble, model1.getSlotAt(3, 4));
    assertEquals(34, model1.getScore());
    assertEquals(expected1, board1.toString());

    model1.move(2, 2, 2, 4);
    // move left
    model1.move(2, 5, 2, 3);
    String expected2
            = "    O O O\n"
            + "  O O O _ O\n"
            + "O O _ O _ _ O\n"
            + "O O O O O _ O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(32, model1.getScore());
    assertEquals(expected2, board1.toString());

    // move up
    model1.move(5, 5, 3, 5);
    String expected3
            = "    O O O\n"
            + "  O O O _ O\n"
            + "O O _ O _ _ O\n"
            + "O O O O O O O\n"
            + "O O O O O _ O\n"
            + "  O O O O _\n"
            + "    O O O";
    assertEquals(31, model1.getScore());
    assertEquals(expected3, board1.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveFromEmpty() {
    model2.move(2, 4, 0, 4); //valid
    model2.move(2, 2, 2, 4); //valid
    model2.move(2, 5, 2, 3); //valid
    String expected
            = "    O O O\n"
            + "  O O O _ O\n"
            + "O O _ O _ _ O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(expected, board2.toString());
    // from is empty
    model2.move(2, 2, 2, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveFromInvalid() {
    model2.move(2, 4, 0, 4); //valid
    model2.move(2, 2, 2, 4); //valid
    model2.move(2, 5, 2, 3); //valid
    // from is invalid
    model2.move(1, 6, 1, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveToFull() {
    // to is full
    model2.move(2, 4, 4, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMovToInvalid() {
    // to is invalid
    model2.move(1, 4, 1, 6);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveMidEmpty() {
    model2.move(2, 4, 0, 4); //valid
    model2.move(2, 2, 2, 4); //valid
    model2.move(2, 5, 2, 3); //valid
    String expected
            = "    O O O\n"
            + "  O O O _ O\n"
            + "O O _ O _ _ O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(expected, board2.toString());
    // mid is empty
    model2.move(0, 4, 2, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveTooFar() {
    model2.move(2, 4, 0, 4); //valid
    // col move is too far
    model2.move(2, 1, 2, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveTooFar1() {
    // row move is too far
    model2.move(3, 4, 0, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveDiagonal() {
    // row-2, col-2
    model2.move(2, 6, 0, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveDiagonal1() {
    // row-2, col+2
    model2.move(2, 2, 0, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveDiagonal2() {
    model2.move(2, 4, 0, 4); //valid
    model2.move(2, 2, 2, 4); //valid
    model2.move(2, 5, 2, 3); //valid
    String expected
            = "    O O O\n"
            + "  O O O _ O\n"
            + "O O _ O _ _ O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(expected, board2.toString());
    // row+2, col+2
    model2.move(0, 2, 2, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveDiagonal3() {
    model2.move(2, 4, 0, 4); //valid
    model2.move(2, 2, 2, 4); //valid
    model2.move(2, 5, 2, 3); //valid
    String expected
            = "    O O O\n"
            + "  O O O _ O\n"
            + "O O _ O _ _ O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "  O O O O O\n"
            + "    O O O";
    assertEquals(expected, board2.toString());
    // row+2, col-2
    model2.move(0, 4, 2, 2);
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
    model1.move(2, 5, 2, 3);
    model1.move(3, 3, 3, 5);
    model1.move(4, 6, 4, 4);
    assertFalse(model1.isGameOver());
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
    model1.move(3, 2, 1, 2);
    assertFalse(model1.isGameOver());
    model1.move(1, 1, 1, 3);

    assertTrue(model1.isGameOver());
    assertEquals(9, model1.getScore());
    String expected
            = "    _ _ _\n"
            + "  _ _ O _ O\n"
            + "_ _ _ _ _ _ O\n"
            + "_ _ _ _ _ _ _\n"
            + "_ _ _ _ O _ _\n"
            + "  O _ _ _ O\n"
            + "    O O O";

    assertEquals(expected, board1.toString());
  }
}
