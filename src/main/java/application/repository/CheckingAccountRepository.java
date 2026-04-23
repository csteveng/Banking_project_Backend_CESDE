package application.repository;

import application.domain.CheckingAccount;
import application.service.ports.CheckingAccountRepositoryPort;

import java.util.HashMap;
import java.util.Map;

public class CheckingAccountRepository implements CheckingAccountRepositoryPort {
    private final Map<String, CheckingAccount> database = new HashMap<>();

    @Override
    public CheckingAccount findByAccountNumber(String accountNumber) {
        return database.get(accountNumber);
    }

    @Override
    public void update(CheckingAccount account) {
        database.put(account.getAccountNumber(), account);
    }

    @Override
    public void delete(String accountNumber) {

    }

    @Override
    public void save(CheckingAccount account) {
        database.put(account.getAccountNumber(), account);
    }

    @Override
    public java.util.List<CheckingAccount> findAll() {
        return new java.util.ArrayList<>(database.values());
    }
}
