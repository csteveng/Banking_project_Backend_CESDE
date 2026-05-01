package application.service.outputs;

import application.domain.Client;

public interface IAuthenticable {
    boolean authenticate(String username, String password);
    void logIn(String username, String password);
    void logOut();
    void changePassword(String oldPassword, String newPassword);
    Client getCurrentClient();
}
