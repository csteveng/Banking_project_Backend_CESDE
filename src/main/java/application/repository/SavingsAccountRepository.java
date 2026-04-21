package application.repository;

import application.domain.SavingsAccount;
import application.service.ports.SavingsAccountRepositoryPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SavingsAccountRepository implements SavingsAccountRepositoryPort {
    private final Map<String, SavingsAccount> database = new HashMap<>();

    @Override
    public void save(SavingsAccount account) {
        database.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<SavingsAccount> findById(String AccountNumber) {
        return Optional.ofNullable(database.get(AccountNumber));
    }

    @Override
    public List<SavingsAccount> findAll() {
        return List.of();
    }

    @Override
    public void update(SavingsAccount account) {
        if (database.containsKey(account.getAccountNumber())) {
            database.put(account.getAccountNumber(), account);
        }
    }

    @Override
    public void delete(SavingsAccount savingsAccount) {
        database.remove(savingsAccount.getAccountNumber());
    }
}
