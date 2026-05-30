package bank.services.inputs;

public interface TransactionService {
    // El caso de uso principal: transferir dinero de una cuenta a otra
    void transfer(String originAccountNumber, String destinationAccountNumber, double amount);
}