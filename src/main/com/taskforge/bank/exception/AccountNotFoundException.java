package com.taskforge.bank.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(long accountId) {
        super("Account with ID " + accountId + " not found bro! 🔥");
    }
}