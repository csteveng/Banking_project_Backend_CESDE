package hapibank.domain;

import hapibank.domain.enums.CustomerState;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {

    private LocalDate registrationDate;
    private CustomerState customerState;
    private List<Account> accounts;

    public Customer(Long id, String name, String lastName, String email,
                    String password, String cellNumber, LocalDate registrationDate) {
        super(id, name, lastName, email, password, cellNumber);
        this.registrationDate = registrationDate;
        this.customerState = CustomerState.ACTIVE; // por defecto al crear
        this.accounts = new ArrayList<>();
    }

    // getters & setters
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public CustomerState getCustomerState() {
        return customerState;
    }

    public void setCustomerState(CustomerState customerState) {
        this.customerState = customerState;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    // methods
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public void block() {
        this.customerState = CustomerState.BLOCKED;
    }

    public void activate() {
        this.customerState = CustomerState.ACTIVE;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", name='" + getFullName() + '\'' +
                ", state=" + customerState +
                ", registrationDate=" + registrationDate +
                '}';
    }
}