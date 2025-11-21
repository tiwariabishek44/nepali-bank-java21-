package com.taskforge.bank.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final long customerId;
    private final String name;
    private final String phone;
    private final String pin;  // In real app: hashed!
    private final List<Account> accounts;

    public Customer(long customerId, String name, String phone, String pin) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.pin = pin;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public long getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getPin() { return pin; }
    public List<Account> getAccounts() { return List.copyOf(accounts); }
}