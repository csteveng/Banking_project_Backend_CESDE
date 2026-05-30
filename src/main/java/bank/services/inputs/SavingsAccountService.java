package bank.services.inputs;

import bank.domain.SavingsAccount;
import bank.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface SavingsAccountService {
    void withdraw(String accountNumber, BigDecimal amount);
    void applyInterest(String accountNumber);
    void deposit(String accountNumber, BigDecimal amount);
    SavingsAccount getAccount(String accountNumber);
    List<SavingsAccount> findByClientId(int clientId);
    List<Transaction> getTransactionsByAccount(String accountNumber);
    void transfer(String fromAccount, String toAccount, BigDecimal amount);

    // Esta es la firma que debe coincidir con tu clase:
    SavingsAccount createAccount(String accountNumber, BigDecimal initialBalance, int clientId);
}


