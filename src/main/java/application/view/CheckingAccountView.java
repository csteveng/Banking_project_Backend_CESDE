package application.view;

import application.domain.CheckingAccount;
import application.service.outputs.CheckingAccountService;
import application.util.FormValidationUtil;

public class CheckingAccountView {
    private final CheckingAccountService checkingAccountService;

    public CheckingAccountView(CheckingAccountService checkingAccountService) {
        this.checkingAccountService = checkingAccountService;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- MENÚ CUENTA CORRIENTE ---");
            System.out.println("1. Crear cuenta");
            System.out.println("2. Consultar cuenta");
            System.out.println("3. Ver todas las cuentas");
            System.out.println("4. Depositar");
            System.out.println("5. Retirar");
            System.out.println("6. Transferir");
            System.out.println("7. Ver movimientos");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> createAccount();
                case 2 -> getAccount();
                case 3 -> getAllAccounts();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transfer();
                case 7 -> showTransactions();
                case 0 -> System.out.println("👋 Saliendo...");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }

    private void createAccount() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        double initialBalance = FormValidationUtil.validateDouble("Ingrese saldo inicial: ");
        double overdraftLimit = FormValidationUtil.validateDouble("Ingrese límite de sobregiro: ");
        double overdraftPercentage = FormValidationUtil.validateDouble("Ingrese porcentaje de sobregiro: ");

        CheckingAccount newAccount = new CheckingAccount(accountNumber, initialBalance, overdraftLimit, overdraftPercentage);
        checkingAccountService.createAccount(newAccount);

        System.out.println("\n✅ Cuenta creada exitosamente: " + accountNumber);
    }

    private void getAccount() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        var account = checkingAccountService.getAccount(accountNumber);
        if (account != null) {
            printAccountDetails(account);
        } else {
            System.out.println("⚠️ No existe una cuenta con ese número.");
        }
    }

    private void getAllAccounts() {
        var accounts = checkingAccountService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("⚠️ No hay cuentas registradas.");
        } else {
            accounts.forEach(this::printAccountDetails);
        }
    }

    private void deposit() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto a depositar: ");
        checkingAccountService.deposit(accountNumber, amount);
    }

    private void withdraw() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto a retirar: ");
        checkingAccountService.withdraw(accountNumber, amount);
    }

    private void transfer() {
        String fromAccount = FormValidationUtil.validateString("Ingrese número de cuenta origen: ");
        String toAccount = FormValidationUtil.validateString("Ingrese número de cuenta destino: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto a transferir: ");
        checkingAccountService.transfer(fromAccount, toAccount, amount);
    }

    private void showTransactions() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        var account = checkingAccountService.getAccount(accountNumber);
        if (account != null) {
            if (account.getTransactions().isEmpty()) {
                System.out.println("⚠️ No hay movimientos registrados.");
            } else {
                System.out.println("\n======================================");
                System.out.println("   📜 HISTORIAL DE MOVIMIENTOS ");
                System.out.println("======================================");
                account.getTransactions().forEach(tx -> {
                    System.out.printf("[%s] %-12s | Monto: $%.2f | Saldo: $%.2f | %s%n",
                            tx.getTimestamp(),
                            tx.getTransactionType(),
                            tx.getAmount(),
                            tx.getBalanceAfterTransaction(),
                            tx.getDescription());
                });
                System.out.println("======================================\n");
            }
        } else {
            System.out.println("⚠️ La cuenta no existe.");
        }
    }

    // Método auxiliar para mostrar detalles bonitos de una cuenta
    private void printAccountDetails(CheckingAccount account) {
        System.out.println("\n======================================");
        System.out.println("   📑 DETALLES DE LA CUENTA CORRIENTE ");
        System.out.println("======================================");
        System.out.printf("Número de cuenta    : %s%n", account.getAccountNumber());
        System.out.printf("Saldo actual        : $%.2f%n", account.getBalance());
        System.out.printf("Límite sobregiro    : $%.2f%n", account.getOverdraftLimit());
        System.out.printf("Porcentaje sobregiro: %.2f%%%n", account.getOverdraftPercentage());
        System.out.println("======================================\n");
    }
}
