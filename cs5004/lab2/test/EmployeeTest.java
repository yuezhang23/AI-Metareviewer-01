//import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * A JUnit test class for the Employee class.
 */
public class EmployeeTest {
  private Employee nameJohn = new Employee("John", 15);
  private Employee nameEmily = new Employee("Emily", 30);
  private Employee nameAron = new Employee("Aron", 0);

  /**
  public void setUp() {
    System.out.println("set up has been run");
    nameJohn = new Employee("John", 15);
    nameEmily = new Employee("Emily", 30);
    nameAron = new Employee("Aron", 0);
  }
  */

  // invalid employ name.
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidName() {
    new Employee("", 55);
  }

  // invalid work rate.
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRate() {
    new Employee("John", -5);
  }

  // reset hours to 0.
  @Test
  public void testResetHours() {
    assertNotEquals(35, nameEmily.getHours(), 0.001);

    nameEmily.resetHoursWorked();
    assertEquals(0, nameEmily.getHours(), 0.001);
  }

  // valid added hours between 0 and 168 (including 0 and 168).
  @Test
  public void testValidAddedHour() {
    nameJohn.addHoursWorked(168);
    assertEquals(168, nameJohn.getHours(), 0.01);

    nameEmily.addHoursWorked(45);
    nameEmily.addHoursWorked(-10);
    assertEquals(35, nameEmily.getHours(), 0.01);

    nameAron.addHoursWorked(0);
    assertEquals(0, nameAron.getHours(), 0.01);
  }

  // invalid work hours added.
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidAddHours() {
    nameJohn.addHoursWorked(169);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddedNegHours() {
    nameJohn.addHoursWorked(-1);
  }

  @Test
  public void testGetWeeklyPaycheck() {
    PayCheck copyJohn = new PayCheck("John", 15, 55);
    nameJohn.addHoursWorked(55);
    copyJohn.equals(nameJohn.getWeeklyCheck());
  }

  @Test
  public void testToString() {
    String expected = "Employee name: John\n WorkRate: $15.0 per hour";
    assertEquals(expected, nameJohn.toString());
  }
}
