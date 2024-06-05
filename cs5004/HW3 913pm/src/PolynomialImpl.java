/**
 * This class implements the polynomial ADT
 */
public class PolynomialImpl implements Polynomial {

  private PolynomialADTList list;

  public PolynomialImpl() {
    this.list = new PolynomialElementNode("0", new PolynomialEmptyNode());
  }

  public PolynomialImpl(String other) {
    String[] stringList = other.split(" ");
    this.list = new PolynomialEmptyNode();
    for (int i = 0; i < stringList.length; i ++) {
      list = list.addTerm(stringList[i]);
    }
  }

  public String toString() {
    return list.toString();
  }

  @Override
  public void addTerm(int coe, int pow) throws IllegalArgumentException {
    String tempTerm;
    if (pow < 0) {
      throw new IllegalArgumentException("negative power can't be added to polynomial");
    } else if (coe == 0) {
      tempTerm = "";
    } else if (pow == 0) {
      tempTerm = String.format("%d", coe);
    } else {
      tempTerm = String.format("%dx^%d", coe, pow);
    }
    list = list.addTerm(tempTerm); // list method add sort
  }

  @Override
  public void removeTerm(int pow) {
    list = list.removeTerm(pow);
  }

  @Override
  public int getDegree() {
    return list.getDegree();
  }
//
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
    Polynomial newPoly = new PolynomialImpl(this.toString());
    for (int i= 0; i <= other.getDegree(); i ++) {
      newPoly.addTerm(other.getCoefficient(i), i);
    }
    return newPoly;
  }

}
