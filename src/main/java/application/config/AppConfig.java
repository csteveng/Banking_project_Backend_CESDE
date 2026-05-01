package application.config;

import application.repository.CheckingAccountRepository;
import application.repository.SavingsAccountRepository;
import application.repository.CreditCardRepository;
import application.repository.ClientRepository;
import application.service.CheckingAccountServiceImpl;
import application.service.SavingsAccountServiceImpl;
import application.service.CreditCardServiceImpl;
import application.service.ClientServiceImpl; // Tu clase concreta
import application.userinterface.MainMenuView;
import application.userinterface.HomeMenu;
import application.view.ClientView;

public class AppConfig {

    public static void main(String[] args) {
        // En lugar de arrancar MainMenuView directo, arrancamos el HomeMenu para exigir Login
        HomeMenu menu = createHomeMenu();
        menu.start();
    }

    public static HomeMenu createHomeMenu() {

        // 1. REPOSITORIOS (Nivel más bajo)
        CheckingAccountRepository checkingRepo = new CheckingAccountRepository();
        SavingsAccountRepository savingsRepo = new SavingsAccountRepository();
        CreditCardRepository creditCardRepo = new CreditCardRepository();
        ClientRepository clientRepo = new ClientRepository();

        // 2. SERVICIOS (Lógica de negocio)
        CheckingAccountServiceImpl checkingService = new CheckingAccountServiceImpl(checkingRepo);
        SavingsAccountServiceImpl savingsService = new SavingsAccountServiceImpl(savingsRepo);
        CreditCardServiceImpl creditCardService = new CreditCardServiceImpl(creditCardRepo);

        // Instanciamos el servicio de cliente (que implementa las 2 interfaces)
        ClientServiceImpl clientService = new ClientServiceImpl(clientRepo);

        // 3. VISTAS Y MENÚS INTERNOS
        // ¡LA MAGIA DE LISKOV! Pasamos 'clientService' en ambos parámetros.
        // El primer parámetro lo tratará como IAuthenticable y el segundo como IClientManagement
        ClientView clientView = new ClientView(clientService, clientService);

        // Ensamblamos el menú principal que se mostrará DESPUÉS de hacer login
        MainMenuView mainMenuView = new MainMenuView(
                checkingService,
                savingsService,
                creditCardService,
                clientService // Si MainMenuView necesita auth, lo recibe como IAuthenticable
        );

        // 4. RETORNAMOS EL MENÚ DE ENTRADA (Home)
        return new HomeMenu(clientView, mainMenuView);
    }
}