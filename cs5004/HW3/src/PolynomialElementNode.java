/****
 * This class implements the polynomial ADT list using linked list with an element node.
 */
public class PolynomialElementNode implements PolynomialADTList {

  private int coefficient;
  private final int power;
  private PolynomialADTList next;
  private String term;

  /**
   * Construct a node with elements by initializing coefficient and
   * power of the term in a polynomial.
   * @param coe the coefficient to pass in a term
   * @param pow the power to pass in the variable of the term
   * @param next the rest of the polynomial
   */
  public PolynomialElementNode(int coe, int pow, PolynomialADTList next) {
    this.coefficient = coe;
    this.power = pow;
    this.next = next;
    this.term = this.updateTerm();
  }

  private String updateTerm() {
    String temp;
    if (coefficient == 0) {
      temp = "";
    } else if (power == 0) {
      temp = String.format("%d", coefficient);
    } else {
      temp = String.format("%dx^%d", coefficient, power);
    }
    return temp;
  }

  @Override
  public PolynomialADTList addTerm(int coe, int pow) {
    if (pow < 0) {
      throw new IllegalArgumentException("negative power detected");
    } else if (coe == 0) {
      return this;

    } else if (pow == power) {
      this.coefficient += coe;
      this.term = this.updateTerm();
      if (coefficient == 0) {
        return this.next;
      } else {
        return this;
      }

    } else if (pow > power) {
      return new PolynomialElementNode(coe, pow, this);

    } else {
      this.next = this.next.addTerm(coe, pow);
      return this;
    }
  }

  @Override
  public PolynomialADTList removeTerm(int pow) {
    if (power == pow) {
      return this.next;
    } else {
      this.next = this.next.removeTerm(pow);
      return this;
    }
  }

  @Override
  public int getDegree() {
    return this.power;
  }

  @Override
  public int getCoefficient(int pow) {
    if (pow == power) {
      return this.coefficient;
    } else {
      return this.next.getCoefficient(pow);
    }
  }

  @Override
  public double evaluate(double x) {
    double value = this.coefficient * Math.pow(x, this.power);
    return value + this.next.evaluate(x);
  }

  @Override
  public String toString() {
    if (term.equals("")) {
      return this.next.toString();
    } else {
      return this.next.toStringHelper(term);
    }
  }

  @Override
  public String toStringHelper(String data) {
    if (coefficient < 0) {
      return this.next.toStringHelper(data + " " + term);
    } else {
      return this.next.toStringHelper(data + " " + "+" + term);
    }
  }
}