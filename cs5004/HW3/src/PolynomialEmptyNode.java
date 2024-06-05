/****
 * This class implements the polynomial ADT list using linked list with an empty node.
 */
public class PolynomialEmptyNode implements PolynomialADTList {

  @Override
  public PolynomialADTList addTerm(int coe, int pow) {
    if (pow < 0) {
      throw new IllegalArgumentException("negative power detected!");
    } else if (coe == 0) {
      return this;
    } else {
      return new PolynomialElementNode(coe, pow, new PolynomialEmptyNode());
    }
  }

  @Override
  public PolynomialADTList removeTerm(int pow) {
    return this;
  }

  @Override
  public int getDegree() {
    return 0;
  }

  @Override
  public int getCoefficient(int pow) {
    return 0;
  }

  @Override
  public double evaluate(double x) {
    return 0;
  }

  @Override
  public String toString() {
    return "0";
  }

  @Override
  public String toStringHelper(String data) {
    return data;
  }

}
