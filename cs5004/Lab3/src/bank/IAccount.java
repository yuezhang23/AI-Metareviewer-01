package bank;

/**
 * This interface represents a bank account. It is the super-type for
 * any other type of traditional financial account a bank might offer.
 */
public interface IAccount {

  /**
   * Represent a deposit operation of traditional bank by adding amount
   * input to existing account balance.
   * @param amount the amount to deposit
   * @throws IllegalArgumentException if amount is negative
   */
  void deposit(double amount);

  /**
   * Return true if withdraw successfully or false if withdraw amount is negative
   * or more than current amount of balance.
   * @param amount the amount to withdraw
   * @return true if withdraw succeed, false otherwise
   */
  boolean withdraw(double amount);

  /**
   * Return the balance in current account.
   * @return balance amount
   */
  double getBalance();

  /**
   * Charge fees for both checking account and savings account if specific
   * conditions are met and reset conditions to default after monthly charge.
   * To a checking account, the  if the balance falls below $100 at any time,
   * a maintenance fee of $5 is charged when the monthly maintenance is performed.
   * To a savings account, if the number of withdrawals is greater than 6, a penalty
   * of $14 is deducted from the account when monthly maintenance is performed.
   */
  void performMonthlyMaintenance();

}
