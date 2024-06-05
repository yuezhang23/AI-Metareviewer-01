package bank;

/**
 * Represents an abstract class type of IAccount, which covers most of
 * the functions a traditional financial account can operate with.
 */
public abstract class AccountAbstract implements IAccount {
  protected double balance;

  /**
   * Construct an AccountAbstract by initializing account balance with
   * a starter amount and will fail if start amount is too small.
   * @param starterAmount the minimum amount required to open an account
   * @throws IllegalArgumentException if starteramount is less than 0.01
   */
  public AccountAbstract(double starterAmount)
          throws IllegalArgumentException {
    if (starterAmount < 0.01) {
      throw new IllegalArgumentException("too little deposit to open an account");
    }
    this.balance = starterAmount;
  }

  @Override
  public void deposit(double amount) throws IllegalArgumentException {
    if (amount < 0) {
      throw new IllegalArgumentException("can't deposit negative money!");
    }
    this.balance += amount;
  }

  @Override
  public boolean withdraw(double amount) {
    if (amount < 0 || balance < amount) {
      return false;
    } else {
      this.balance -= amount;
      return true;
    }
  }

  @Override
  public double getBalance() {
    return this.balance;
  }

  /**
   * Print out current account balance in specific format.
   * @return balance with two decimal places
   */
  public String toString() {
    return String.format("$%.2f", balance);

  }
}