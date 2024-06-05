import java.util.Scanner;

/**
 * This class implements the polynomial ADT.
 */
public class PolynomialImpl implements Polynomial {

  private PolynomialADTList list;

  /**
   * Construct a PolynomialImpl object as polynomial 0 with no parameters.
   */
  public PolynomialImpl() {
    this.list = new PolynomialEmptyNode();
  }

  /**
   * Construct a PolynomialImpl object that takes a polynomial as a string, parses it
   * and creates the polynomial accordingly.
   * @param other the string format of another polynomial
   * @throws IllegalArgumentException if input polynomial string is invalid
   */
  public PolynomialImpl(String other) throws IllegalArgumentException {
    if (this.isInvalidFormat(other, other.split(" "))) {
      throw new IllegalArgumentException("invalid polynomial string");
    }

    String[] stringList = other.split(" ");
    this.list = new PolynomialEmptyNode();
    for (String w:stringList) {
      String[] result = this.findIndex(w);
      list = list.addTerm(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
    }
  }

  // a helper method to include all invalid polynomial strings.
  private boolean isInvalidFormat(String poly, String[] polyList) {
    if ((poly.equals("") || poly.contains(" 0x") || poly.contains("x^0")
            || poly.contains("x^-") || poly.startsWith("+")) || poly.contains(".")) {
      return true;
    }
    for (int i = 1; i < polyList.length; i ++) {
      if (!(polyList[i].startsWith("+") || polyList[i].startsWith("-"))) {
        return true;
      }
    }
    return false;
  }

  // a helper method to find the coefficient and the power of a string.
  private String[] findIndex(String data) {
    String[] result = new String[2];
    if (data.contains("x")) {
      Scanner s = new Scanner(data.replace("^", "")).useDelimiter("\\s*x\\s*");
      result[0] = s.next();
      result[1] = s.next();
    } else if (!data.equals("")) {
      result[0] = data;
      result[1] = "0";
    }
    return result;
  }

  @Override
  public String toString() {
    return list.toString();
  }

  @Override
  public void addTerm(int coe, int pow) throws IllegalArgumentException {
    if (pow < 0) {
      throw new IllegalArgumentException("negative power can't be added to polynomial");
    }
    list = list.addTerm(coe, pow);
  }

  @Override
  public void removeTerm(int pow) {
    list = list.removeTerm(pow);
  }

  @Override
  public int getDegree() {
    return list.getDegree();
  }

  @Override
  public int getCoefficient(int pow) {
    return list.getCoefficient(pow);
  }

  @Override
  public double evaluate(double x) {
    return list.evaluate(x);
  }

  @Override
  public Polynomial add(Polynomial other) throws IllegalArgumentException {
    if (!(other instanceof PolynomialImpl)) {
      throw new IllegalArgumentException("added object is not the same type");
    }
    Polynomial newPoly = new PolynomialImpl(this.toString());
    for (int i = 0; i <= other.getDegree(); i ++) {
      newPoly.addTerm(other.getCoefficient(i), i);
    }
    return newPoly;
  }
}
