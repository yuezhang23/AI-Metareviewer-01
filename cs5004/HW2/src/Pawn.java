/**
 * This class represents a kind of ChessPiece called Pawn class .
 */
public class Pawn extends ChessAbstract {

  /**
   * Construct the Pawn class by initializing three variables of row, column
   * and color.
   * @param row the row number that piece stands on chess board
   * @param col the column number that piece stands on chess board
   * @param pColor the color current piece represents
   */
  public Pawn(int row, int col, Color pColor) throws IllegalArgumentException {
    super(new Board2D(row, col), PieceName.PAWN, pColor);
  }

//  @Override
//  public boolean canMove(int row, int col) throws IllegalArgumentException {
//    if (isNotValidPos(row, col)) {
//      throw new IllegalArgumentException("invalid board position");
//    } else if (this.getColumn() == col) {
//      return (this.getRow() == row + 1 && this.getColor() == Color.BLACK)
//              || (this.getRow() == row - 1 && this.getColor() == Color.WHITE);
//    } else {
//      return false;
//    }
//  }

  @Override
  public boolean canKill(ChessPiece piece) throws IllegalArgumentException {
    if (piece.getColor() == this.color) {
      throw new IllegalArgumentException("Only kill your enemy!");
    } else if (this.reference.equalPos(piece.getRow(), piece.getColumn())) {
      throw new IllegalArgumentException("two pieces cannot be at the same position");
    } else if (this.reference.getDeltaCol(piece.getColumn()) == 1) {
      return this.canMove(piece.getRow(), this.getColumn());
    } else {
      return false;
    }
  }

}
