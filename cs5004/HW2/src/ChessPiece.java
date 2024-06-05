/**
 * This interface contains all operations that all types of ChessPiece
 * should support.
 */
public interface ChessPiece {

  /**
   * Return the row number of the position where the chess piece stands on chess board.
   * @return the row number
   */
  int getRow();

  /**
   * Return the column number of the position where the chess piece stands on chess board.
   * @return the column number
   */
  int getColumn();

  /**
   * Return the color of the given chess piece.
   * @return the element from the Color Enum
   */
  Color getColor();

  /**
   * Return true if the chess piece could move to given position, otherwise false.
   * @param row the row number of the position the piece is heading to.
   * @param col the col number of the position the piece is heading to.
   * @return boolean value of the movement
   */
  boolean canMove(int row, int col);

  /**
   * Return true if the chess piece could move to given position where
   * another chess piece stands.Return false otherwise.
   * @param piece another chess piece that stands at the target position
   * @return boolean value of the movement that will replace another piece
   */
  boolean canKill(ChessPiece piece);





}
