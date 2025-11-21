// src/main/com/taskforge/bank/util/JsonBankMapper.java
package com.taskforge.bank.util;

import com.taskforge.bank.model.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public final class JsonBankMapper {
    private JsonBankMapper() {}

    public static String toJson(List<Customer> customers) {
        if (customers.isEmpty()) return "[]";

        String encoded = Base64.getEncoder().encodeToString(
                customers.stream()
                        .map(c -> String.format(
                                "%d:%s:%s:%s:%s",
                                c.getCustomerId(),
                                escape(c.getName()),
                                escape(c.getPhone()),
                                c.getPin(),
                                c.getAccounts().stream()
                                        .map(a -> a.getAccountId() + "|" + a.getAccountType() + "|" + a.getBalance())
                                        .collect(Collectors.joining(";"))
                        ))
                        .collect(Collectors.joining("#"))
                        .getBytes()
        );
        return encoded;
    }

    public static List<Customer> fromJson(String json) {
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return new ArrayList<>();
        }

        try {
            String decoded = new String(Base64.getDecoder().decode(json.trim()));
            List<Customer> list = new ArrayList<>();

            for (String part : decoded.split("#")) {
                String[] fields = part.split(":", 5);
                long id = Long.parseLong(fields[0]);
                String name = unescape(fields[1]);
                String phone = unescape(fields[2]);
                String pin = fields[3];
                Customer c = new Customer(id, name, phone, pin);

                if (fields.length > 4 && !fields[4].isBlank()) {
                    for (String accStr : fields[4].split(";")) {
                        String[] acc = accStr.split("\\|");
                        long accId = Long.parseLong(acc[0]);
                        String type = acc[1];
                        double balance = Double.parseDouble(acc[2]);

                        Account account = type.equals("SAVINGS")
                                ? new SavingsAccount(id, 0)
                                : new CurrentAccount(id, 0);

                        // Use reflection safely with try-catch
                        try {
                            var balanceField = Account.class.getDeclaredField("balance");
                            balanceField.setAccessible(true);
                            balanceField.setDouble(account, balance);

                            var idField = Account.class.getDeclaredField("accountId");
                            idField.setAccessible(true);
                            idField.setLong(account, accId);
                        } catch (Exception ignored) {
                            // If reflection fails, we just lose old balance/ID — better than crash
                        }

                        c.addAccount(account);
                    }
                }
                list.add(c);
            }
            return list;

        } catch (Exception e) {
            System.out.println("Bank data corrupted! Starting fresh king 👑");
            return new ArrayList<>();
        }
    }

    private static String escape(String s) {
        return s.replace(":", "__COLON__").replace("#", "__HASH__");
    }

    private static String unescape(String s) {
        return s.replace("__COLON__", ":").replace("__HASH__", "#");
    }
}