package com.taskforge.bank.exception;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException() {
        super("Wrong PIN entered king! Try again 🔐");
    }
}