import java.util.List;

/**
 * This is a Board2D class that represents a 2D cell position on the 8X8 grid chess
 * board. The position is denoted as a List type of integers as row and col.
 */
public class Board2D {
  private final List<Integer> reference;

  /**
   * Construct the Board2D class by initializing row and col which
   * make up a List type of reference as the class field.
   * @param row the first element of reference
   * @param col the second element of reference
   */
  public Board2D(int row, int col) {
    this.reference = List.of(row, col);
  }

  /**
   * Return the absolute difference between the first element of reference
   * and a target row number.
   * @param row the target row number
   * @return the absolute difference value
   */
  public int getDeltaRow(int row) {
    return Math.abs(this.getPosX() - row);
  }

  /**
   * Return the absolute difference between the second element of reference
   * and a target column number.
   * @param col the target column number
   * @return the absolute difference value
   */
  public int getDeltaCol(int col) {
    return Math.abs(this.getPosY() - col);
  }

  /**
   * Return true if the reference  has the same row number and column
   * number compared to given (row, col) inputs, else return false.
   * @param row input row number
   * @param col input column number
   * @return boolean value after comparison
   */
  public boolean equalPos(int row, int col) {
    return this.getPosX() == row && this.getPosY() == col;
  }

  /**
   * Return the first element of the Board2D reference.
   * @return the first element
   */
  public int getPosX() {
    return this.reference.get(0);
  }

  /**
   * Return the second element of the Board2D reference.
   * @return the second element
   */
  public int getPosY() {
    return this.reference.get(1);
  }

}
