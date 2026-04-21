package application.domain;

import java.time.LocalDate;
import java.util.List;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, double balance, LocalDate dateOpened, String stateAccount, String accountType, List<Transaction> transactions, double interestRate) {
        super(accountNumber, balance, dateOpened, stateAccount, accountType, transactions);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", dateOpened=" + dateOpened +
                ", accountState=" + accountState +
                ", accountType='" + accountType + '\'' +
                ", transactions=" + transactions +
                ", interestRate=" + interestRate +
                "} " + super.toString();
    }
}
