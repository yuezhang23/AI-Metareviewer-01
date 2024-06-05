public interface PolynomialADTList {

  /**
   * Return the polynomial after adding a node to existing polynomial.
   * @param term a term format of string added to current polynomial
   */
  PolynomialADTList addTerm(String term);

  /**
   * Return the polynomial after removing all terms from the polynomial
   * with the same given power.
   * @param pow the power of a polynomial term
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


  String toString();

  String toStringHelper(String data);

}
