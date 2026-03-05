package hapibank.domain;

import hapibank.domain.enums.AccountType;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private Long id;
    private String accountNumber;
    private AccountType type;
    private double balance;
    private double interestRate;
    private double handlingFee;
    private Customer owner;

    public Account(Long id, String accountNumber, AccountType type,
                   double interestRate, double handlingFee, Customer owner) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = 0.0; // siempre inicia en 0
        this.interestRate = interestRate;
        this.handlingFee = handlingFee;
        this.owner = owner;
    }

    // getters
    public Long getId() { return id; }

    public String getAccountNumber() { return accountNumber; }

    public AccountType getType() { return type; }

    public double getBalance() { return balance; }

    public double getInterestRate() { return interestRate; }

    public double getHandlingFee() { return handlingFee; }

    public Customer getOwner() { return owner; }

    // setters — balance NO tiene setter, solo se modifica con los métodos
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setHandlingFee(double handlingFee) {
        this.handlingFee = handlingFee;
    }

    // methods
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0");
        }
        if (!hasSufficientBalance(amount)) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance -= amount;
    }

    public boolean hasSufficientBalance(double amount) {
        return this.balance >= amount;
    }

    public void applyInterest() {
        if (this.type == AccountType.SAVINGS) {
            this.balance += this.balance * this.interestRate;
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", type=" + type +
                ", balance=" + balance +
                ", owner=" + owner.getFullName() +
                '}';
    }
}