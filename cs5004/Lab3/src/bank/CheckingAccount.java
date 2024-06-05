package bank;

/**
 * Represents a subclass of AccountAbstract class and also type of IAccount,
 * which functions as a traditional checking account.
 */
public class CheckingAccount extends AccountAbstract {
  private boolean balanceUnder100;

  /**
   * Construct a CheckingAccount object by initializing the starter amount
   * and set boolean value of balanceAmount to check if balance is less than 100.
   * @param starterAmount the minimum amount to create a CheckingAccount object
   */
  public CheckingAccount(double starterAmount) {
    super(starterAmount);
    balanceUnder100 = starterAmount < 100;
  }

  @Override
  public void performMonthlyMaintenance() {
    if (balanceUnder100) {
      this.balance -= 5;
      balanceUnder100 = false;
    }
  }

  @Override
  public boolean withdraw(double amount) {
    boolean successWithdraw = super.withdraw(amount);
    balanceUnder100 = balance < 100;
    return successWithdraw;
  }
}
