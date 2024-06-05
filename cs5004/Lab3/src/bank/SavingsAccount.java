package bank;

/**
 * Represents a subclass of AccountAbstract class and also type of IAccount,
 * which functions as a traditional savings account.
 */
public class SavingsAccount extends AccountAbstract {
  private int countWithdraw;

  /**
   * Construct a SavingsAccount object by initializing the starter amount
   * and set number of withdrawals to 0.
   * @param starterAmount the minimum amount to create a SavingsAccount object
   */
  public SavingsAccount(double starterAmount) {
    super(starterAmount);
    this.countWithdraw = 0;
  }

  @Override
  public boolean withdraw(double amount) {
    boolean successWithdraw = super.withdraw(amount);
    if (successWithdraw) {
      countWithdraw += 1;
    }
    return successWithdraw;
  }

  @Override
  public void performMonthlyMaintenance() {
    if (countWithdraw > 6) {
      this.balance -= 14;
      countWithdraw = 0;
    }
  }
}
