/**
 * This class represents class Employee.
 * An Employee has an employeeName, a workRate and
 * a workHours.
 */
public class Employee {
  private final String employeeName;
  private final double workRate;
  private double workHours;

  /**
   * Constructs an Employee object by initializing instance
   * variables of employee name, work rate and work hours.
   * @param name the name of the employee
   * @param rate the pay rate $/per week
   * @throws IllegalArgumentException if name is empty or pay rate is less than 0.
   */
  public Employee(String name, double rate) throws IllegalArgumentException {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("employee name cannot be empty");
    }
    if (rate < 0) {
      throw new IllegalArgumentException("pay rate cannot be negative");
    }
    this.employeeName = name;
    this.workRate = rate;
    this.workHours = 0;
  }

  public boolean equals(Employee other) {
    if (other.employeeName == employeeName
            && other.workRate == workRate && other.workHours == workHours) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Add work hours to existing work hours.
   * @param hours the hours to be added
   * @throws IllegalArgumentException if updated work hours are not in the
   *        range of 0 to 168.
   */
  public void addHoursWorked(double hours) throws IllegalArgumentException {
    if (workHours + hours > 168) { // there is 168 hours in total for one week.
      throw new IllegalArgumentException("exceeding a total of week hours");
    } else if (workHours + hours < 0) {
      throw new IllegalArgumentException("work hours is non-negative");
    }
    workHours += hours;
  }

  /**
   * the total work hours for current week.
   * @return total work hours of current week
   */
  public double getHours() {
    return workHours;
  }

  /**
   * reset the total work hours to 0 for current week.
   */
  public void resetHoursWorked() {
    workHours = 0.0;
  }

  /**
   * get a Paycheck object in accordance with the current Employee object.
   * @return a Paycheck object for given employee
   */
  public PayCheck getWeeklyCheck() {
    return new PayCheck(employeeName, workRate, workHours);
  }

  /**
   * get a message displaying employee information in specific format.
   * @return a message displaying employee information
   */
  public String toString() {
    return String.format("Employee name: %s\n WorkRate: $%.1f per hour", employeeName, workRate);
  }
}
