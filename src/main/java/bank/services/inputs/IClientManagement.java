package bank.services.inputs;

import bank.domain.Client;

public interface IClientManagement {
    void registerClient(Client client);
    Client getClient(int clientId);
}
