/**
 * This class represents a kind of ChessPiece called Knight class .
 */
public class Knight extends ChessAbstract {

  /**
   * Construct the Knight class by initializing three variables of row, column
   * and color.
   * @param row the row number that piece stands on chess board
   * @param col the column number that piece stands on chess board
   * @param kColor the color current piece represents
   */
  public Knight(int row, int col, Color kColor) {
    super(new Board2D(row, col), PieceName.KNIGHT, kColor);
  }

//  @Override
//  public boolean canMove(int row, int col) throws IllegalArgumentException {
//    if (isNotValidPos(row, col)) {
//      throw new IllegalArgumentException("invalid board position");
//    } else {
//      return ((this.reference.getDeltaRow(row) == 1 && this.reference.getDeltaCol(col) == 2)
//              || (this.reference.getDeltaRow(row) == 2 && this.reference.getDeltaCol(col) == 1));
//    }
//  }
}
