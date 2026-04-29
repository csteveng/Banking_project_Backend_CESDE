package application.service;

import application.domain.Client;
import application.service.outputs.IAuthenticable;
import application.service.ports.IClientRepository;

public class ClientService implements IAuthenticable {
    private final IClientRepository clientRepository;
    private Client currentClient; // Is used to track the logged user

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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

    @Override
    public void logIn(String username, String password) {
        if(!authenticate(username, password)) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        Client client = clientRepository.findByUserName(username).get();
        client.setAuthenticated(true);
        clientRepository.update(client);
        this.currentClient = client;
    }

    @Override
    public void logOut() {
        if(currentClient == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        currentClient.setAuthenticated(false);
        clientRepository.update(currentClient);
        this.currentClient = null;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if (currentClient == null) {
            throw new IllegalStateException("Must be logged in to change password.");
        }
        if (!currentClient.getPassword().equals(oldPassword)){
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 3) {
            throw new IllegalArgumentException("New password must be at least 3 characters long.");
        }

        currentClient.setPassword(newPassword);
        clientRepository.update(currentClient);

    }

    public Client getCurrentClient() {
        return currentClient;
    }
}
