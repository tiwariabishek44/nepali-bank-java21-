package com.taskforge.bank.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(double balance, double amount) {
        super(String.format("Insufficient funds! Balance: %.2f | Requested: %.2f 💸", balance, amount));
    }
}