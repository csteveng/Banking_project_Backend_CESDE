package bank.userinterface;

import bank.domain.Client;
import bank.utils.FormValidationUtil;
import bank.view.ClientView;

public class HomeMenu {

    private final ClientView clientView;
    private final MainMenuView mainMenuView;

    public HomeMenu(ClientView clientView, MainMenuView mainMenuView) {
        this.clientView = clientView;
        this.mainMenuView = mainMenuView;
    }

    public void start() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n========================================");
            System.out.println("          BIENVENIDO A HAPIBANK         ");
            System.out.println("========================================");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear Usuario (Registrarse)");
            System.out.println("3. Salir");
            System.out.println("========================================");

            int option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1:
                    // 1. Cambiamos el retorno de boolean a Client.
                    // Si el login falla, handleLogin() debería devolver null.
                    Client loggedInClient = clientView.handleLogin();

                    // 2. Si el cliente no es nulo, el login fue exitoso
                    if (loggedInClient != null) {
                        // 3. Le pasamos el cliente al menú principal como acordamos antes
                        mainMenuView.showMenu(loggedInClient);

                        // Cuando el usuario presione 0 en mainMenuView, el código volverá aquí
                        // y el bucle while mostrará el menú de Home otra vez.
                    }
                    break;
                case 2:
                    clientView.handleRegister();
                    break;
                case 3:
                    System.out.println("Saliendo del sistema... ¡Gracias por preferir HapiBank!");
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione 1, 2 o 3.");
            }
        }
    }
}