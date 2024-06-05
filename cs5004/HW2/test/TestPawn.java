import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains all the unit tests for Pawn class, type of ChessPiece.
 */
public class TestPawn {
  ChessPiece pawn1B;
  ChessPiece pawn1W;
  ChessPiece pawn2B;
  ChessPiece pawn2W;

  @Before
  public void setUp() {
    // in the middle
    pawn2B = new Pawn(2, 6, Color.BLACK);
    pawn1W = new Pawn(5, 5, Color.WHITE);

    // along the edge
    pawn1B = new Pawn(4, 0, Color.BLACK);

    // in the corner
    pawn2W = new Pawn(1, 7, Color.WHITE);
  }

  @Test
  public void testGetCol() {
    assertEquals(0, pawn1B.getColumn());
    assertEquals(6, pawn2B.getColumn());
    assertEquals(5, pawn1W.getColumn());
    assertEquals(7, pawn2W.getColumn());
  }

  @Test
  public void testGetRow() {
    assertEquals(4, pawn1B.getRow());
    assertEquals(2, pawn2B.getRow());
    assertEquals(5, pawn1W.getRow());
    assertEquals(1, pawn2W.getRow());
  }

  @Test
  public void testGetColor() {
    assertEquals(Color.BLACK, pawn1B.getColor());
    assertEquals(Color.WHITE, pawn1W.getColor());
    assertEquals(Color.BLACK, pawn2B.getColor());
    assertEquals(Color.WHITE, pawn2W.getColor());
  }

  @Test
  public void testCanMove() {
    // move -1, 0
    assertTrue(pawn1B.canMove(3, 0));

    // move -1, 0
    assertTrue(pawn2B.canMove(1, 6));

    // move +1, 0
    assertTrue(pawn1W.canMove(6, 5));

    // move +1, 0
    assertTrue(pawn2W.canMove(2, 7));
  }

  @Test
  public void testCanKill() {
    // move -1, +1
    assertTrue(pawn1B.canKill(new Bishop(3, 1, Color.WHITE)));

    // move -1, -1
    assertTrue(pawn2B.canKill(new Queen(1, 5, Color.WHITE)));

    // move +1, +1
    assertTrue(pawn1W.canKill(new Pawn(6, 6, Color.BLACK)));

    // move +1, -1
    assertTrue(pawn2W.canKill(new Rook(2, 6, Color.BLACK)));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRow1() {
    // row < 0
    new Pawn(-3, 3, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRow2() {
    // row > 7
    new Pawn(9, 1, Color.BLACK);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCol1() {
    // column > 7
    new Pawn(0, 10, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCol2() {
    // column < 0
    new Pawn(4, -1, Color.WHITE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillBlackFriend() {
    pawn1B.canKill(new Bishop(1, 7, Color.BLACK));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillWhiteFriend() {
    pawn1W.canKill(new Queen(2, 2, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testKillSamePos() {
    pawn2B.canKill(new Pawn(2, 6, Color.WHITE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow1() {
    // row > 7
    pawn1B.canMove(8, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidRow2() {
    // row < 0
    pawn2B.canMove(-1, 1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol1() {
    // column < 0
    pawn1W.canMove(3, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidCol2() {
    // column > 7
    pawn2W.canMove(1, 8);
  }

  @Test
  public void testMoveInvalid1() {
    assertFalse(pawn1B.canMove(3, 1));  // 4 0
  }

  @Test
  public void testMoveInvalid2() {
    assertFalse(pawn1W.canMove(6, 4));  // 5 5
  }
}
