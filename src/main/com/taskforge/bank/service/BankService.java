package com.taskforge.bank.service;

import com.taskforge.bank.exception.*;
import com.taskforge.bank.model.*;
import com.taskforge.bank.repository.InMemoryBankRepository;

import java.time.LocalDateTime;

public class BankService {
    private final InMemoryBankRepository repo = new InMemoryBankRepository();

    public BankService() {
        repo.loadFromFile();
    }

    public long registerCustomer(String name, String phone, String pin) {
        long id = repo.nextCustomerId();
        Customer c = new Customer(id, name, phone, pin);
        repo.saveCustomer(c);
        System.out.printf("Customer %s registered! ID: %d 🔥%n", name, id);
        return id;
    }

    public boolean login(long customerId, String pin) {
        return repo.findCustomerById(customerId)
                .map(c -> c.getPin().equals(pin))
                .orElse(false);
    }

    public void createAccount(long customerId, String type) {
        Customer customer = repo.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        double initial = type.equals("SAVINGS") ? 1000 : 5000;
        Account account = type.equals("SAVINGS")
                ? new SavingsAccount(customerId, initial)
                : new CurrentAccount(customerId, initial);

        customer.addAccount(account);
        System.out.printf("%s Account created! ID: %d | Balance: %.2f 👑%n",
                type, account.getAccountId(), initial);
    }

    private Account findAccount(long customerId, long accountId) {
        return repo.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId))
                .getAccounts().stream()
                .filter(a -> a.getAccountId() == accountId)
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public void deposit(long customerId, long accountId, double amount) {
        if (amount <= 0) throw new InvalidAmountException();
        Account acc = findAccount(customerId, accountId);
        acc.deposit(amount);
        System.out.printf("Deposited %.2f → Acc %d | New Balance: %.2f 💰%n", amount, accountId, acc.getBalance());
    }

    public void withdraw(long customerId, long accountId, double amount) {
        if (amount <= 0) throw new InvalidAmountException();
        Account acc = findAccount(customerId, accountId);
        if (!acc.withdraw(amount)) {
            throw new InsufficientFundsException(acc.getBalance(), amount);
        }
        System.out.printf("Withdrawn %.2f ← Acc %d | New Balance: %.2f 💸%n", amount, accountId, acc.getBalance());
    }

    public void transfer(long fromCustomerId, long fromAccId, long toAccId, double amount) {
        if (amount <= 0) throw new InvalidAmountException();
        Account from = findAccount(fromCustomerId, fromAccId);
        Account to = findAccount(fromCustomerId, toAccId); // same customer for now

        if (!from.withdraw(amount)) {
            throw new InsufficientFundsException(from.getBalance(), amount);
        }
        to.deposit(amount);
        System.out.printf("Transferred %.2f → %d → %d | Success 👑%n", amount, fromAccId, toAccId);
    }

    public void viewAccounts(long customerId) {
        repo.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId))
                .getAccounts()
                .forEach(System.out::println);
    }

    public void viewTransactions(long customerId) {
        System.out.println("\n=== All Transaction History ===");
        repo.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId))
                .getAccounts()
                .forEach(acc -> {
                    System.out.printf("%n%s:%n", acc);
                    acc.getTransactions().forEach(t -> System.out.println("  • " + t));
                });
    }

    public void saveBankData() {
        repo.saveToFile();
    }

    public void loadBankData() {
        // already in constructor
    }
}