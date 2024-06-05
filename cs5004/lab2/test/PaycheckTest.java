import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for the Paycheck class.
 */
public class PaycheckTest {
  private PayCheck nameJohn;
  private PayCheck nameEmily;
  private PayCheck nameAron;

  @Before
  public void setUp() {
    nameJohn = new PayCheck("John", 15, 55);
    nameEmily = new PayCheck("Emily", 30, 30);
    nameAron = new PayCheck("Aron", 0, 15);
  }

  // invalid employee name.
  @Test (expected = IllegalStateException.class)
  public void testInvalidName() {
    new PayCheck("", 15, 55);
  }

  // invalid work rate.
  @Test (expected = IllegalStateException.class)
  public void testInvalidRate() {
    new PayCheck("kate", -1, 55);
  }

  // invalid work hours, exceeding up limit.
  @Test(expected = IllegalStateException.class)
  public void testInvalidAddHours() {
    new PayCheck("kate", 25, 169);
  }

  // invalid work hours, -ve.
  @Test(expected = IllegalStateException.class)
  public void testAddedNegHours() {
    new PayCheck("kate", 25, -1);
  }

  // valid total payments for a week.
  @Test
  public void testGetTotalPay() {
    assertEquals(937.5, nameJohn.getTotalPay(), 0.01);
    assertEquals(900, nameEmily.getTotalPay(), 0.01);
    assertEquals(0, nameAron.getTotalPay(), 0.01);
  }

  @Test
  public void testToString() {
    String expected = "the total paycheck for the week is $937.50.";
    assertEquals(expected, nameJohn.toString());
  }
}
