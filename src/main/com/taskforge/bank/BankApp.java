package com.taskforge.bank;

import com.taskforge.bank.service.BankService;
import com.taskforge.bank.util.ConsoleUtil;

public class BankApp {
    private final BankService bankService;
    private long currentCustomerId = -1;

    public BankApp() {
        this.bankService = new BankService();
        bankService.loadBankData(); // Load from file on startup
    }

    public void start() {
        while (true) {
            if (currentCustomerId == -1) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        ConsoleUtil.clearScreen();
        System.out.println("=== TASKFORGE BANK – Login / Register 👑 ===");
        System.out.println("1 → Register New Customer");
        System.out.println("2 → Login (by Customer ID)");
        System.out.println("3 → Exit");
        System.out.print("Choose: ");

        int choice = ConsoleUtil.readInt("");
        switch (choice) {
            case 1 -> registerCustomer();
            case 2 -> loginCustomer();
            case 3 -> {
                bankService.saveBankData();
                System.out.println("Thank you for using TaskForge Bank 👑🔥 See you!");
                System.exit(0);
            }
            default -> System.out.println("Invalid choice bro!");
        }
        ConsoleUtil.pressEnterToContinue();
    }

    private void registerCustomer() {
        String name = ConsoleUtil.readLine("Enter your name: ");
        String phone = ConsoleUtil.readLine("Enter phone: ");
        String pin = ConsoleUtil.readLine("Set 4-digit PIN: ");

        if (pin.length() != 4 || !pin.matches("\\d{4}")) {
            System.out.println("PIN must be exactly 4 digits king!");
            return;
        }

        long id = bankService.registerCustomer(name, phone, pin);
        System.out.printf("Registered Successfully! Your Customer ID: %d 🔥%n", id);
    }

    private void loginCustomer() {
        long id = ConsoleUtil.readInt("Enter Customer ID: ");
        String pin = ConsoleUtil.readLine("Enter PIN: ");

        if (bankService.login(id, pin)) {
            currentCustomerId = id;
            System.out.println("Login Successful 👑 Welcome back!");
        } else {
            System.out.println("Wrong ID or PIN bro! Try again.");
        }
    }

    private void showMainMenu() {
        ConsoleUtil.clearScreen();
        System.out.printf("=== Welcome Customer %d | TaskForge Bank 👑 ===%n", currentCustomerId);
        System.out.println("1 → Create Savings Account");
        System.out.println("2 → Create Current Account");
        System.out.println("3 → Deposit Money");
        System.out.println("4 → Withdraw Money");
        System.out.println("5 → Transfer Money");
        System.out.println("6 → View Accounts");
        System.out.println("7 → Transaction History");
        System.out.println("8 → Logout");
        System.out.print("Choose: ");

        int choice = ConsoleUtil.readInt("");
        switch (choice) {
            case 1 -> createAccount("SAVINGS");
            case 2 -> createAccount("CURRENT");
            case 3 -> deposit();
            case 4 -> withdraw();
            case 5 -> transfer();
            case 6 -> viewAccounts();
            case 7 -> viewTransactions();
            case 8 -> {
                currentCustomerId = -1;
                System.out.println("Logged out safely king!");
            }
            default -> System.out.println("Invalid option bro!");
        }
        if (choice != 8) ConsoleUtil.pressEnterToContinue();
    }

    // These will be fully implemented after we drop the model + service
    private void createAccount(String type) {
        bankService.createAccount(currentCustomerId, type);
    }

    private void deposit() {
        long accId = ConsoleUtil.readInt("Enter Account ID: ");
        double amount = ConsoleUtil.readDouble("Enter amount to deposit: ");
        bankService.deposit(currentCustomerId, accId, amount);
    }

    private void withdraw() {
        long accId = ConsoleUtil.readInt("Enter Account ID: ");
        double amount = ConsoleUtil.readDouble("Enter amount to withdraw: ");
        bankService.withdraw(currentCustomerId, accId, amount);
    }

    private void transfer() {
        long fromId = ConsoleUtil.readInt("From Account ID: ");
        long toId = ConsoleUtil.readInt("To Account ID: ");
        double amount = ConsoleUtil.readDouble("Transfer amount: ");
        bankService.transfer(currentCustomerId, fromId, toId, amount);
    }

    private void viewAccounts() {
        System.out.println("\n=== Your Accounts ===");
        bankService.viewAccounts(currentCustomerId);
    }

    private void viewTransactions() {
        bankService.viewTransactions(currentCustomerId);
    }
}