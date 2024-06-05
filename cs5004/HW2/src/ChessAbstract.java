/**
 * This class represents an abstract class type of ChessPiece.
 */
public abstract class ChessAbstract implements ChessPiece {
  protected Board2D reference;
  protected PieceName piece;
  protected Color color;

  /**
   * Construct a ChessAbstract by initializing three variables of reference,
   * piece and color.
   * @param position a Board2D type of position representation on chess board
   * @param name a piece name from the PieceName enum
   * @param color the color current piece represents
   * @throws IllegalArgumentException if the row and column numbers of position
   *        are not within the board range.
   */
  public ChessAbstract(Board2D position, PieceName name, Color color)
          throws IllegalArgumentException {
    if (isNotValidPos(position.getPosX(), position.getPosY())) {
      throw new IllegalArgumentException("invalid board position");
    }
    this.reference = position;
    this.piece = name;
    this.color = color;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public int getRow() {
    return this.reference.getPosX();
  }

  @Override
  public int getColumn() {
    return this.reference.getPosY();
  }

  @Override
  public boolean canKill(ChessPiece piece) throws IllegalArgumentException {
    if (piece.getColor() == this.color) {
      throw new IllegalArgumentException("Only kill your enemy!");
    } else if (this.reference.equalPos(piece.getRow(), piece.getColumn())) {
      throw new IllegalArgumentException("two pieces can't be at the same position");
    } else {
      return this.canMove(piece.getRow(), piece.getColumn());
    }
  }
  
  /**
   * Return true if given x and y are within board range from 0 to 7.
   * @return boolean value after the comparison
   */
  protected boolean isNotValidPos(int x, int y) {
    return x > 7 || x < 0 || y > 7 || y < 0;
  }

  @Override
  public boolean canMove(int row, int col) throws IllegalArgumentException {
    if (isNotValidPos(row, col) || (this.reference.equalPos(row, col))) {
      throw new IllegalArgumentException("invalid board position");
    }
    switch (piece) {
      case PAWN:
        return this.getColumn() == col &&
                ((this.getRow() == row + 1 && this.getColor() == Color.BLACK)
                        || (this.getRow() == row - 1 && this.getColor() == Color.WHITE));
      case ROOK:
        return getRow() == row || getColumn() == col;
      case QUEEN:
        return this.reference.getDeltaRow(row) == this.reference.getDeltaCol(col)
                || getRow() == row || getColumn() == col;
      case BISHOP:
        return this.reference.getDeltaRow(row) == this.reference.getDeltaCol(col);
      case KNIGHT:
        return (this.reference.getDeltaRow(row) == 1 && this.reference.getDeltaCol(col) == 2)
                || (this.reference.getDeltaRow(row) == 2 && this.reference.getDeltaCol(col) == 1);
      default:
        return false;
    }
  }
}
