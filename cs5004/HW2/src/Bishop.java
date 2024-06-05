/**
 * This class represents a kind of ChessPiece called Bishop class .
 */
public class Bishop extends ChessAbstract {

  /**
   * Construct the Bishop class by initializing three variables of row, column
   * and color.
   * @param row the row number that piece stands on chess board
   * @param col the column number that piece stands on chess board
   * @param bColor the color current piece represents
   */
  public Bishop(int row, int col, Color bColor) {
    super(new Board2D(row, col), PieceName.BISHOP, bColor);
  }

//  @Override
//  public boolean canMove(int row, int col) throws IllegalArgumentException {
//    if (isNotValidPos(row, col) || (this.reference.equalPos(row, col))) {
//      throw new IllegalArgumentException("invalid board position");
//    } else {
//      return this.reference.getDeltaRow(row) == this.reference.getDeltaCol(col);
//    }
//  }
}
