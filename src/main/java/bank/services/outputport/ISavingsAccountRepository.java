package bank.services.outputport;

import bank.domain.SavingsAccount;

import java.util.List;
import java.util.Optional;

public interface ISavingsAccountRepository {

    List<SavingsAccount> findByClientId(int clientId);

    void save(SavingsAccount savingsAccount);
    Optional<SavingsAccount> findById(String AccountNumber);
    List<SavingsAccount> findAll();
    void update(SavingsAccount savingsAccount);
    void delete(String accountNumber);
    SavingsAccount findByAccountNumber(String accountNumber);
}
