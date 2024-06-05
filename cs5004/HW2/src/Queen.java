/**
 * This class represents a kind of ChessPiece called Queen class .
 */
public class Queen extends ChessAbstract {

  /**
   * Construct the Queen class by initializing three variables of row, column
   * and color.
   * @param row the row number that piece stands on chess board
   * @param col the column number that piece stands on chess board
   * @param qColor the color current piece represents
   */
  public Queen(int row, int col, Color qColor) {
    super(new Board2D(row, col), PieceName.QUEEN, qColor);
  }

//  @Override
//  public boolean canMove(int row, int col) throws IllegalArgumentException {
//    if (isNotValidPos(row, col) || (this.reference.equalPos(row, col))) {
//      throw new IllegalArgumentException("invalid board position");
//    } else {
//      return (this.reference.getDeltaRow(row) == this.reference.getDeltaCol(col)
//              || getRow() == row || getColumn() == col);
//    }
//  }
}
