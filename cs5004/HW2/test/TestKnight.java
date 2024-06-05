import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains all the unit tests for knight class, type of ChessPiece.
 */
public class TestKnight {
  ChessPiece knight1B;
  ChessPiece knight1W;
  ChessPiece knight2B;
  ChessPiece knight2W;

  @Before
  public void setUp() {
    // in the middle
    knight1B = new Knight(4, 3, Color.BLACK);
    knight2B = new Knight(2, 6, Color.BLACK);

    // along the edge
    knight1W = new Knight(5, 0, Color.WHITE);

    // in the corner
    knight2W = new Knight(0, 7, Color.WHITE);
  }

  @Test
  public void testGetCol() {
    assertEquals(3, knight1B.getColumn());
    assertEquals(6, knight2B.getColumn());
    assertEquals(0, knight1W.getColumn());
    assertEquals(7, knight2W.getColumn());
  }

  @Test
  public void testGetRow() {
    assertEquals(4, knight1B.getRow());
    assertEquals(2, knight2B.getRow());
    assertEquals(5, knight1W.getRow());
    assertEquals(0, knight2W.getRow());
  }

  @Test
  public void testGetColor() {
    assertEquals(Color.BLACK, knight1B.getColor());
    assertEquals(Color.WHITE, knight1W.getColor());
    assertEquals(Color.BLACK, knight2B.getColor());
    assertEquals(Color.WHITE, knight2W.getColor());
  }

  @Test
  public void testCanMove() {
    // move -1, -2
    assertTrue(knight1B.canMove(3, 1));

    // move -1, +2
    assertTrue(knight1B.canMove(3, 5));

    // move +1, -2
    assertTrue(knight2W.canMove(1, 5));

    // move +1, +2
    assertTrue(knight1W.canMove(6, 2));

    // move +2, +1
    assertTrue(knight2B.canMove(4, 7));

    // move +2, -1
    assertTrue(knight2B.canMove(4, 5));

    // move -2, +1
    assertTrue(knight1W.canMove(3, 1));

    // move -2 -1
    assertTrue(knight1B.canMove(2, 2));


    assertFalse(knight1B.canMove(4, 2));
    assertFalse(knight1W.canMove(1, 1));
  }

  @Test
  public void testCanKill() {
    // move -1, -2
    assertTrue(knight1B.canKill(new Bishop(3, 1, Color.WHITE)));

    // move -1, +2
    assertTrue(knight1B.canKill(new Bishop(3, 5, Color.WHITE)));

    // move +1, -2
    assertTrue(knight2W.canKill(new Rook(1, 5, Color.BLACK)));

    // move +1, +2
    assertTrue(knight1W.canKill(new Knight(6, 2, Color.BLACK)));

    // move +2, +1
    assertTrue(knight2B.canKill(new Queen(4, 7, Color.WHITE)));

    // move +2, -1
    assertTrue(knight2B.canKill(new Queen(4, 5, Color.WHITE)));

    // move -2, +1
    assertTrue(knight1W.canKill(new Knight(3, 1, Color.BLACK)));

    // move -2 -1
    assertTrue(knight1B.canKill(new Rook(2, 2, Color.WHITE)));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosRow1() {
    // row < 0
    new Knight(-3, 3, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosRow2() {
    // row > 7
    new Knight(9, 1, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosCol1() {
    // column > 7
    new Knight(0, 10, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPosCol2() {
    // column < 0
    new Knight(4, -1, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillBlackFriend() {
    knight1B.canKill(new Bishop(1, 7, Color.BLACK));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillWhiteFriend() {
    knight1W.canKill(new Queen(2, 2, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillSamePos() {
    knight2B.canKill(new Knight(2, 6, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow1() {
    // row > 7
    knight1B.canMove(8, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow2() {
    // row < 0
    knight2B.canMove(-1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol1() {
    // column < 0
    knight1W.canMove(3, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol2() {
    // column > 7
    knight2W.canMove(0, 8);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveSamePos() {
    assertFalse(knight1B.canMove(4, 3));
    assertFalse(knight2B.canMove(2, 6));
    assertFalse(knight1W.canMove(5, 0));
    assertFalse(knight2W.canMove(0, 7));
  }
}
