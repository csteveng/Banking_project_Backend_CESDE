package application.view;

import application.domain.Client;
import application.service.ClientServiceImpl;
import application.service.outputs.IAuthenticable;
import application.service.outputs.IClientManagement;
import application.util.FormValidationUtil;

public class ClientView {

    // Dependemos de abstracciones, no de implementaciones
    private final IAuthenticable authService;
    private final IClientManagement clientManagement;

    // Pedimos ambos contratos
    public ClientView(IAuthenticable authService, IClientManagement clientManagement) {
        this.authService = authService;
        this.clientManagement = clientManagement;
    }

    // --- FORMULARIO OPCIÓN 1: INICIAR SESIÓN ---
    // Retorna true si el login fue exitoso, false si falló.
    // Esto le sirve al HomeMenu para saber si debe mostrar el siguiente menú.
    public boolean handleLogin() {
        System.out.println("\n--- INICIO DE SESIÓN ---");

        String username = FormValidationUtil.validateString("Ingrese su usuario: ");
        String password = FormValidationUtil.validateString("Ingrese su contraseña: ");

        try {
            // El servicio lanza una excepción si falla (ej. cuenta bloqueada, clave incorrecta)
            authService.logIn(username, password);

            // Si llegamos a esta línea, es porque no hubo errores en logIn()
            System.out.println("¡Inicio de sesión exitoso! Bienvenido de nuevo, "
                    + authService.getCurrentClient().getFullName());
            return true;

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Atrapamos el error exacto y se lo mostramos al usuario de forma amigable
            System.out.println("Error de acceso: " + e.getMessage());
            return false;
        }
    }

    // --- FORMULARIO OPCIÓN 2: CREAR USUARIO ---
    public void handleRegister() {
        System.out.println("\n--- FORMULARIO DE REGISTRO DE CLIENTE ---");

        String identification = FormValidationUtil.validateString("Ingrese número de identificación (Cédula): ");
        String fullName = FormValidationUtil.validateString("Ingrese nombre completo: ");
        String cellPhone = FormValidationUtil.validateString("Ingrese número de celular: ");
        String userName = FormValidationUtil.validateString("Cree un nombre de usuario: ");
        String password = FormValidationUtil.validateString("Cree una contraseña (mínimo 6 caracteres): ");

        // El ID '0' indica que es un usuario nuevo para que el repositorio le asigne uno
        Client newClient = new Client(0, identification, fullName, cellPhone, userName, password, 0, false);

        try {
            clientManagement.registerClient(newClient);
            System.out.println("¡Registro completado con éxito! Ya puede iniciar sesión.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error en el registro: " + e.getMessage());
        }
    }
}