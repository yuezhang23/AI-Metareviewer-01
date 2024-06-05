package cs5004.marblesolitaire;

/**
 * This interface reads data from an array of String and
 * parse in data to MarbleSolitaireModel constructor.
 */
public interface Parser {

  /**
   * Look through the array and find required number that sets the longest
   * arm length of the board.
   * @return the longest arm length of the board
   */
  int getArmLength();

  /**
   * Look through the array and find required number that sets row position of the empty slot.
   * @return the row number of the position where empty slot sets on the board
   */
  int getPosRow();

  /**
   * Look through the array and find required number that sets col position of the empty slot.
   * @return the column number of the position where empty slot sets on the board
   */
  int getPosCol();
}
