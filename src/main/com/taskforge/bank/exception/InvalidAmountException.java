package com.taskforge.bank.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) {
        super("Invalid amount: " + message + " 🚫");
    }

    public InvalidAmountException() {
        super("Amount must be positive and greater than zero bro! 💰");
    }
}