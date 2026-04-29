package application.service.outputs;

import application.domain.CheckingAccount;
import java.util.List;


public interface CheckingAccountService {

    void createAccount(CheckingAccount account);

    CheckingAccount getAccount(String accountNumber);

    List<CheckingAccount> getAllAccounts();

    void deposit(String accountNumber, double amount);

    void withdraw(String accountNumber, double amount);

    void transfer(String fromAccount, String toAccount, double amount);

}
