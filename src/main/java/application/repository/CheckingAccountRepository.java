package application.repository;

import application.domain.CheckingAccount;
import application.repository.CheckingAccountRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class CheckingAccountRepository implements CheckingAccountRepositoryPort {

    private List<CheckingAccount> accounts = new ArrayList<>();

    @Override
    public void save(CheckingAccount account) {
        accounts.add(account);
    }

    @Override
    public CheckingAccount findByAccountNumber(String accountNumber) {
        for (CheckingAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }

    @Override
    public List<CheckingAccount> findAll() {
        return accounts;
    }

    @Override
    public void update(CheckingAccount account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber().equals(account.getAccountNumber())) {
                accounts.set(i, account);
                return;
            }
        }
    }

    @Override
    public void delete(String accountNumber) {
        CheckingAccount toRemove = null;

        for (CheckingAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                toRemove = acc;
                break;
            }
        }

        if (toRemove != null) {
            accounts.remove(toRemove);
        }
    }
}