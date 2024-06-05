import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains all the unit tests for Rook class, type of ChessPiece.
 */
public class TestRook {
  ChessPiece rook1B;
  ChessPiece rook1W;
  ChessPiece rook2B;
  ChessPiece rook2W;

  @Before
  public void setUp() {
    // in the middle
    rook1B = new Rook(4, 3, Color.BLACK);
    rook1W = new Rook(5, 5, Color.WHITE);

    // along the edge
    rook2B = new Rook(3, 0, Color.BLACK);
    
    // in the corner
    rook2W = new Rook(0, 7, Color.WHITE);
  }

  @Test
  public void testGetCol() {
    assertEquals(3, rook1B.getColumn());
    assertEquals(0, rook2B.getColumn());
    assertEquals(5, rook1W.getColumn());
    assertEquals(7, rook2W.getColumn());
  }

  @Test
  public void testGetRow() {
    assertEquals(4, rook1B.getRow());
    assertEquals(3, rook2B.getRow());
    assertEquals(5, rook1W.getRow());
    assertEquals(0, rook2W.getRow());
  }

  @Test
  public void testGetColor() {
    assertEquals(Color.BLACK, rook1B.getColor());
    assertEquals(Color.WHITE, rook1W.getColor());
    assertEquals(Color.BLACK, rook2B.getColor());
    assertEquals(Color.WHITE, rook2W.getColor());
  }

  @Test
  public void testCanMove() {
    // move vertical 2, 0
    assertTrue(rook1B.canMove(6, 3));

    // move vertical -1, 0
    assertTrue(rook2B.canMove(3, 3));

    // move horizontal 0, +1
    assertTrue(rook1W.canMove(5, 6));

    // move horizontal 0, -1
    assertTrue(rook2W.canMove(0, 6));

    assertFalse(rook2W.canMove(1, 6));
    assertFalse(rook1W.canMove(7, 6));
  }

  @Test
  public void testCanKill() {
    // move vertical 2, 0
    assertTrue(rook1B.canKill(new Rook(6, 3, Color.WHITE)));

    // move vertical -1, 0
    assertTrue(rook2B.canKill(new Knight(3, 3, Color.WHITE)));

    // move horizontal 0, +1
    assertTrue(rook1W.canKill(new Rook(5, 6, Color.BLACK)));

    // move horizontal 0, -1
    assertTrue(rook2W.canKill(new Rook(0, 6, Color.BLACK)));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosRow1() {
    // row < 0
    new Rook(-2, 3, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosRow2() {
    // row > 7
    new Rook(8, 1, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosCol1() {
    // column > 7
    new Rook(0, 8, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosCol2() {
    // column < 0
    new Rook(4, -1, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillBlackFriend() {
    rook1B.canKill(new Knight(1, 7, Color.BLACK));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillWhiteFriend() {
    rook1W.canKill(new Rook(2, 2, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillSamePos() {
    rook2B.canKill(new Pawn(3, 0, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow1() {
    // row > 7
    rook1B.canMove(8, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow2() {
    // row < 0
    rook2B.canMove(-1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol1() {
    // column < 0
    rook1W.canMove(3, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol2() {
    // column > 7
    rook2W.canMove(0, 8);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveSamePos() {
    rook2W.canMove(0, 7);
  }
}
