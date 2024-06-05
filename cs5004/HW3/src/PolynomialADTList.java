/**
 * This interface represents all operations for a node in a list of terms
 * implemented as a polynomial ADT.
 */
public interface PolynomialADTList {

  /**
   * Return a polynomial after adding a term to existing polynomial.
   * @param coe the coefficient value of the term
   * @param pow the powers of the variable of the term
   * @return polynomial after adding a term
   */
  PolynomialADTList addTerm(int coe, int pow);

  /**
   * Return the polynomial after removing all terms from the polynomial
   * with the same given power.
   * @param pow the power of a polynomial term
   * @return polynomial after removing terms
   */
  PolynomialADTList removeTerm(int pow);

  /**
   * Return the degree of this polynomial.
   * @return the degree number of the polynomial
   */
  int getDegree();

  /**
   * Return the coefficient of the term with specified power.
   * @param pow the power of the term to get coefficient from
   * @return the coefficient of the term
   */
  int getCoefficient(int pow);

  /**
   * Return the computation result of the polynomial.
   * @param x the variable of the polynomial
   * @return the computation result of polynomial
   */
  double evaluate(double x);

  /**
   * Print out polynomial in specific format of a string.
   * @return a string of polynomial
   */
  String toString();

  String toStringHelper(String data);
}
