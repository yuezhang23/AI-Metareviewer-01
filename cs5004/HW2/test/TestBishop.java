import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains all the unit tests for bishop class, type of ChessPiece.
 */
public class TestBishop {
  ChessPiece bishop1B;
  ChessPiece bishop1W;
  ChessPiece bishop2B;
  ChessPiece bishop2W;

  @Before
  public void setUp() {
    // in the middle
    bishop1B = new Bishop(5, 3, Color.BLACK);
    bishop1W = new Bishop(3, 2, Color.WHITE);

    // along edge
    bishop2B = new Bishop(3, 0, Color.BLACK);

    // in the corner
    bishop2W = new Bishop(0, 7, Color.WHITE);
  }

  @Test
  public void testGetCol() {
    assertEquals(3, bishop1B.getColumn());
    assertEquals(0, bishop2B.getColumn());
    assertEquals(2, bishop1W.getColumn());
    assertEquals(7, bishop2W.getColumn());
  }

  @Test
  public void testGetRow() {
    assertEquals(5, bishop1B.getRow());
    assertEquals(3, bishop2B.getRow());
    assertEquals(3, bishop1W.getRow());
    assertEquals(0, bishop2W.getRow());
  }

  @Test
  public void testGetColor() {
    assertEquals(Color.BLACK, bishop1B.getColor());
    assertEquals(Color.WHITE, bishop1W.getColor());
    assertEquals(Color.BLACK, bishop2B.getColor());
    assertEquals(Color.WHITE, bishop2W.getColor());
  }

  @Test
  public void testCanMove() {
    // move -2, -2
    assertTrue(bishop1B.canMove(3, 1));

    // move +2, +2
    assertTrue(bishop1W.canMove(5, 4));

    // move -2, +2
    assertTrue(bishop2B.canMove(1, 2));

    // move +3, -3
    assertTrue(bishop2W.canMove(3, 4));

    assertFalse(bishop2W.canMove(2, 4));
    assertFalse(bishop2B.canMove(1, 3));

  }

  @Test
  public void testCanKill() {
    // move -2, -2
    assertTrue(bishop1B.canKill(new Bishop(3, 1, Color.WHITE)));

    // move +2, +2
    assertTrue(bishop1W.canKill(new Queen(5, 4, Color.BLACK)));

    // move -2, +2
    assertTrue(bishop2B.canKill(new Knight(1, 2, Color.WHITE)));

    // move +3, -3
    assertTrue(bishop2W.canKill(new Rook(3, 4, Color.BLACK)));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosRow1() {
    // row < 0
    new Bishop(-2, 3, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosRow2() {
    // row > 7
    new Bishop(8, 1, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosCol1() {
    // column > 7
    new Bishop(0, 8, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosCol2() {
    // column < 0
    new Bishop(4, -1, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillBlackFriend() {
    bishop1B.canKill(new Knight(1, 7, Color.BLACK));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillWhiteFriend() {
    bishop1W.canKill(new Queen(2, 2, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillSamePos() {
    bishop2B.canKill(new Bishop(3, 0, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow1() {
    // row > 7
    bishop1B.canMove(8, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow2() {
    // row < 0
    bishop2B.canMove(-1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol1() {
    // column < 0
    bishop1W.canMove(3, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol2() {
    // column > 7
    bishop2W.canMove(0, 8);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveSamePos() {
    bishop2W.canMove(0, 7);
  }
}
