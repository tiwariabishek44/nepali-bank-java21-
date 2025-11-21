package com.taskforge.bank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(
        long id,
        String type,            // DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
        double amount,
        long fromAccountId,     // -1 if not applicable
        long toAccountId,       // -1 if not applicable
        LocalDateTime timestamp
) {
    private static final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String toString() {
        String base = String.format("%s | %.2f NPR | %s",
                dtf.format(timestamp), amount, type);

        return switch (type) {
            case "TRANSFER_OUT" -> base + " → Acc " + toAccountId;
            case "TRANSFER_IN"  -> base + " ← Acc " + fromAccountId;
            default             -> base;
        };
    }
}