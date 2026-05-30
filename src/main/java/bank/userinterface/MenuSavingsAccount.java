package bank.userinterface;

import bank.utils.FormValidationUtil;
import bank.view.SavingsAccountView;

public class MenuSavingsAccount {
    private final SavingsAccountView savingsAccountView;
    private int currentClientId;

    public MenuSavingsAccount(SavingsAccountView savingsAccountView) {
        this.savingsAccountView = savingsAccountView;
    }


    public void setCurrentClientId(int currentClientId) {
        this.currentClientId = currentClientId;
    }


    public void showMenu() {
        this.savingsAccountView.setCurrentClientId(this.currentClientId);
        int option;
        do {
            System.out.println("\n--- MENÚ CUENTA DE AHORROS ---");
            System.out.println("1. Crear cuenta");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Aplicar intereses");
            System.out.println("5. Consultar saldo");
            System.out.println("6. Ver movimientos");
            System.out.println("7. Transferir dinero");
            System.out.println("0. Volver al menú principal");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> savingsAccountView.createAccount();
                case 2 -> savingsAccountView.deposit();
                case 3 -> savingsAccountView.withdraw();
                case 4 -> savingsAccountView.applyInterest();
                case 5 -> savingsAccountView.showBalance();
                case 6 -> savingsAccountView.showTransactions();
                case 7 -> savingsAccountView.transfer();
                case 0 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }
}
