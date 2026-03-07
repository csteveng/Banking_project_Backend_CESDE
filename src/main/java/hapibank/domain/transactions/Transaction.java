package hapibank.domain.transactions;

import hapibank.domain.Customer;
import hapibank.domain.enums.TransactionState;

import java.time.LocalDate;

public class Transaction {
    private long id;
    private double amount;
    private LocalDate date;
    private TransactionState state;

    public Transaction(long id, double amount, LocalDate date, TransactionState state) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }
}
