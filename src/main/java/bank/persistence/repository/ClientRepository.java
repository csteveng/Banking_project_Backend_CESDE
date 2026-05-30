package bank.persistence.repository;

import bank.domain.Client;

import java.util.*;

public class ClientRepository  {

    private final Map<Integer, Client> clients = new HashMap<>();

    // --- CONSTRUCTOR PARA QUEMAR DATOS DE PRUEBA (SEEDING) ---
    public ClientRepository() {
        // Cliente 1: Usuario normal y activo
        Client client1 = new Client(1, "10203040", "Ana Maria Garcia", "3001234567", "anagarcia", "secreta123", 0, false);

        // Cliente 2: Usuario bloqueado por exceder intentos
        Client client2 = new Client(2, "98765432", "Carlos Lopez", "3109876543", "carlopez", "clave123", 3, true);

        // Cliente 3: Usuario a punto de ser bloqueado (2 intentos fallidos)
        Client client3 = new Client(3, "11223344", "Diana Perez", "3201122334", "dperez", "pass321", 2, false);

        // Agregamos los clientes al HashMap simulando la base de datos
        clients.put(client1.getId(), client1);
        clients.put(client2.getId(), client2);
        clients.put(client3.getId(), client3);

        System.out.println("[INFO] Base de datos de clientes inicializada con 3 usuarios de prueba.");
    }
    // ---------------------------------------------------------


    public Optional<Client> findByUserName(String userName) {
        return clients.values().stream()
                .filter(c -> c.getUserName().equals(userName))
                .findFirst();
    }


    public Optional<Client> findById(int id) {
        return Optional.ofNullable(clients.get(id));
    }


    public List<Client> findAll() {
        return new ArrayList<>(clients.values());
    }


    public void save(Client client) {
        // Auto-generar ID si viene en 0
        if (client.getId() <= 0) {
            int newId = clients.size() + 1;
            client.setId(newId);
        }

        if(clients.containsKey(client.getId())) {
            throw new IllegalArgumentException("Client with id " + client.getId() + " already exists");
        }
        clients.put(client.getId(), client);
    }


    public void update(Client client) {
        if (!clients.containsKey(client.getId())) {
            throw new IllegalArgumentException("Error al actualizar: El cliente con ID " + client.getId() + " no existe.");
        }
        clients.put(client.getId(), client);
    }
}