import java.util.Scanner;

public class PolynomialElementNode implements PolynomialADTList {

  private String term;
  private PolynomialADTList next;
  private int coefficient;
  private int power;

  public PolynomialElementNode(String term, PolynomialADTList next) throws  IllegalStateException {
    this.term = term;
    this.next = next;
    this.coefficient = this.findIndex(term)[0];
    this.power = this.findIndex(term)[1];

    if (power < 0) {
      throw new IllegalStateException("negative power");
    }

  }

  private int[] findIndex(String data) {  // "", "5", "5x^-2", "+5x^2","-5x^-8"
    int[] result = new int[2];
    if (data.contains("x")) {
      Scanner s = new Scanner(data.replace("^","").replace("+","")).useDelimiter("\\s*x\\s*");
      result[0] = s.nextInt();
      result[1] = s.nextInt();
    } else if (data != ""){
      result[0] = Integer.parseInt(data);
    }
    return result;
  }

  @Override
  public PolynomialADTList addTerm(String term) {
    int newCoe = this.findIndex(term)[0];
    int newPow = this.findIndex(term)[1];

    if (newPow == power) {
      this.coefficient += newCoe;

      if (coefficient == 0) {
        this.removeTerm(power);
      } else if (newPow == 0) {
        this.term = String.format("%d", coefficient);
      } else {
        this.term = String.format("%dx^%d", coefficient, power);
      }
    } else if (newPow > power) {  // add head
      this.next = new PolynomialElementNode(this.term, this.next);
      this.term = term;
      this.coefficient = newCoe;
      this.power = newPow;
    } else {
      this.next = this.next.addTerm(term);
    }
    return this;
  }

  @Override
  public PolynomialADTList removeTerm(int pow) {
    if (power == pow) { // remove head
      this.term = "";
    } else {
      this.next = this.next.removeTerm(pow);
    }
    return this;
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
    double value;
    if (term == "") {
      value = 0;
    } else {
      value = this.coefficient * Math.pow(x, this.power);
    }
    return value + this.next.evaluate(x);
  }


  @Override
  public String toString() {
    if (term == "") {
      return this.next.toString();
    } else {
      return this.next.toStringHelper(term);
    }
  }

  // “2x^5 -3x^2 +4x^1 -10”
  // “2x^5 -3x^2 +4x^1 -10”
  @Override
  public String toStringHelper(String data) {
    if (coefficient == 0 || term == "") {
      return this.next.toStringHelper(data);
    } else if (coefficient < 0) {
      return this.next.toStringHelper(data + " " + term);
    } else {
      return this.next.toStringHelper(data + " " + "+" + term);
    }
  }

}
