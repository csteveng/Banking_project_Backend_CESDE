package application.repository;

import application.domain.Client;
import application.service.ports.IClientRepository;

import java.util.*;

public class ClientRepository implements IClientRepository {

    private final Map<Integer, Client> clients = new HashMap<>();

    @Override
    public Optional<Client> findByUserName(String userName) {
        return clients.values().stream()
                .filter(c -> c.getUserName().equals(userName))
                .findFirst();
    }

    @Override
    public Optional<Client> findById(int id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public void save(Client client) {
        if(clients.containsKey(client.getId())) {
            throw new IllegalArgumentException("Client with id " + client.getId() + " already exists");
        }
        clients.put(client.getId(), client);
    }

    @Override
    public void update(Client client) {

    }
}
