public class PolynomialEmptyNode implements PolynomialADTList {

  @Override
  public PolynomialADTList addTerm(String term) {
    if (term == "") {
      return new PolynomialEmptyNode();
    } else {
      return new PolynomialElementNode(term, new PolynomialEmptyNode());
    }
  }

  @Override
  public PolynomialADTList removeTerm(int pow) {
    return new PolynomialEmptyNode();
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
    return "";
  }

  // “2x^5 -3x^2 +4x^1 -10”
  // “2x^5 -3x^2 +4x^1 -10”
  @Override
  public String toStringHelper(String data) {
    return data;
  }

}
