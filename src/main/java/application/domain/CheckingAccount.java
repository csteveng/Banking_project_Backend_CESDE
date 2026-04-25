package application.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckingAccount extends Account {
    private double overdraftPercentage;
    private double overdraftLimit;
    private List<Transaction> transactions = new ArrayList<>();

    public CheckingAccount(String accountNumber, double balance, LocalDate dateOpened,
                           String stateAccount, String accountType, List<Transaction> transactions,
                           double overdraftPercentage, double overdraftLimit) {
        super(accountNumber, balance, dateOpened, stateAccount, accountType, transactions);
        this.overdraftPercentage = overdraftPercentage;
        this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount(String accountNumber, double initialBalance,
                           double overdraftLimit, double overdraftPercentage) {
        super(accountNumber, initialBalance, LocalDate.now(), "ACTIVE", "CHECKING", new ArrayList<>());
        this.overdraftLimit = overdraftLimit;
        this.overdraftPercentage = overdraftPercentage;
    }

    public double getOverdraftPercentage() { return overdraftPercentage; }
    public void setOverdraftPercentage(double overdraftPercentage) { this.overdraftPercentage = overdraftPercentage; }

    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    public List<Transaction> getTransactions() { return transactions; }
}
