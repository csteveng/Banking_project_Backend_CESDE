package bank.services.inputs;

import bank.domain.CheckingAccount;
import bank.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;


public interface CheckingAccountService {

    void createAccount(CheckingAccount account);
    List<CheckingAccount> getAllAccounts(int clientId);
    CheckingAccount findByAccountNumber(String accountNumber);

    CheckingAccount getAccountByClientId(int clientId);

    CheckingAccount getAccount(String accountNumber);

    List<CheckingAccount> findByClientId(int clientId);

    void deposit(String accountNumber, BigDecimal amount);

    void withdraw(String accountNumber, BigDecimal amount);

    List<Transaction> getTransactionsByAccount(String accountNumber);

    void transfer(String fromAccount, String toAccount, BigDecimal amount);

    int getClientIdByAccountNumber(String accountNumber);



}
