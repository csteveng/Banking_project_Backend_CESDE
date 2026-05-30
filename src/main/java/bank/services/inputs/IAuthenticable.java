package bank.services.inputs;

import bank.domain.Client;

public interface IAuthenticable {
    boolean authenticate(String username, String password);
    Client logIn(String username, String password);
    void changePassword(String username, String oldPassword, String newPassword);
}
