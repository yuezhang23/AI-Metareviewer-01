/**
 * This class represents a kind of ChessPiece called Rook class .
 */
public class Rook extends ChessAbstract {

  /**
   * Construct the Rook class by initializing three variables of row, column
   * and color.
   * @param row the row number that piece stands on chess board
   * @param col the column number that piece stands on chess board
   * @param rColor the color current piece represents
   */
  public Rook(int row, int col, Color rColor) {
    super(new Board2D(row, col), PieceName.ROOK, rColor);
  }

//  @Override
//  public boolean canMove(int row, int col) throws IllegalArgumentException {
//    if (isNotValidPos(row, col) || (this.reference.equalPos(row, col))) {
//      throw new IllegalArgumentException("invalid board position");
//    } else {
//      return (getRow() == row || getColumn() == col);
//    }
//  }
}
