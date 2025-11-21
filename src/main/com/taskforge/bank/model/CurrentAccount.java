package com.taskforge.bank.model;

public final class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 50000;

    public CurrentAccount(long customerId, double initialDeposit) {
        super(customerId, "CURRENT", initialDeposit);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        if (balance + OVERDRAFT_LIMIT < amount) {
            System.out.println("Overdraft limit exceeded! Max extra: 50,000");
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
        return 0; // Current accounts don't earn interest
    }
}