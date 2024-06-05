/**
 * This class represents class FibonacciCounter.
 * A FibonacciCounter has a count number and its corresponding fibonacci number.
 */
public class FibonacciCounter {
  private final int count;
  private final int fibonacciNumber;

  /**
   * Constructs a FibonacciCounter object by initializing instance
   * variables of count and fibonacciNumber at count position.
   * @param n the count number
   * @throws IllegalArgumentException if name is empty or pay rate is less than 0
   *        or work hours are not between 0 and 168.
   */
  public FibonacciCounter(int n) {
    fibonacciNumber = isValidN(n);
    count = n;
  }

  /**
   * a helper method that calculate the fibonacci number at count position.
   * @param n the count number
   * @return the corresponding fibonacci number
   * @throws IllegalArgumentException if count number is less than 0 or
   *        if the fibonacci number exceeds the maximum value as an int
   */
  private int isValidN(int n) throws IllegalArgumentException {
    if (n < 0) {
      throw new IllegalArgumentException("count is non-negative");
    }

    long tempNumber = (long) (1 / Math.sqrt(5) * (Math.pow(0.5 * (1 + Math.sqrt(5)), n)
                - Math.pow(0.5 * (1 - Math.sqrt(5)), n)));
    if (tempNumber > (long)(Math.pow(2, 31) - 1)) {
      throw new IllegalArgumentException("encounter an integer overflow");
    } else {
      return (int)tempNumber;
    }
  }

  /**
   * Gets a new FibonacciCounter object at the next count position.
   * @return the next FibonacciCounter object
   * @throws IllegalArgumentException if the fibonacci number
   *        exceeds the maximum value as an int
   */
  public FibonacciCounter countPlusFib() throws IllegalArgumentException {
    if (isValidN(count + 1) > 0) {
      return new FibonacciCounter(count + 1);
    } else {
      throw new IllegalArgumentException("encounter an integer overflow");
    }
  }

  /**
   * Gets a new FibonacciCounter object at the last count position.
   * @return the prior FibonacciCounter object or the current object
   */
  public FibonacciCounter countMinusFib() {
    if (count == 0) {
      return new FibonacciCounter(0);
    } else {
      return new FibonacciCounter(count - 1);
    }
  }

  /**
   * Gets the count position of current object.
   * @return the count position
   */
  public int getCount() {
    return count;
  }

  /**
   * Gets the fibonacci number at the count position.
   * @return the fibonacci number at the count position
   */
  public int getFibNumber() {
    return fibonacciNumber;
  }
}
