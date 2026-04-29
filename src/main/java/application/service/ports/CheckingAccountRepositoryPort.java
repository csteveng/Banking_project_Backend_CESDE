package application.service.ports;

import application.domain.CheckingAccount;
import java.util.List;


public interface CheckingAccountRepositoryPort {


    void save(CheckingAccount account);
    CheckingAccount findByAccountNumber(String accountNumber);
    List<CheckingAccount> findAll();
    void update(CheckingAccount account);
    void delete(String accountNumber);

}








