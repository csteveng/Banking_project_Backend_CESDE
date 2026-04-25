
package application.view;

import application.domain.CreditCard;
import application.service.outputs.CreditCardService;
import application.service.CreditCardServiceImpl;
import application.util.FormValidationUtil;

public class CreditCardView {
    private final CreditCardService creditCardService;

    public CreditCardView(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- MENÚ TARJETA DE CRÉDITO ---");
            System.out.println("1. Crear tarjeta");
            System.out.println("2. Consultar tarjeta");
            System.out.println("3. Ver todas las tarjetas");
            System.out.println("4. Realizar compra");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> createCard();
                case 2 -> getCard();
                case 3 -> getAllCards();
                case 4 -> purchase();
                case 0 -> System.out.println("👋 Saliendo...");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }

    private void createCard() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double quota = FormValidationUtil.validateDouble("Ingrese cupo de crédito: ");
        double creditLimit = FormValidationUtil.validateDouble("Ingrese límite máximo de crédito: ");

        CreditCard newCard = new CreditCard(cardNumber, quota, creditLimit);
        creditCardService.createCreditCard(newCard);

        System.out.println("\n✅ Tarjeta creada exitosamente: " + cardNumber);
    }

    private void getCard() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        var card = creditCardService.getCard(cardNumber);
        if (card != null) {
            printCardDetails(card);
        } else {
            System.out.println("⚠️ No existe una tarjeta con ese número.");
        }
    }

    private void getAllCards() {
        var cards = creditCardService.getAllCards();
        if (cards.isEmpty()) {
            System.out.println("⚠️ No hay tarjetas registradas.");
        } else {
            cards.forEach(this::printCardDetails);
        }
    }

    private void purchase() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto de la compra: ");
        int installments = FormValidationUtil.validateInt("Ingrese número de cuotas: ");

        if (creditCardService instanceof CreditCardServiceImpl impl) {
            impl.purchase(cardNumber, amount, installments);
        } else {
            System.out.println("⚠️ La operación de compra no está disponible.");
        }
    }

    private void printCardDetails(CreditCard card) {
        System.out.println("\n======================================");
        System.out.println("   💳 DETALLES DE LA TARJETA DE CRÉDITO ");
        System.out.println("======================================");
        System.out.printf("Número de tarjeta   : %s%n", card.getAccountNumber());
        System.out.printf("Cupo de crédito     : $%.2f%n", card.getQuota());
        System.out.printf("Límite de crédito   : $%.2f%n", card.getCreditLimit());
        System.out.printf("Deuda actual        : $%.2f%n", card.getDebt());
        System.out.printf("Cuotas últimas compra: %d%n", card.getNumberOfInstallments());
        System.out.printf("Estado              : %s%n", card.getAccountState());
        System.out.println("======================================\n");
    }
}
