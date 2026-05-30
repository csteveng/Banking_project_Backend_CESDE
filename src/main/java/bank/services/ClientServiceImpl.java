package bank.services;

import bank.domain.Client;
import bank.services.inputs.IAuthenticable;
import bank.services.inputs.IClientManagement;
import bank.services.outputport.IClientRepository;

public class ClientServiceImpl implements IAuthenticable, IClientManagement {

    private final IClientRepository clientRepository;

    // ELIMINAMOS 'private Client currentClient;' - El servicio ya no guarda estado

    public ClientServiceImpl(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void registerClient(Client newClient) {
        // (Tus validaciones están perfectas, las dejamos igual)
        if (newClient.getFullName() == null || newClient.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        }
        if (newClient.getUserName() == null || newClient.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (newClient.getPassword() == null || newClient.getPassword().length() < 6) {
            throw new IllegalArgumentException("Por seguridad, la contraseña debe tener al menos 6 caracteres.");
        }
        if (newClient.getIdentification() == null || !newClient.getIdentification().matches("\\d+")) {
            throw new IllegalArgumentException("La identificación (cédula) debe contener únicamente números.");
        }
        if (newClient.getCellPhone() == null || !newClient.getCellPhone().matches("\\d+")) {
            throw new IllegalArgumentException("El número de celular debe contener únicamente números.");
        }
        if (clientRepository.findByUserName(newClient.getUserName()).isPresent()) {
            throw new IllegalStateException("El nombre de usuario ya está en uso.");
        }

        newClient.setFailedIntents(0);
        newClient.setBlocked(false);
        newClient.setAuthenticated(false);

        clientRepository.save(newClient);
    }

    @Override
    public boolean authenticate(String username, String password) {
        Client client = clientRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: "  + username));

        if(client.isBlocked()) {
            throw new IllegalStateException("Account is blocked. Contact support.");
        }

        if(client.getPassword().equals(password)) {
            client.setFailedIntents(0);
            clientRepository.update(client);
            return true;
        } else {
            int intents = client.getFailedIntents() + 1;
            client.setFailedIntents(intents);

            if(intents >= Client.MAX_USER_INTENTS) {
                client.setBlocked(true);
            }
            clientRepository.update(client);
            return false;
        }
    }

    // MODIFICADO: Ahora retorna el Client en lugar de guardarlo internamente
    @Override
    public Client logIn(String username, String password) {
        if(!authenticate(username, password)) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        // Ya sabemos que existe porque authenticate pasó
        Client client = clientRepository.findByUserName(username).get();
        client.setAuthenticated(true);

        // NO llamamos a clientRepository.update() aquí.
        return client; // Retornamos el cliente para que el Controlador (Adapter) maneje la sesión
    }

    // ELIMINADO: El método logOut() ya no tiene sentido en el servicio.
    // Cerrar sesión es ahora responsabilidad exclusiva del Adaptador (ej. borrar la cookie/token)
    // Puedes borrar este método de la interfaz IAuthenticable.

    // MODIFICADO: Ahora debe recibir el username o id de quien quiere cambiar la clave
    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {

        Client client = clientRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!client.getPassword().equals(oldPassword)){
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 3) {
            throw new IllegalArgumentException("New password must be at least 3 characters long.");
        }

        client.setPassword(newPassword);

        // Aquí SÍ hacemos update porque el password es un dato que persiste en base de datos
        clientRepository.update(client);
    }

    @Override
    public Client getClient(int clientId) {
        // Usa el repositorio de clientes para buscar por su ID numérico de la BD
        // Si tu repositorio usa findById, lo llamamos así:
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con el ID: " + clientId));
    }
}
