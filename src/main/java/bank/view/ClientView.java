package bank.view;

import bank.domain.Client;
import bank.services.inputs.IAuthenticable;
import bank.services.inputs.IClientManagement;
import bank.utils.FormValidationUtil;

public class ClientView {

    private final IAuthenticable authService;
    private final IClientManagement clientManagement;

    public ClientView(IAuthenticable authService, IClientManagement clientManagement) {
        this.authService = authService;
        this.clientManagement = clientManagement;
    }

    // --- FORMULARIO OPCIÓN 1: INICIAR SESIÓN ---
    // AHORA RETORNA: El objeto Client si fue exitoso, o null si falló.
    public Client handleLogin() {
        System.out.println("\n--- INICIO DE SESIÓN ---");

        String username = FormValidationUtil.validateString("Ingrese su usuario: ");
        String password = FormValidationUtil.validateString("Ingrese su contraseña: ");

        try {
            // El servicio ahora retorna el cliente recién validado
            Client loggedInClient = authService.logIn(username, password);

            // Usamos ese mismo objeto para saludarlo
            System.out.println("¡Inicio de sesión exitoso! Bienvenido de nuevo, "
                    + loggedInClient.getFullName());

            // Retornamos el cliente al HomeMenu
            return loggedInClient;

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error de acceso: " + e.getMessage());
            // Si hay error, devolvemos null para que el HomeMenu sepa que falló
            return null;
        }
    }

    // --- FORMULARIO OPCIÓN 2: CREAR USUARIO ---
    // (Este queda exactamente igual)
    public void handleRegister() {
        System.out.println("\n--- FORMULARIO DE REGISTRO DE CLIENTE ---");

        String identification = FormValidationUtil.validateString("Ingrese número de identificación (Cédula): ");
        String fullName = FormValidationUtil.validateString("Ingrese nombre completo: ");
        String cellPhone = FormValidationUtil.validateString("Ingrese número de celular: ");
        String userName = FormValidationUtil.validateString("Cree un nombre de usuario: ");
        String password = FormValidationUtil.validateString("Cree una contraseña (mínimo 6 caracteres): ");

        Client newClient = new Client(0, identification, fullName, cellPhone, userName, password, 0, false);

        try {
            clientManagement.registerClient(newClient);
            System.out.println("¡Registro completado con éxito! Ya puede iniciar sesión.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error en el registro: " + e.getMessage());
        }
    }
}