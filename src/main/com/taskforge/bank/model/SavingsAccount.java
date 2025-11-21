package com.taskforge.bank.model;

public final class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.06; // 6% per year

    public SavingsAccount(long customerId, double initialDeposit) {
        super(customerId, "SAVINGS", initialDeposit);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        if (balance - amount < 1000) {
            System.out.println("Minimum balance 1000 required in Savings!");
            return false;
        }
        deduct(amount);
        addTransaction(new Transaction(
                0, "WITHDRAW", amount, -1, -1, java.time.LocalDateTime.now()
        ));
        return true;
    }

    @Override
    public double calculateInterest() {
        return balance * (INTEREST_RATE / 12); // monthly
    }
}