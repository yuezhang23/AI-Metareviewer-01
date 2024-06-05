import org.junit.Before;
import org.junit.Test;

import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModelState.SlotState;
import cs5004.marblesolitaire.model.hw07.TriangleSolitaireModel;
import cs5004.marblesolitaire.view.MarbleSolitaireView;
import cs5004.marblesolitaire.view.TriangleSolitaireTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This is the test for all methods that implement the TriangleSolitaireModel class.
 */
public class TestTriangleSolitaireModel {
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
    model1 = new TriangleSolitaireModel();
    board1 = new TriangleSolitaireTextView(model1);
    model2 = new TriangleSolitaireModel(3, 1);
    board2 = new TriangleSolitaireTextView(model2);
    model3 = new TriangleSolitaireModel(10);
    board3 = new TriangleSolitaireTextView(model3);
    model4 = new TriangleSolitaireModel(6, 3, 1);
    board4 = new TriangleSolitaireTextView(model4);
  }

  @Test
  public void testConstrNoPara() {
    String expected =
              "    _\n"
            + "   O O\n"
            + "  O O O\n"
            + " O O O O\n"
            + "O O O O O";
    assertEquals(expected, board1.toString());
    assertEquals(5, model1.getBoardSize());
    assertEquals(SlotState.Empty, model1.getSlotAt(0, 0));
  }

  @Test
  public void testConstrTwoPara() {
    String expected =
             "    O\n"
           + "   O O\n"
           + "  O O O\n"
           + " O _ O O\n"
           + "O O O O O";
    assertEquals(expected, board2.toString());
    assertEquals(5, model2.getBoardSize());
    assertEquals(SlotState.Empty, model2.getSlotAt(3, 1));
  }

  @Test
  public void testConstrOnePara() {
    String expected =
            "         _\n"
          + "        O O\n"
          + "       O O O\n"
          + "      O O O O\n"
          + "     O O O O O\n"
          + "    O O O O O O\n"
          + "   O O O O O O O\n"
          + "  O O O O O O O O\n"
          + " O O O O O O O O O\n"
          + "O O O O O O O O O O";
    assertEquals(expected, board3.toString());
    assertEquals(10, model3.getBoardSize());
    assertEquals(SlotState.Empty, model3.getSlotAt(0, 0));
  }

  @Test
  public void testConstrThreePara() {
    String expected =
              "     O\n"
            + "    O O\n"
            + "   O O O\n"
            + "  O _ O O\n"
            + " O O O O O\n"
            + "O O O O O O";
    assertEquals(expected, board4.toString());
    assertEquals(6, model4.getBoardSize());
    assertEquals(SlotState.Empty, model4.getSlotAt(3, 1));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstWithTooSmallSize() {
    new TriangleSolitaireModel(0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstPosOutOfBound() {
    new TriangleSolitaireModel(5, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstPosOutOfBoundRow() {
    new TriangleSolitaireModel(5, 6, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstPosOutOfBoundCol() {
    new TriangleSolitaireModel(5, 3, 6);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstPosInvalid() {
    new TriangleSolitaireModel(5, 0, 1);
  }

  @Test
  public void testGetBoardSize() {
    assertEquals(5, model1.getBoardSize());
    assertEquals(5, model2.getBoardSize());
    assertEquals(10, model3.getBoardSize());
    assertEquals(6, model4.getBoardSize());
  }

  @Test
  public void testScore() {
    assertEquals(14, model1.getScore());
    assertEquals(14, model2.getScore());
    assertEquals(54, model3.getScore());
    assertEquals(20, model4.getScore());

    // 1 move decreases score by 1
    model2.move(3, 3, 3, 1);
    assertEquals(13, model2.getScore());
  }

  @Test
  public void testGetSlotValid() {
    assertEquals(SlotState.Empty, model1.getSlotAt(0, 0));
    assertEquals(SlotState.Empty, model2.getSlotAt(3, 1));
    assertEquals(SlotState.Marble, model2.getSlotAt(0, 0));
    assertEquals(SlotState.Empty, model3.getSlotAt(0, 0));
    assertEquals(SlotState.Marble, model4.getSlotAt(0, 0));
    assertEquals(SlotState.Empty, model4.getSlotAt(3, 1));
  }

  @Test
  public void testGetSlotInvalidWithinBound() {
    assertEquals(SlotState.Invalid, model1.getSlotAt(0, 1));
    assertEquals(SlotState.Invalid, model2.getSlotAt(3, 4));
    assertEquals(SlotState.Invalid, model3.getSlotAt(8, 9));
    assertEquals(SlotState.Invalid, model4.getSlotAt(4, 5));
  }

  @Test (expected = IllegalArgumentException.class)
  public void getInvalidGetSlotInvalidPos() {
    // invalid position
    model1.getSlotAt(0, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void getInvalidGetSlotOutOfColBound() {
    // out of board range
    model3.getSlotAt(9, 10);
  }

  @Test (expected = IllegalArgumentException.class)
  public void getInvalidGetSlotOutOfRowBound() {
    // out of board range
    model4.getSlotAt(6, 0);
  }

  @Test
  public void testValidMove() {
    // horizontal move left
    model2.move(3, 3, 3, 1);
    assertEquals(SlotState.Marble, model2.getSlotAt(3, 1));
    assertEquals(SlotState.Empty, model2.getSlotAt(3, 3));
    assertEquals(SlotState.Empty, model2.getSlotAt(3, 2));
    String expected =
            "    O\n"
          + "   O O\n"
          + "  O O O\n"
          + " O O _ _\n"
          + "O O O O O";
    assertEquals(expected, board2.toString());

    // horizontal move diagonal right-down
    model2.move(1, 0, 3, 2);
    assertEquals(SlotState.Marble, model2.getSlotAt(3, 2));
    assertEquals(SlotState.Empty, model2.getSlotAt(1, 0));
    assertEquals(SlotState.Empty, model2.getSlotAt(2, 1));
    String expected1 =
                      "    O\n"
                    + "   _ O\n"
                    + "  O _ O\n"
                    + " O O O _\n"
                    + "O O O O O";
    assertEquals(expected1, board2.toString());

    // horizontal move diagonal right-up
    model2.move(4, 1, 2, 1);
    assertEquals(SlotState.Marble, model2.getSlotAt(2, 1));
    assertEquals(SlotState.Empty, model2.getSlotAt(3, 1));
    assertEquals(SlotState.Empty, model2.getSlotAt(4, 1));
    String expected2 =
                      "    O\n"
                    + "   _ O\n"
                    + "  O O O\n"
                    + " O _ O _\n"
                    + "O _ O O O";
    assertEquals(expected2, board2.toString());


    // horizontal move diagonal left-down
    model2.move(1, 1, 3, 1);
    assertEquals(SlotState.Marble, model2.getSlotAt(3, 1));
    assertEquals(SlotState.Empty, model2.getSlotAt(2, 1));
    assertEquals(SlotState.Empty, model2.getSlotAt(1, 1));
    String expected4 =
                      "    O\n"
                    + "   _ _\n"
                    + "  O _ O\n"
                    + " O O O _\n"
                    + "O _ O O O";
    assertEquals(expected4, board2.toString());

    // horizontal move diagonal left-up
    model2.move(4, 3, 2, 1);
    assertEquals(SlotState.Marble, model2.getSlotAt(2, 1));
    assertEquals(SlotState.Empty, model2.getSlotAt(3, 2));
    assertEquals(SlotState.Empty, model2.getSlotAt(4, 3));
    String expected3 =
                      "    O\n"
                    + "   _ _\n"
                    + "  O O O\n"
                    + " O O _ _\n"
                    + "O _ O _ O";
    assertEquals(expected3, board2.toString());

    // horizontal move right
    model2.move(3, 0, 3, 2);
    assertEquals(SlotState.Marble, model2.getSlotAt(3, 2));
    assertEquals(SlotState.Empty, model2.getSlotAt(3, 1));
    assertEquals(SlotState.Empty, model2.getSlotAt(3, 0));
    String expected5 =
                      "    O\n"
                    + "   _ _\n"
                    + "  O O O\n"
                    + " _ _ O _\n"
                    + "O _ O _ O";
    assertEquals(expected5, board2.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  // from is empty
  public void testInvalidMoveFromEmpty() {
    model4.move(3, 1, 3, 3);
  }

  // from is invalid
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveFromInvalid() {
    model2.move(5, 1, 3, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveToFull() {
    // to is full
    model4.move(2, 2, 0, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveToInvalid() {
    // to is invalid
    model4.move(1, 0, 1, 2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveMidEmpty() {
    model4.move(1, 1, 3, 1);
    String expected =
                      "     O\n"
                    + "    O _\n"
                    + "   O _ O\n"
                    + "  O O O O\n"
                    + " O O O O O\n"
                    + "O O O O O O";
    assertEquals(expected, board4.toString());
    model4.move(3, 1, 1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveInvalidDir() {
    model4.move(1, 1, 3, 1);
    String expected =
                      "     O\n"
                    + "    O _\n"
                    + "   O _ O\n"
                    + "  O O O O\n"
                    + " O O O O O\n"
                    + "O O O O O O";
    assertEquals(expected, board4.toString());
    model4.move(3, 2, 2, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveTooFar() {
    model4.move(1, 1, 3, 1);
    String expected =
                      "     O\n"
                    + "    O _\n"
                    + "   O _ O\n"
                    + "  O O O O\n"
                    + " O O O O O\n"
                    + "O O O O O O";
    assertEquals(expected, board4.toString());
    model4.move(4, 1, 1, 1); // 4 1 2 1
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveDir1() {
    model4.move(4, 1, 2, 3);  // row-2, col+2

  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveDir2() {
    model4.move(1, 3, 3, 1); // row+2, col-2
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveMinusNumber() {
    model4.move(1, 1, 3, 1);
    String expected =
                      "     O\n"
                    + "    O _\n"
                    + "   O _ O\n"
                    + "  O O O O\n"
                    + " O O O O O\n"
                    + "O O O O O O";
    assertEquals(expected, board4.toString());
    model4.move(3, 3, 0, 2); // 3 3 1 1
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidMoveAddNumber() {
    model4.move(1, 1, 3, 1);
    model4.move(4, 3, 2, 1);
    model4.move(4, 1, 4, 3);
    String expected =
                      "     O\n"
                    + "    O _\n"
                    + "   O O O\n"
                    + "  O O _ O\n"
                    + " O _ _ O O\n"
                    + "O O O O O O";
    assertEquals(expected, board4.toString());
    model4.move(1, 0, 4, 1); // 1, 0, 3, 2
  }

  @Test
  public void testGameOver1() {
    model4.move(1, 1, 3, 1);
    model4.move(4, 1, 2, 1);
    model4.move(3, 3, 3, 1);
    model4.move(3, 0, 3, 2);
    model4.move(4, 3, 4, 1);
    model4.move(5, 0, 3, 0);
    model4.move(5, 1, 3, 1);
    model4.move(2, 0, 4, 2);
    model4.move(2, 1, 4, 3);
    model4.move(0, 0, 2, 0);
    model4.move(5, 3, 3, 1);
    model4.move(5, 5, 5, 3);
    model4.move(5, 3, 3, 3);
    model4.move(2, 0, 4, 0);
    assertFalse(model4.isGameOver());
    assertEquals(6, model4.getScore());
    String expected =
                     "     _\n"
                   + "    _ _\n"
                   + "   _ _ O\n"
                   + "  _ O _ O\n"
                   + " O _ _ _ O\n"
                   + "_ _ O _ _ _";
    assertEquals(expected, board4.toString());

    // diagonal move to game over
    model4.move(3, 3, 5, 5);
    assertTrue(model4.isGameOver());
    assertEquals(5, model4.getScore());
    String expected1 =
                      "     _\n"
                    + "    _ _\n"
                    + "   _ _ O\n"
                    + "  _ O _ _\n"
                    + " O _ _ _ _\n"
                    + "_ _ O _ _ O";
    assertEquals(expected1, board4.toString());
  }

  @Test
  public void testGameOver2() {
    model4.move(1, 1, 3, 1);
    model4.move(4, 1, 2, 1);
    model4.move(3, 3, 3, 1);
    model4.move(3, 0, 3, 2);
    model4.move(4, 3, 4, 1);
    model4.move(5, 0, 3, 0);
    model4.move(5, 1, 3, 1);
    model4.move(2, 0, 4, 2);
    model4.move(2, 1, 4, 3);
    model4.move(5, 3, 5, 1);
    model4.move(4, 3, 4, 1);
    model4.move(3, 0, 5, 2);
    model4.move(5, 5, 3, 3);
    model4.move(3, 3, 1, 1);
    model4.move(5, 2, 5, 0);
    assertFalse(model4.isGameOver());
    assertEquals(5, model4.getScore());
    String expected =
                      "     O\n"
                    + "    O O\n"
                    + "   _ _ _\n"
                    + "  _ _ _ _\n"
                    + " _ _ _ _ _\n"
                    + "O _ _ _ O _";
    assertEquals(expected, board4.toString());

    // horizontal move to game over
    model4.move(0, 0, 2, 0);
    assertTrue(model4.isGameOver());
    assertEquals(4, model4.getScore());
    String expected1 =
                      "     _\n"
                    + "    _ O\n"
                    + "   O _ _\n"
                    + "  _ _ _ _\n"
                    + " _ _ _ _ _\n"
                    + "O _ _ _ O _";
    assertEquals(expected1, board4.toString());
  }
}
