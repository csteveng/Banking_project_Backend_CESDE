package application.service.outputs;

public interface SavingsAccountService {
    void withdraw(String accountNumber, double amount);
    void applyInterest(String accountNumber);
}
