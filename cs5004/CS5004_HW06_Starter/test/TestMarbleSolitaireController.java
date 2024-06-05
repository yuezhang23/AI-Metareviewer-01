import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import cs5004.marblesolitaire.controller.MarbleSolitaireController;
import cs5004.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import cs5004.marblesolitaire.model.hw05.EnglishSolitaireModel;
import cs5004.marblesolitaire.model.hw05.MarbleSolitaireModel;
import cs5004.marblesolitaire.view.MarbleSolitaireTextView;
import cs5004.marblesolitaire.view.MarbleSolitaireView;

import java.io.StringReader;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * This is to test all cases in the playGame method and the constructor
 * in MarbleSolitaireControllerImpl class.
 */
public class TestMarbleSolitaireController {
  private MarbleSolitaireModel model;
  private MarbleSolitaireView view;
  private MarbleSolitaireModel mockModel;
  private MarbleSolitaireView mockView;
  private StringBuilder mockLog;
  private StringBuilder log;

  @Before
  public void setup() {
    mockLog = new StringBuilder();
    mockModel = new SolitaireMockModel(mockLog);
    mockView = new MarbleSolitaireTextView(mockModel, mockLog);

    log = new StringBuilder();
    model = new EnglishSolitaireModel();
    view = new MarbleSolitaireTextView(model, log);
  }

  // only one valid move before quiting is passed to model
  @Test
  public void testConstructWithValidInput() {
    StringReader in = new StringReader("4 6 4 4 q 4 3 4 5");
    new MarbleSolitaireControllerImpl(
            mockModel, mockView, in).playGame();
    String[] lines = mockLog.toString().split("\n");
    String msgC = String.join("\n",
            Arrays.copyOfRange(lines, 3, 5));
    // position inputs passed to model should be subtracted by 1
    assertEquals("passed in:\n"
            + "fromRow: 3, fromCol: 5, toRow: 3, toCol: 3", msgC);
  }

  // invalid input is ignored when passed to model
  @Test
  public void testConstructValidInputWithInvalidChar() {
    StringReader in = new StringReader("4 #$% 6 4 4 q");
    new MarbleSolitaireControllerImpl(
            mockModel, mockView, in).playGame();
    String[] lines = mockLog.toString().split("\n");
    String msgC = String.join("\n",
            Arrays.copyOfRange(lines, 3, 5));
    assertEquals("passed in:\n"
            + "fromRow: 3, fromCol: 5, toRow: 3, toCol: 3", msgC);
  }

  // invalid move is passed to model
  @Test
  public void testConstructValidInputWithInvalidPos() {
    StringReader in = new StringReader("4 6 2 4 q");
    new MarbleSolitaireControllerImpl(
            mockModel, mockView, in).playGame();
    String[] lines = mockLog.toString().split("\n");
    String msgC = String.join("\n",
            Arrays.copyOfRange(lines, 3, 5));
    assertEquals("passed in:\n"
            + "fromRow: 3, fromCol: 5, toRow: 1, toCol: 3", msgC);
  }

  // two valid moves passed to model
  @Test
  public void testConstructTwoValidInputs() {
    StringReader in = new StringReader("4 6 4 4 4 3 4 5 q");
    new MarbleSolitaireControllerImpl(
            mockModel, mockView, in).playGame();
    String[] lines = mockLog.toString().split("\n");
    String msgC = String.join("\n",
            Arrays.copyOfRange(lines, 3, 10));
    assertEquals("passed in:\nfromRow: 3, fromCol: 5, toRow: 3, toCol: 3\n"
            + "O O\n"
            + "O O\n"
            + "Score: 100\n"
            + "passed in:\nfromRow: 3, fromCol: 2, toRow: 3, toCol: 4", msgC);
  }

  // invalid move is not passed to model
  @Test
  public void testConstructInvalidInputWithInvalidPos() {
    StringReader in = new StringReader("4 6 2 &4# q");
    new MarbleSolitaireControllerImpl(
            mockModel, mockView, in).playGame();
    assertEquals("O O\n"
            + "O O\n"
            + "Score: 100\n"
            + "Game quit!\n"
            + "State of game when quit:\n"
            + "O O\n"
            + "O O\n"
            + "Score: 100\n", mockLog.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstructModelNull() {
    new MarbleSolitaireControllerImpl(
            null, view, new StringReader("4 6 4 4 q")).playGame();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstViewNull() {
    new MarbleSolitaireControllerImpl(
            model, null, new StringReader("4 6 4 4 q")).playGame();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidConstInputNull() {
    new MarbleSolitaireControllerImpl(
            model, view, null).playGame();
  }

  // quit after one valid move
  @Test
  public void testQuitAfterOneValidMove() {
    StringReader in1 = new StringReader("4 6 4 4 q");
    new MarbleSolitaireControllerImpl(model, view, in1).playGame();
    assertEquals("    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 32\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 31\n"
            + "Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 31\n", log.toString());

    String[] lines = log.toString().split("\n");
    assertEquals(26, lines.length);
  }

  // quit after one invalid move
  @Test
  public void testQuitAfterOneInvalidMove() {
    StringReader in2 = new StringReader("4 6 2 4 q");
    new MarbleSolitaireControllerImpl(model, view, in2).playGame();
    assertEquals("    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 32\n"
            + "Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 32\n", log.toString());

    String[] lines = log.toString().split("\n");
    assertEquals(18, lines.length);
  }

  // quit after one invalid move
  @Test
  public void testQuitAfterOneInvalidInput() {
    StringReader in2 = new StringReader("4 6 4 $4% q");
    new MarbleSolitaireControllerImpl(model, view, in2).playGame();
    assertEquals("    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 32\n"
            + "Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 32\n", log.toString());
  }

  // quit after two valid moves
  @Test
  public void testQuitAfterTwoValidMoves() {
    StringReader in3 = new StringReader("4 6 4 4 4 3 4 5 Q");

    new MarbleSolitaireControllerImpl(model, view, in3).playGame();
    String[] lines = log.toString().split("\n");
    assertEquals(34, lines.length);

    String midMsg = String.join("\n",
            Arrays.copyOfRange(lines, 8, 16));
    String lastMsg = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 10, lines.length));
    assertEquals(
            "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 31", midMsg);
    assertEquals("Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O _ _ O _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 30", lastMsg);

    // compare to same moves ending by Q instead of q
    StringReader in4 = new StringReader("4 6 4 4 4 3 4 5 q");
    StringBuilder log4 = new StringBuilder();
    MarbleSolitaireModel model4 = new EnglishSolitaireModel();
    new MarbleSolitaireControllerImpl(model4,
            new MarbleSolitaireTextView(model4, log4), in4).playGame();
    assertEquals(log.toString(), log4.toString());
  }

  // quit after valid and invalid moves
  @Test
  public void testQuitAfterValidAndInvalidMoves() {
    StringReader in5 = new StringReader("4 6 4 4 6 4 4 4 4 3 4 5 Q");

    new MarbleSolitaireControllerImpl(model, view, in5).playGame();
    String[] lines = log.toString().split("\n");
    assertEquals(34, lines.length);

    // read message lines that show the board after first valid move
    String midMsg = String.join("\n",
            Arrays.copyOfRange(lines, 8, 16));
    assertEquals("    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 31", midMsg);

    // read message of the quit state
    String lastMsg = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 10, lines.length));
    assertEquals("Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O _ _ O _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 30", lastMsg);

    // compare to same moves in different order before quit
    StringReader in6 = new StringReader("6 4 4 4 4 3 4 5 4 6 4 4 Q");
    StringBuilder log6 = new StringBuilder();
    MarbleSolitaireModel model6 = new EnglishSolitaireModel();
    new MarbleSolitaireControllerImpl(model6,
            new MarbleSolitaireTextView(model6, log6), in6).playGame();

    // same moves with order change produce different outcome
    assertNotEquals(log6.toString(), log.toString());
  }

  // valid move with invalid input at different positions
  @Test
  public void testQuitAfterValidMoveWithInvalidChars() {
    //  invalid char input at front
    StringReader in7 = new StringReader("%#s 4 6 4 4 Q");
    new MarbleSolitaireControllerImpl(model, view, in7).playGame();
    String[] lines = log.toString().split("\n");
    String lastMsg7 = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 10, lines.length));
    assertEquals("Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 31", lastMsg7);

    //  compare to invalid char in the middle
    StringReader in8 = new StringReader("4 6 %#s 4 4 q");
    StringBuilder log8 = new StringBuilder();
    MarbleSolitaireModel model8 = new EnglishSolitaireModel();
    new MarbleSolitaireControllerImpl(model8,
            new MarbleSolitaireTextView(model8, log8), in8).playGame();
    assertEquals(log8.toString(), log.toString());

    //  compare to invalid char in the last
    StringReader in9 = new StringReader("4 6 4 4 %#s q");
    StringBuilder log9 = new StringBuilder();
    MarbleSolitaireModel model9 = new EnglishSolitaireModel();
    new MarbleSolitaireControllerImpl(model9,
            new MarbleSolitaireTextView(model9, log9), in9).playGame();
    assertEquals(log9.toString(), log.toString());

    //  compare to invalid char after quit
    StringReader in90 = new StringReader("4 6 4 4 q %#s");
    StringBuilder log90 = new StringBuilder();
    MarbleSolitaireModel model90 = new EnglishSolitaireModel();
    new MarbleSolitaireControllerImpl(model90,
            new MarbleSolitaireTextView(model90, log90), in90).playGame();
    assertEquals(log90.toString(), log.toString());
  }

  // put quit at front
  @Test
  public void testQuitBeforeValidMove() {
    // where q is at the front of valid moves
    StringReader in10 = new StringReader("q 4 6 4 4 4 3 4 5");
    new MarbleSolitaireControllerImpl(model, view, in10).playGame();

    String[] lines = log.toString().split("\n");
    String lastMsg = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 10, lines.length));
    assertEquals("Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 32", lastMsg);

    // compare to where q is between two valid move
    StringReader in11 = new StringReader("4 6 4 4 q 4 3 4 5");
    StringBuilder log11 = new StringBuilder();
    MarbleSolitaireModel model11 = new EnglishSolitaireModel();

    new MarbleSolitaireControllerImpl(model11,
            new MarbleSolitaireTextView(model11, log11), in11).playGame();

    String[] lines1 = log11.toString().split("\n");
    String lastMsg1 = String.join("\n",
            Arrays.copyOfRange(lines1, lines1.length - 10, lines1.length));
    assertEquals("Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 31", lastMsg1);

    // compare to where q is in the middle of one valid move
    StringReader in12 = new StringReader("4 6 4 4 4 3 q 4 5");
    StringBuilder log12 = new StringBuilder();
    MarbleSolitaireModel model12 = new EnglishSolitaireModel();

    new MarbleSolitaireControllerImpl(model12,
            new MarbleSolitaireTextView(model12, log12), in12).playGame();

    String[] lines2 = log12.toString().split("\n");
    String lastMsg2 = String.join("\n",
            Arrays.copyOfRange(lines2, lines2.length - 10, lines2.length));
    assertEquals(lastMsg2, lastMsg1);
  }

  @Test
  public void testGameOver() {
    String over = "4 6 4 4 2 5 4 5 5 5 3 5 2 3 2 5 2 5 4 5 4 3 2 3 1 3 3 3 "
            + "3 3 3 5 3 1 3 3 4 1 4 3 4 3 2 3 4 4 4 6 5 7 5 5 6 4 4 4 "
            + "3 6 3 4 6 5 4 5 6 3 4 3 5 1 5 3 5 3 3 3 3 3 3 5 4 5 4 3 "
            + "1 5 1 3 1 3 3 3 3 3 5 3 4 7 4 5 4 5 2 5";
    StringReader in13 = new StringReader(over);
    new MarbleSolitaireControllerImpl(model, view, in13).playGame();
    String[] lines = log.toString().split("\n");
    String lastMsg = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 9, lines.length));
    assertEquals("Game over!\n"
            + "    _ _ _\n"
            + "    _ _ O\n"
            + "_ _ _ _ _ _ O\n"
            + "_ _ _ _ _ _ _\n"
            + "_ _ O _ _ _ _\n"
            + "    _ _ _\n"
            + "    O O O\n"
            + "Score: 6", lastMsg);

    //quit after game over
    StringReader in14 = new StringReader("2 4 2 2");
    new MarbleSolitaireControllerImpl(model, view, in14).playGame();
    String[] lines1 = log.toString().split("\n");
    String lastMsg1 = String.join("\n",
            Arrays.copyOfRange(lines1, lines1.length - 9, lines1.length));
    assertEquals(lastMsg1, lastMsg);

    // invalid input after game over
    StringReader in15 = new StringReader("4 6 4 4");
    new MarbleSolitaireControllerImpl(model, view, in15).playGame();
    String[] lines2 = log.toString().split("\n");
    String lastMsg2 = String.join("\n",
            Arrays.copyOfRange(lines2, lines2.length - 9, lines2.length));
    assertEquals(lastMsg2, lastMsg);
  }

  // valid moves without q
  @Test (expected = IllegalStateException.class)
  public void testValidMovesAndNoQuit() {
    StringReader in14 = new StringReader("4 6 4 4 4 3 4 5");
    new MarbleSolitaireControllerImpl(model, view, in14).playGame();
  }

  // invalid move without q
  @Test (expected = IllegalStateException.class)
  public void testInvalidMoveAndNoQuit() {
    StringReader in15 = new StringReader("4 2 3 4");
    new MarbleSolitaireControllerImpl(model, view, in15).playGame();
  }

  // valid and invalid moves without q
  @Test (expected = IllegalStateException.class)
  public void testValidAndInvalidMovesAndNoQuit() {
    StringReader in16 = new StringReader("4 6 4 4 4 2");
    new MarbleSolitaireControllerImpl(model, view, in16).playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testValidAndInvalidInputsAndNoQuit() {
    StringReader in17 = new StringReader("4 6 4 4 r$%");
    new MarbleSolitaireControllerImpl(model, view, in17).playGame();
  }

  // input format error where q is not recognized
  @Test (expected = IllegalStateException.class)
  public void testQuitNotRecognized() {
    StringReader in18 = new StringReader("4 6 4 4 rq");
    new MarbleSolitaireControllerImpl(model, view, in18).playGame();
  }

  // test IOE exception
  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    Appendable gameLog = new FailingAppendable();
    MarbleSolitaireController case1 = new MarbleSolitaireControllerImpl(model,
            new MarbleSolitaireTextView(model, gameLog), new StringReader("4 6 4 4 q"));
    case1.playGame();
  }
}
