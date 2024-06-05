/**
 * This class represents class Paycheck.
 * A Paycheck has an employeeName, a workRate , a workHours
 * and a totalPay.
 */
public class PayCheck {
  private final String employName;
  private final double workRate;
  private final double workHours;
  private double totalPay;

  /**
   * Constructs a Paycheck object by initializing instance
   * variables of employee name, work rate and work hours.
   * @param name the name of the employee
   * @param rate the pay rate $/per week
   * @param hours the work hours in current week
   * @throws IllegalArgumentException if name is empty or pay rate is less than 0
   *        or work hours are not between 0 and 168.
   */
  public PayCheck(String name, double rate, double hours) throws IllegalArgumentException {
    if (name.isEmpty()) {
      throw new IllegalStateException("employee name cannot be empty");
    }
    if (rate < 0) {
      throw new IllegalStateException("pay rate cannot be negative");
    }
    if (hours < 0 || hours > 168) {
      throw new IllegalStateException("working hours can't be negative");
    }
    this.employName = name;
    this.workRate = rate;
    this.workHours = hours;
    this.totalPay = this.calTotalPay(); // call from helper function to calculate total payments.
  }

  /**
   * the helper method that calculates total work payments for current week.
   * @return total work payments of current week
   */
  private double calTotalPay() {
    if (workHours <= 40) {
      totalPay = workRate * workHours;
    } else {
      totalPay = workRate * 40 + 1.5 * workRate * (workHours - 40);
    }
    return totalPay;
  }

  /**
   * get the total work payments for current week.
   * @return the total work payments
   */
  public double getTotalPay() {
    return this.totalPay;
  }

  /**
   * get a message displaying employee information in specific format.
   * @return  a message displaying employee payment
   */
  public String toString() {
    return String.format("the total paycheck for %s in this week is $%.2f.",
            this.employName, this.getTotalPay());
  }

  public boolean equals(PayCheck other) {
    if (other.employName == employName
            && other.workRate == workRate && other.workHours == workHours) {
      return true;
    } else {
      return false;
    }
  }
}
