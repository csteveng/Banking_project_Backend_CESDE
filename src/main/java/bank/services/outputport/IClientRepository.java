package bank.services.outputport;

import bank.domain.Client;

import java.util.List;
import java.util.Optional;

public interface IClientRepository {
    Optional<Client> findByUserName(String UserName);
    Optional<Client> findById(int id);
    List<Client> findAll();
    Client save(Client client);
    Client update(Client client);
    void deleteClient(int id);
}
