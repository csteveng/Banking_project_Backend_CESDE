package application.config;

import application.repository.CheckingAccountRepository;
import application.repository.SavingsAccountRepository;
import application.repository.CreditCardRepository;
import application.service.CheckingAccountServiceImpl;
import application.service.SavingsAccountServiceImpl;
import application.service.CreditCardServiceImpl;
import application.view.MainMenuView;

public class AppConfig {

    public static void main(String[] args) {

        MainMenuView menu = createMenuApp();
        menu.showMenu();
    }


    public static MainMenuView createMenuApp() {
        // Repositorios
        var checkingRepo = new CheckingAccountRepository();
        var savingsRepo = new SavingsAccountRepository();
        var creditCardRepo = new CreditCardRepository();

        // Servicios
        var checkingService = new CheckingAccountServiceImpl(checkingRepo);
        var savingsService = new SavingsAccountServiceImpl(savingsRepo);
        var creditCardService = new CreditCardServiceImpl(creditCardRepo);

        // Retornar menú principal ya armado
        return new MainMenuView(checkingService, savingsService, creditCardService);
    }
}
