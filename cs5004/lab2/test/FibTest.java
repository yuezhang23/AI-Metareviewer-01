import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for the FibTest class.
 */
public class FibTest {
  private FibonacciCounter fib1;
  private FibonacciCounter fib2;
  private FibonacciCounter fib3;
  private FibonacciCounter fib4;

  @Before
  public void setUp() {
    fib1 = new FibonacciCounter(6);
    fib2 = new FibonacciCounter(0);
    fib3 = new FibonacciCounter(18);
    fib4 = new FibonacciCounter(30);
  }

  // count -ve.
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCount() {
    new FibonacciCounter(-1);
  }

  // invalid count position with a fibonacci number exceeding maximum int value.
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidInt() {
    new FibonacciCounter(47);
  }

  // valid count and fibonacci numbers.
  @Test
  public void testGetFibNumber() {
    assertEquals(8, fib1.getFibNumber());
    assertEquals(0, fib2.getFibNumber());
    assertEquals(2584, fib3.getFibNumber());
    assertEquals(832040, fib4.getFibNumber());
  }

  @Test
  public void testMinusCount() {
    FibonacciCounter newFib2 = fib2.countMinusFib();
    assertEquals(0, newFib2.getCount());

    FibonacciCounter newFib1 = fib1.countMinusFib();
    assertEquals(5, newFib1.getCount());
  }

  @Test
  public void testPlusCount() {
    FibonacciCounter newFib2 = fib2.countPlusFib();
    assertEquals(1, newFib2.getCount());

    FibonacciCounter newFib1 = fib1.countPlusFib();
    assertEquals(7, newFib1.getCount());
  }

  // invalid next count position with a fibonacci number exceeding maximum int value.
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPlus() {
    new FibonacciCounter(46).countPlusFib();
  }

  @Test
  public void testGetCount() {
    assertEquals(0, fib2.getCount());
    assertEquals(30, fib4.getCount());
  }
}
