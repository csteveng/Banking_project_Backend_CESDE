package application.userinterface;

import application.service.ClientServiceImpl;
import application.service.CreditCardServiceImpl;
import application.service.outputs.CheckingAccountService;
import application.service.outputs.SavingsAccountService;
import application.service.CreditCardServiceImpl;
import application.util.FormValidationUtil;
import application.view.CheckingAccountView;
import application.view.SavingsAccountView;

public class MainMenuView {
    private final CheckingAccountView checkingAccountView;
    private final SavingsAccountView savingsAccountView;
    private final MenuCreditCard menuCreditCard;

    public MainMenuView(CheckingAccountService checkingService,
                        SavingsAccountService savingsService,
                        CreditCardServiceImpl creditCardService, ClientServiceImpl clientService) {
        this.checkingAccountView = new CheckingAccountView(checkingService);
        this.savingsAccountView = new SavingsAccountView(savingsService);
        this.menuCreditCard = new MenuCreditCard(clientService);
    }


    public void showMenu() {
        int option;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL HAPIBANK ===");
            System.out.println("1. Cuenta Corriente");
            System.out.println("2. Cuenta de Ahorros");
            System.out.println("3. Tarjeta de Crédito");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> checkingAccountView.showMenu();
                case 2 -> savingsAccountView.showMenu();
                case 3 -> menuCreditCard.showMenu(); //
                case 0 -> System.out.println("Gracias por usar el sistema bancario CESDE. ¡Hasta pronto!");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }
}
