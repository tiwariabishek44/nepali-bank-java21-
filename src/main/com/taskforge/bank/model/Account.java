package com.taskforge.bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Account {
    private static final AtomicLong idGenerator = new AtomicLong(100000);

    protected final long accountId;
    protected final long customerId;
    protected final String accountType;  // SAVINGS or CURRENT
    protected double balance;
    protected final List<Transaction> transactions;

    protected Account(long customerId, String accountType, double initialDeposit) {
        this.accountId = idGenerator.getAndIncrement();
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.transactions = new ArrayList<>();
        addTransaction(new Transaction(
                0, "DEPOSIT", initialDeposit, -1, -1, java.time.LocalDateTime.now()
        ));
    }

    // Abstract methods — forced implementation in children
    public abstract boolean withdraw(double amount);
    public abstract double calculateInterest();  // monthly interest

    // Common methods
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
        addTransaction(new Transaction(
                0, "DE onPOSIT", amount, -1, -1, java.time.LocalDateTime.now()
        ));
    }

    protected void addTransaction(Transaction tx) {
        transactions.add(tx);
    }

    public long getAccountId() { return accountId; }
    public long getCustomerId() { return customerId; }
    public String getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return Collections.unmodifiableList(transactions); }

    protected void deduct(double amount) {
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return String.format("%s Account | ID: %d | Balance: %.2f NPR",
                accountType, accountId, balance);
    }
}