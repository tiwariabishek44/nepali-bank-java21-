package com.taskforge.bank.util;

import com.taskforge.bank.model.*;

import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;

public final class JsonBankMapper {
    private JsonBankMapper() {}

    public static String toJson(List<Customer> customers) {
        if (customers.isEmpty()) return "{\"customers\":[]}";

        String encoded = Base64.getEncoder().encodeToString(
                customers.stream()
                        .map(c -> String.format(
                                "%d:%s:%s:%s:%s",
                                c.getCustomerId(),
                                c.getName(),
                                c.getPhone(),
                                c.getPin(),
                                c.getAccounts().stream()
                                        .map(a -> a.getAccountId() + "|" + a.getAccountType() + "|" + a.getBalance())
                                        .collect(Collectors.joining(";"))
                        ))
                        .collect(Collectors.joining("#"))
                        .getBytes()
        );
        return "{\"data\":\"" + encoded + "\"}";
    }

    public static List<Customer> fromJson(String json) {
        if (json == null || json.trim().isEmpty() || json.equals("{\"customers\":[]}"))
            return List.of();

        try {
            String encoded = json.contains("\"data\"")
                    ? json.split("\"data\":\"")[1].split("\"")[0]
                    : json;
            String decoded = new String(Base64.getDecoder().decode(encoded));
            return java.util.Arrays.stream(decoded.split("#"))
                    .map(part -> {
                        String[] fields = part.split(":", 5);
                        long id = Long.parseLong(fields[0]);
                        Customer c = new Customer(id, fields[1], fields[2], fields[3]);
                        if (fields.length > 4 && !fields[4].isBlank()) {
                            for (String accStr : fields[4].split(";")) {
                                String[] acc = accStr.split("\\|");
                                long accId = Long.parseLong(acc[0]);
                                String type = acc[1];
                                double balance = Double.parseDouble(acc[2]);

                                Account account = type.equals("SAVINGS")
                                        ? new SavingsAccount(id, 0)
                                        : new CurrentAccount(id, 0);
                                // Hack balance (in real app use reflection or proper serialization)
                                java.lang.reflect.Field balanceField = Account.class.getDeclaredField("balance");
                                balanceField.setAccessible(true);
                                balanceField.set(account, balance);
                                // Also set accountId
                                java.lang.reflect.Field idField = Account.class.getDeclaredField("accountId");
                                idField.setAccessible(true);
                                idField.set(account, accId);

                                c.addAccount(account);
                            }
                        }
                        return c;
                    })
                    .toList();
        } catch (Exception e) {
            System.out.println("Corrupted bank data! Starting fresh king 👑");
            return List.of();
        }
    }
}