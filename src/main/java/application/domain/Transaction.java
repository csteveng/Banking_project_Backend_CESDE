package application.domain;

import application.domain.enums.TransactionType;

import java.time.LocalDateTime;

public class Transaction {

    private int id;
    private LocalDateTime timestamp;
    private TransactionType transactionType;
    private double amount;
    private double balanceAfterTransaction;
    private String description;

    public Transaction(int id, TransactionType transactionType, double amount, double balanceAfterTransaction, String description) {
        this.id = id;
        this.timestamp = LocalDateTime.now();
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", balanceAfterTransaction=" + balanceAfterTransaction +
                ", description='" + description + '\'' +
                '}';
    }
}
