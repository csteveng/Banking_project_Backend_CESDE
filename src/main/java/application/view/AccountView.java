package application.view;

import application.domain.Account;
import application.util.FormValidationUtil;

public class AccountView {

    public void showAccountDetails(Account account) {
        if (account == null) {
            System.out.println("No se encontró la cuenta.");
            return;
        }

        System.out.println("\n--- DETALLES DE LA CUENTA ---");
        System.out.println("Número de cuenta: " + account.getAccountNumber());
        System.out.println("Saldo: $" + account.getBalance());
        System.out.println("Fecha de apertura: " + account.getDateOpened());
        System.out.println("Estado: " + account.getAccountState());
        System.out.println("Tipo de cuenta: " + account.getAccountType());
        System.out.println("Transacciones: ");
        account.getTransactions().forEach(System.out::println);
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- MENÚ GENERAL DE CUENTAS ---");
            System.out.println("1. Consultar detalles de una cuenta");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> consultAccount();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }

    private void consultAccount() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");

        System.out.println("Consulta de cuenta " + accountNumber + " (usar servicio específico).");
    }
}

