package application.view;

import application.service.outputs.SavingsAccountService;
import application.util.FormValidationUtil;

public class SavingsAccountView {

    private final SavingsAccountService service;

    public SavingsAccountView(SavingsAccountService service) {
        this.service = service;
    }


    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- MENÚ CUENTA DE AHORROS ---");
            System.out.println("1. Retirar dinero");
            System.out.println("2. Aplicar intereses");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> withdraw();
                case 2 -> applyInterest();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }

    private void withdraw() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto a retirar: ");
        service.withdraw(accountNumber, amount);
    }

    private void applyInterest() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        service.applyInterest(accountNumber);
    }
}
