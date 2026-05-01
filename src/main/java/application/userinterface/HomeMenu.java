package application.userinterface;

import application.util.FormValidationUtil;
import application.view.ClientView;

public class HomeMenu {

    private final ClientView clientView;
    private final MainMenuView mainMenuView; // Agregamos la dependencia del menú interno

    // Actualizamos el constructor
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
                    // Llamamos al formulario y capturamos si fue exitoso
                    boolean loginSuccess = clientView.handleLogin();

                    if (loginSuccess) {
                        // ¡AQUÍ ESTÁ LA MAGIA! Si se loguea bien, lanzamos el menú principal
                        mainMenuView.showMenu();
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