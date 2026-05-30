package bank.configuration;

import bank.services.outputport.ITransactionRepository;
import bank.view.CheckingAccountView;
import bank.persistence.repository.*;
import bank.persistence.database.DatabaseConnectionMySQL; // Importamos tu clase Singleton
import bank.services.CheckingAccountServiceImpl;
import bank.services.SavingsAccountServiceImpl;
import bank.services.CreditCardServiceImpl;
import bank.services.ClientServiceImpl;
import bank.persistence.mapper.CheckingAccountRowMapper;
import bank.persistence.mapper.SavingsAccountRowMapper;
import bank.persistence.mapper.TransactionRowMapper;
import bank.userinterface.MainMenuView;
import bank.userinterface.HomeMenu;
import bank.view.ClientView;
import bank.view.CreditCardView;
import bank.view.SavingsAccountView;
import bank.persistence.mapper.ClientRowMapper;

import java.sql.Connection;

public class AppConfig {

    public static void main(String[] args) {
        // Al arrancar, el Singleton manejará cualquier error de conexión lanzando un RuntimeException
        HomeMenu menu = createHomeMenu();
        menu.start();
    }

    public static HomeMenu createHomeMenu() {

        // 0. OBTENEMOS LA CONEXIÓN A LA BASE DE DATOS
        // Llamamos a tu Singleton para obtener la única instancia de la conexión
        Connection connection = DatabaseConnectionMySQL.getInstance().getConnection();

        // Instanciamos el Mapper que convierte ResultSet a objetos Client
        ClientRowMapper clientRowMapper = new ClientRowMapper();
        CheckingAccountRowMapper checkingRowMapper = new CheckingAccountRowMapper();
        SavingsAccountRowMapper savingsRowMapper = new SavingsAccountRowMapper();
        TransactionRowMapper transactionRowMapper= new TransactionRowMapper();


        // 1. Repositorios

        SavingsAccountRepositoryDb savingsRepo = new SavingsAccountRepositoryDb(connection, savingsRowMapper);
        CreditCardRepositoryDb creditCardRepo = new CreditCardRepositoryDb(connection);

        // Inyectamos la conexión y el mapper en el repositorio de base de datos
        ClientRepositoryDb clientRepo = new ClientRepositoryDb(connection, clientRowMapper);
        CheckingAccountRepositoryDb checkingRepo = new CheckingAccountRepositoryDb(connection, checkingRowMapper);
        ITransactionRepository transactionRepo = new TransactionRepositoryDb(connection, transactionRowMapper);



        // 2. Servicios

        CheckingAccountServiceImpl checkingService = new CheckingAccountServiceImpl(checkingRepo , transactionRepo , savingsRepo);
        SavingsAccountServiceImpl savingsService = new SavingsAccountServiceImpl(savingsRepo ,transactionRepo ,checkingRepo);
        CreditCardServiceImpl creditCardService = new CreditCardServiceImpl(creditCardRepo);
        ClientServiceImpl clientService = new ClientServiceImpl(clientRepo);

        // 3. Vistas
        ClientView clientView = new ClientView(clientService, clientService);
        SavingsAccountView savingsAccountView = new SavingsAccountView(savingsService , clientService );
        CreditCardView creditCardView = new CreditCardView(creditCardService);
        CheckingAccountView checkingAccountView = new CheckingAccountView(checkingService, clientService);

        // 4. Menús
        MainMenuView mainMenuView = new MainMenuView(
                checkingService,
                savingsAccountView,
                creditCardService,clientService

        );

        // 5. Retornamos el menú de entrada (Home)
        return new HomeMenu(clientView, mainMenuView);
    }
}