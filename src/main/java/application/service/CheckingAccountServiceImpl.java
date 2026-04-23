package application.service;

import application.domain.CheckingAccount;
import application.domain.Transaction;
import application.domain.enums.TransactionType;
import application.service.outputs.CheckingAccountService;
import application.service.ports.CheckingAccountRepositoryPort;

import java.util.List;

public class CheckingAccountServiceImpl implements CheckingAccountService {
    private final CheckingAccountRepositoryPort repository;

    public CheckingAccountServiceImpl(CheckingAccountRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void createAccount(CheckingAccount account) {
        repository.save(account);
    }

    @Override
    public CheckingAccount getAccount(String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }

    @Override
    public List<CheckingAccount> getAllAccounts() {
        return repository.findAll();
    }

    @Override
    public void deposit(String accountNumber, double amount) {
        CheckingAccount account = repository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            account.getTransactions().add(new Transaction(
                    account.getTransactions().size() + 1,
                    TransactionType.DEPOSIT,
                    amount,
                    account.getBalance(),
                    "Depósito realizado"
            ));
            repository.update(account);
            System.out.println("\n✅ Depósito realizado con éxito");
            System.out.printf("Nuevo saldo de la cuenta %s: $%.2f%n", accountNumber, account.getBalance());
        } else {
            System.out.println("⚠️ La cuenta no existe.");
        }
    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        CheckingAccount account = repository.findByAccountNumber(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            account.getTransactions().add(new Transaction(
                    account.getTransactions().size() + 1,
                    TransactionType.WITHDRAWAL,
                    amount,
                    account.getBalance(),
                    "Retiro realizado"
            ));
            repository.update(account);
            System.out.println("\n✅ Retiro realizado con éxito");
            System.out.printf("Nuevo saldo de la cuenta %s: $%.2f%n", accountNumber, account.getBalance());
        } else {
            System.out.println("⚠️ Fondos insuficientes o cuenta inexistente.");
        }
    }

    @Override
    public void transfer(String fromAccount, String toAccount, double amount) {
        CheckingAccount origin = repository.findByAccountNumber(fromAccount);
        CheckingAccount destination = repository.findByAccountNumber(toAccount);

        if (origin == null) {
            System.out.println("⚠️ La cuenta origen no existe.");
            return;
        }
        if (destination == null) {
            System.out.println("⚠️ La cuenta destino no existe.");
            return;
        }
        if (origin.getBalance() < amount) {
            System.out.println("⚠️ Fondos insuficientes en la cuenta origen.");
            return;
        }

        origin.setBalance(origin.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        origin.getTransactions().add(new Transaction(
                origin.getTransactions().size() + 1,
                TransactionType.TRANSFER_IN,
                amount,
                origin.getBalance(),
                "Transferencia a cuenta " + toAccount
        ));

        destination.getTransactions().add(new Transaction(
                destination.getTransactions().size() + 1,
                TransactionType.DEPOSIT,
                amount,
                destination.getBalance(),
                "Transferencia recibida de cuenta " + fromAccount
        ));

        repository.update(origin);
        repository.update(destination);

        System.out.println("\n💸 Transferencia realizada con éxito");
        System.out.printf("Saldo cuenta origen (%s): $%.2f%n", fromAccount, origin.getBalance());
        System.out.printf("Saldo cuenta destino (%s): $%.2f%n", toAccount, destination.getBalance());
    }
}
