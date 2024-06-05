/**
 * This interface represents the polynomial ADT.
 */
public interface Polynomial {

  /**
   * Add a term to existing polynomial
   * @param coe the coefficient value of the term
   * @param pow the powers of the variable of the term
   * @throws IllegalArgumentException if power is negative
   */
  void addTerm(int coe, int pow) throws IllegalArgumentException;

  /**
   * Remove all terms from the polynomial with the same powers.
   * @param pow the power of a polynomial term
   */
  void removeTerm(int pow);

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
   * Return a type of polynomial after add current polynomial with the other one.
   * @param other the polynomial to add with
   * @return a new polynomial after add operation
   * @throws IllegalArgumentException if two objects added are not of same type.
   */
  Polynomial add(Polynomial other) throws IllegalArgumentException;

}
