package bank.userinterface;

import bank.services.inputs.IClientManagement;
import bank.domain.Client;
import bank.services.inputs.CheckingAccountService;
import bank.services.CreditCardServiceImpl;
import bank.utils.FormValidationUtil;
import bank.view.CheckingAccountView;
import bank.view.CreditCardView;
import bank.view.SavingsAccountView;

public class MainMenuView {
    private final CheckingAccountView checkingAccountView;
    private final MenuSavingsAccount menuSavingsAccount;
    private final MenuCreditCard menuCreditCard;
    private Client loggedInClient;

    public MainMenuView(CheckingAccountService checkingService,
                        SavingsAccountView savingsAccountView,
                        CreditCardServiceImpl creditCardService,
                        IClientManagement clientService ) {
        this.checkingAccountView = new CheckingAccountView(checkingService, clientService );
        this.menuSavingsAccount = new MenuSavingsAccount(savingsAccountView);
        CreditCardView creditCardView = new CreditCardView(creditCardService);
        this.menuCreditCard = new MenuCreditCard(creditCardView);
    }

    public void showMenu(Client loggedInClient) {
        this.checkingAccountView.setLoggedInClientId(loggedInClient.getId());
        int option;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL HAPIBANK ===");
            System.out.println("Bienvenido/a, " + loggedInClient.getFullName());
            System.out.println("1. Cuenta Corriente");
            System.out.println("2. Cuenta de Ahorros");
            System.out.println("3. Tarjeta de Crédito");
            System.out.println("0. Cerrar sesión");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> checkingAccountView.showMenu();

                // 🌟 CORREGIDO: Agregadas llaves { } porque ejecuta dos instrucciones consecutivas
                case 2 -> {
                    menuSavingsAccount.setCurrentClientId(loggedInClient.getId());
                    menuSavingsAccount.showMenu();
                }

                case 3 -> {
                    this.menuCreditCard.setLoggedInClientId(loggedInClient.getId());
                    this.menuCreditCard.showMenu();
                }

                case 0 -> System.out.println("Cerrando sesión de " + loggedInClient.getUserName() + "... ¡Hasta pronto!");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }
}