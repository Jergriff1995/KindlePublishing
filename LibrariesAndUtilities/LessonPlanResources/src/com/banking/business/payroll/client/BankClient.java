package com.banking.business.payroll.client;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The BankClient which connects to our company's bank
 * to retrieve our payroll account.
 */
public class BankClient {
    private BigDecimal balance = BigDecimal.valueOf(100000);

    /**
     * Private constructor for BankClient so other code can't instantiate us
     */
    public BankClient() { }

    /**
     * Withdraw funds from an account
     * @param amount The amount to withdraw
     * @return the updated balance
     */
    public BigDecimal withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount);
        System.out.println("Withdrew $" + amount.setScale(2, RoundingMode.HALF_UP));
        return balance;
    }
}
