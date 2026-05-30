package bank.services.outputport;

import bank.domain.Transaction;
import java.util.List;
import java.util.Optional;

public interface ITransactionRepository {

    Transaction save(Transaction transaction);
    Optional<Transaction> findById(int id);
    List<Transaction> findAll();
    List<Transaction> findByAccountNumber(String accountNumber);
}