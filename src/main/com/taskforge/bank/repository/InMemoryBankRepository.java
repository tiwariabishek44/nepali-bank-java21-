package com.taskforge.bank.repository;

import com.taskforge.bank.model.*;
import com.taskforge.bank.util.JsonBankMapper;

import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryBankRepository {
    private final Map<Long, Customer> customers = new HashMap<>();
    private final AtomicLong customerIdGenerator = new AtomicLong(1000);
    private final Path dataFile = Path.of("data/bank-data.json");

    public long nextCustomerId() {
        return customerIdGenerator.getAndIncrement();
    }

    public void saveCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public Optional<Customer> findCustomerById(long id) {
        return Optional.ofNullable(customers.get(id));
    }

    public List<Customer> findAllCustomers() {
        return List.copyOf(customers.values());
    }

    // Persistence
    public void loadFromFile() {
        try {
            if (Files.exists(dataFile)) {
                String content = Files.readString(dataFile);
                List<Customer> loaded = JsonBankMapper.fromJson(content);
                loaded.forEach(c -> {
                    customers.put(c.getCustomerId(), c);
                    customerIdGenerator.set(Math.max(customerIdGenerator.get(), c.getCustomerId() + 1));
                });
                System.out.println("Bank data loaded: " + loaded.size() + " customers 👑");
            }
        } catch (Exception e) {
            System.out.println("Failed to load bank data — starting fresh");
        }
    }

    public void saveToFile() {
        try {
            Files.createDirectories(dataFile.getParent());
            Files.writeString(dataFile, JsonBankMapper.toJson(findAllCustomers()));
            System.out.println("Bank data saved successfully 🔥");
        } catch (Exception e) {
            System.out.println("Failed to save bank data bro!");
        }
    }
}