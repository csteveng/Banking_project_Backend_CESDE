package application.domain;

import java.time.LocalDate;
import java.util.List;

public class CheckingAccount extends Account {

    private double overdraftPercentage;
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, double balance, LocalDate dateOpened, String stateAccount, String accountType, List<Transaction> transactions, double overdraftPercentage, double overdraftLimit) {
        super(accountNumber, balance, dateOpened, stateAccount, accountType, transactions);
        this.overdraftPercentage = overdraftPercentage;
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftPercentage() {
        return overdraftPercentage;
    }

    public void setOverdraftPercentage(double overdraftPercentage) {
        this.overdraftPercentage = overdraftPercentage;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "overdraftPercentage=" + overdraftPercentage +
                ", overdraftLimit=" + overdraftLimit +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", dateOpened=" + dateOpened +
                ", accountState=" + accountState +
                ", accountType='" + accountType + '\'' +
                ", transactions=" + transactions +
                "} " + super.toString();
    }
}
