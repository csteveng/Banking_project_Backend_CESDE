package application.service.outputs;

import application.domain.CheckingAccount;
import application.outputs.CheckingAccountService;
import application.repositoryPort.CheckingAccountRepositoryPort;

import java.util.List;

public class CheckingAccountServiceImpl implements CheckingAccountService {

    private final CheckingAccountRepositoryPort repository;

    public CheckingAccountServiceImpl(CheckingAccountRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void createAccount(CheckingAccount account) {

        if (account == null) {
            throw new RuntimeException("Cuenta inválida");
        }

        double limit = account.getBalance() * account.getOverdraftPercentage();
        account.setOverdraftLimit(limit);

        repository.save(account);
    }

    @Override
    public CheckingAccount getAccount(String accountNumber) {

        CheckingAccount account = repository.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new RuntimeException("Cuenta no encontrada");
        }

        return account;
    }

    @Override
    public List<CheckingAccount> getAllAccounts() {
        return repository.findAll();
    }

    @Override
    public void deposit(String accountNumber, double amount) {

        CheckingAccount account = repository.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new RuntimeException("Cuenta no encontrada");
        }

        if (amount <= 0) {
            throw new RuntimeException("Monto inválido");
        }

        account.setBalance(account.getBalance() + amount);

        repository.update(account);
    }

    @Override
    public void withdraw(String accountNumber, double amount) {

        CheckingAccount account = repository.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new RuntimeException("Cuenta no encontrada");
        }

        if (amount <= 0) {
            throw new RuntimeException("Monto inválido");
        }

        double available = account.getBalance() + account.getOverdraftLimit();

        if (amount <= available) {
            account.setBalance(account.getBalance() - amount);
        } else {
            throw new RuntimeException("Fondos insuficientes");
        }

        repository.update(account);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, double amount) {

        CheckingAccount origin = repository.findByAccountNumber(fromAccount);
        CheckingAccount destination = repository.findByAccountNumber(toAccount);

        if (origin == null || destination == null) {
            throw new RuntimeException("Cuenta inválida");
        }

        if (amount <= 0) {
            throw new RuntimeException("Monto inválido");
        }

        double available = origin.getBalance() + origin.getOverdraftLimit();

        if (amount <= available) {
            origin.setBalance(origin.getBalance() - amount);
            destination.setBalance(destination.getBalance() + amount);
        } else {
            throw new RuntimeException("Fondos insuficientes");
        }

        repository.update(origin);
        repository.update(destination);
    }
}