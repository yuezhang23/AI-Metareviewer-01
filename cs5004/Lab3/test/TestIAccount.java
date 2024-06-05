import org.junit.Before;
import org.junit.Test;

import bank.CheckingAccount;
import bank.IAccount;
import bank.SavingsAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test all the methods which various types of the IAccount interface
 * should support.
 */
public class TestIAccount {
  private IAccount savingAccount1;
  private IAccount checkingAccount1;

  @Before
  public void setUp() {
    savingAccount1 = new SavingsAccount(100);
    checkingAccount1 = new CheckingAccount(180);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructS() {
    IAccount savingAccount2 = new SavingsAccount(0);
    savingAccount2.getBalance();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructC() {
    IAccount checkingAccount2 = new CheckingAccount(0.001);
    checkingAccount2.getBalance();
  }

  @Test
  public void testGetBalance() {
    assertEquals(180, checkingAccount1.getBalance(), 0.01);
    assertEquals(100, savingAccount1.getBalance(), 0.01);
  }

  @Test
  public void testWithdraw() {
    assertTrue(checkingAccount1.withdraw(80));
    assertEquals(100, checkingAccount1.getBalance(), 0.01);

    assertFalse(checkingAccount1.withdraw(120));
    assertFalse(checkingAccount1.withdraw(-10));

    assertTrue(savingAccount1.withdraw(80));
    assertEquals(20, savingAccount1.getBalance(), 0.01);

    assertFalse(savingAccount1.withdraw(30));
    assertFalse(savingAccount1.withdraw(-10));
  }

  @Test
  public void testMaintenanceS() {
    savingAccount1.withdraw(10);
    savingAccount1.withdraw(10);
    savingAccount1.withdraw(10);
    savingAccount1.withdraw(10);
    savingAccount1.withdraw(10);
    savingAccount1.withdraw(10);
    assertEquals(40, savingAccount1.getBalance(), 0.01);
    savingAccount1.performMonthlyMaintenance();
    assertEquals(40, savingAccount1.getBalance(), 0.01);

    savingAccount1.withdraw(10);
    assertEquals(30, savingAccount1.getBalance(), 0.01);
    savingAccount1.performMonthlyMaintenance();
    assertEquals(16, savingAccount1.getBalance(), 0.01);
  }

  @Test
  public void testMaintenanceC() {
    checkingAccount1.withdraw(80);
    assertEquals(100, checkingAccount1.getBalance(), 0.01);
    checkingAccount1.performMonthlyMaintenance();
    assertEquals(100, checkingAccount1.getBalance(), 0.01);

    checkingAccount1.withdraw(10);
    assertEquals(90, checkingAccount1.getBalance(), 0.01);
    checkingAccount1.performMonthlyMaintenance();
    assertEquals(85, checkingAccount1.getBalance(), 0.01);
  }

  @Test
  public void testDeposit() {
    checkingAccount1.deposit(50);
    assertEquals(230, checkingAccount1.getBalance(), 0.01);

    savingAccount1.deposit(30);
    assertEquals(130, savingAccount1.getBalance(), 0.01);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testDepositC() {
    checkingAccount1.deposit(-50);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testDepositS() {
    savingAccount1.deposit(-50);
  }

}
