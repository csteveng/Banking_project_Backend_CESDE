package application.domain;

import application.domain.enums.AccountState;

import java.time.LocalDate;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected LocalDate dateOpened;
    protected AccountState accountState;
    protected String accountType;
    protected List<Transaction> transactions;

    public Account(String accountNumber, double balance, LocalDate dateOpened, String stateAccount, String accountType, List<Transaction> transactions) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.dateOpened = LocalDate.now();
        this.accountState = AccountState.ACTIVE;
        this.accountType = accountType;
        this.transactions = transactions;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", dateOpened=" + dateOpened +
                ", accountState=" + accountState +
                ", accountType='" + accountType + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}
