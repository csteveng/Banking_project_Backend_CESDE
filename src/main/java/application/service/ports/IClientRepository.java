package application.service.ports;

import application.domain.Client;

import java.util.List;
import java.util.Optional;

public interface IClientRepository {
    Optional<Client> findByUserName(String UserName);
    Optional<Client> findById(int id);
    List<Client> findAll();
    void save(Client client);
    void update(Client client);
}
