package com.taskforge.bank;

import com.taskforge.bank.util.ConsoleUtil;

public class Main {
    public static void main(String[] args) {
        ConsoleUtil.clearScreen();
        System.out.println("""
            ╔══════════════════════════════════════════════════╗
            ║                                                  ║
            ║         TASKFORGE BANK – NEPAL EDITION           ║
            ║              Java 21 OOP Beast Mode              ║
            ║                                                  ║
            ╚══════════════════════════════════════════════════╝
            """);

        BankApp app = new BankApp();
        app.start();
    }
}