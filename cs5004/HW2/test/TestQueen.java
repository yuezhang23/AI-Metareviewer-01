import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains all the unit tests for Queen class, type of ChessPiece.
 */
public class TestQueen {
  ChessPiece queen1B;
  ChessPiece queen1W;
  ChessPiece queen2B;
  ChessPiece queen2W;

  @Before
  public void setUp() {
    queen1B = new Queen(4, 3, Color.BLACK);
    queen2B = new Queen(3, 0, Color.BLACK);
    queen1W = new Queen(5, 5, Color.WHITE);
    queen2W = new Queen(0, 7, Color.WHITE);
  }

  @Test
  public void testGetCol() {
    assertEquals(3, queen1B.getColumn());
    assertEquals(0, queen2B.getColumn());
    assertEquals(5, queen1W.getColumn());
    assertEquals(7, queen2W.getColumn());
  }

  @Test
  public void testGetRow() {
    assertEquals(4, queen1B.getRow());
    assertEquals(3, queen2B.getRow());
    assertEquals(5, queen1W.getRow());
    assertEquals(0, queen2W.getRow());
  }

  @Test
  public void testGetColor() {
    assertEquals(Color.BLACK, queen1B.getColor());
    assertEquals(Color.WHITE, queen1W.getColor());
    assertEquals(Color.BLACK, queen2B.getColor());
    assertEquals(Color.WHITE, queen2W.getColor());
  }

  @Test
  public void testCanMove() {
    // move diagonal -2, -2
    assertTrue(queen1B.canMove(2, 1));

    // move diagonal +2  +2
    assertTrue(queen2B.canMove(5, 2));

    // move diagonal -1, +1
    assertTrue(queen1W.canMove(4, 6));

    // move diagonal +1, -1
    assertTrue(queen2W.canMove(1, 6));

    // move vertical 2, 0
    assertTrue(queen1B.canMove(6, 3));

    // move vertical -1, 0
    assertTrue(queen2B.canMove(3, 3));

    // move horizontal 0, +1
    assertTrue(queen1W.canMove(5, 6));

    // move horizontal 0, -1
    assertTrue(queen2W.canMove(0, 6));

    assertFalse(queen1W.canMove(6, 7));
    assertFalse(queen2W.canMove(1, 5));
  }

  @Test
  public void testCanKill() {
    // move diagonal -2, -2
    assertTrue(queen1B.canKill(new Queen(2, 1, Color.WHITE)));

    // move diagonal +2  +2
    assertTrue(queen2B.canKill(new Knight(5, 2, Color.WHITE)));

    // move diagonal -1, +1
    assertTrue(queen1W.canKill(new Queen(4, 6, Color.BLACK)));

    // move diagonal +1, -1
    assertTrue(queen2W.canKill(new Rook(1, 6, Color.BLACK)));

    // move vertical 2, 0
    assertTrue(queen1B.canKill(new Queen(6, 3, Color.WHITE)));

    // move vertical -1, 0
    assertTrue(queen2B.canKill(new Knight(3, 3, Color.WHITE)));

    // move horizontal 0, +1
    assertTrue(queen1W.canKill(new Queen(5, 6, Color.BLACK)));

    // move horizontal 0, -1
    assertTrue(queen2W.canKill(new Rook(0, 6, Color.BLACK)));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosRow1() {
    // row < 0
    new Queen(-2, 3, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosRow2() {
    // row > 7
    new Queen(8, 1, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosCol1() {
    // column > 7
    new Queen(0, 8, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosCol2() {
    // column < 0
    new Queen(4, -1, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillBlackFriend() {
    queen1B.canKill(new Knight(1, 7, Color.BLACK));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillWhiteFriend() {
    queen1W.canKill(new Queen(2, 2, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillSamePos() {
    queen1W.canKill(new Rook(5, 5, Color.BLACK));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow1() {
    // row > 7
    queen1B.canMove(8, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow2() {
    // row < 0
    queen2B.canMove(-1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol1() {
    // column < 0
    queen1W.canMove(3, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol2() {
    // column > 7
    queen2W.canMove(0, 8);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveSamePos() {
    queen2W.canMove(0, 7);
  }
}
