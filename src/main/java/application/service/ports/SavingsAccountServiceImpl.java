package application.service.ports;

import application.domain.SavingsAccount;
import application.domain.Transaction;
import application.domain.enums.AccountState;
import application.domain.enums.TransactionType;
import application.service.outputs.SavingsAccountService;

import java.util.Optional;

public class SavingsAccountServiceImpl implements SavingsAccountService {
    private final SavingsAccountRepositoryPort repository;

    public SavingsAccountServiceImpl(SavingsAccountRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        Optional<SavingsAccount> accountOpt = repository.findById(accountNumber);

        if (accountOpt.isEmpty()) {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
            return;
        }

        SavingsAccount account = accountOpt.get();

        if (account.getAccountState() != AccountState.ACTIVE) {
            System.out.println("Error: La cuenta no está activa.");
            return;
        }

        if (amount < 0) {
            System.out.println("Error: El monto a retirar debe ser mayor a cero.");
            return;
        }

        if (account.getBalance() < amount) {
            System.out.println("Error: Saldo insuficiente. Tu saldo es: $" + account.getBalance());
            return;
        }

        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);

        //creare el registro de la transaccion
        Transaction withdrawalReccord = new Transaction(
                account.getTransactions().size() + 1,
                TransactionType.WITHDRAWAL,
                amount,
                newBalance,
                "Retiro en cuenta de ahorros desde el servicio"
        );
        account.getTransactions().add(withdrawalReccord);

        repository.update(account);
        System.out.println("Retiro exitoso. Nuevo saldo: $" + newBalance);

    }

    @Override
    public void applyInterest(String accountNumber) {
        Optional<SavingsAccount> accountOpt = repository.findById(accountNumber);

        if (accountOpt.isPresent()) {
            SavingsAccount account = accountOpt.get();
            if (account.getAccountState() == AccountState.ACTIVE) {
                double interestAmount = account.getBalance() * (account.getInterestRate() / 100);
                double newBalance = account.getBalance() + interestAmount;

                account.setBalance(newBalance);

                Transaction interestReccord = new Transaction(
                        account.getTransactions().size() + 1,
                        TransactionType.DEPOSIT,
                        interestAmount,
                        newBalance,
                        "Abono por liquidación de intereses"
                );

                account.getTransactions().add(interestReccord);

                repository.update(account);
                System.out.println("Intereses aplicados a la cuenta " + accountNumber + ": $" + interestAmount);
            } else {
                System.out.println("La cuenta " + accountNumber + " no está activa para recibir intereses.");
            }
            } else {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
        }
    }

}
