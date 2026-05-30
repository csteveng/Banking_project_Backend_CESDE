package bank.view;

import bank.services.inputs.CreditCardService;
import bank.domain.CreditCard;
import bank.domain.PurchaseResult;
import bank.utils.FormValidationUtil;
import java.math.BigDecimal;
import java.util.List;

public class CreditCardView {
    private final CreditCardService creditCardService;
    private int currentClientId; // Este es el ID que viene del MainMenuView

    public CreditCardView(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    // Método para recibir el ID del cliente logueado
    public void setLoggedInClientId(int clientId) {
        this.currentClientId = clientId;
    }

    public void createCard() {


        if (this.currentClientId <= 0) {
            System.out.println("⚠️ Error: Cliente no autenticado correctamente.");
            return;
        }
        System.out.println("--- ASIGNACIÓN AUTOMÁTICA DE TARJETA DE CRÉDITO ---");
        System.out.println("DEBUG: Intentando crear tarjeta para el cliente ID: " + this.currentClientId);

        double quotaDouble = FormValidationUtil.validateDouble("Ingrese cupo de crédito: ");
        double limitDouble = FormValidationUtil.validateDouble("Ingrese límite máximo por compra: ");

        // Convertimos a BigDecimal para el dominio
        BigDecimal quota = BigDecimal.valueOf(quotaDouble);
        BigDecimal limit = BigDecimal.valueOf(limitDouble);

        // Generamos un número de tarjeta aleatorio (ejemplo simple)
        String cardNumber = "4" + (long)(Math.random() * 100000L);

        // Creamos la entidad
        CreditCard card = new CreditCard(cardNumber, quota, limit, this.currentClientId);

        try {
            creditCardService.createCreditCard(card);
            System.out.println("✅ Tarjeta creada con éxito. Número: " + cardNumber);
        } catch (IllegalArgumentException e) {
            // Capturamos específicamente la regla de negocio que agregamos en el Service
            System.out.println("⚠️ " + e.getMessage());
        } catch (Exception e) {
            System.err.println("⚠️ Error crítico al guardar la tarjeta: " + e.getMessage());
        }
    }

    public void getCard() {
        CreditCard card = creditCardService.getCardByClientId(this.currentClientId);
        if (card != null) {
            System.out.println("\n💳 INFORMACIÓN DE TARJETA: " + card.toString());
        } else {
            System.out.println("⚠️ No tienes una tarjeta registrada.");
        }
    }

    public void purchase() {
        CreditCard card = creditCardService.getCardByClientId(this.currentClientId);
        if (card == null) {
            System.out.println("⚠️ No tienes tarjeta para realizar compras.");
            return;
        }

        double amountDouble = FormValidationUtil.validateDouble("Ingrese monto de la compra: ");
        int installments = FormValidationUtil.validateInt("Ingrese número de cuotas: ");

        try {
            PurchaseResult result = creditCardService.purchaseCreditCard(
                    card.getAccountNumber(),
                    BigDecimal.valueOf(amountDouble),
                    installments
            );
            System.out.println("✅ Compra exitosa. Nueva deuda: $" + result.getNewDebt());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    public void getAllCards() {
        List<CreditCard> cards = creditCardService.getAllCards();
        if (cards.isEmpty()) {
            System.out.println("⚠️ No hay tarjetas registradas.");
        } else {
            cards.forEach(System.out::println);
        }
    }

    public void pay() {
        CreditCard card = creditCardService.getCardByClientId(this.currentClientId);
        if (card == null) {
            System.out.println("⚠️ No tienes tarjeta para realizar pagos.");
            return;
        }

        System.out.println("💳 Deuda actual: $" + card.getDebt());
        double amountDouble = FormValidationUtil.validateDouble("Ingrese el monto a abonar: ");
        BigDecimal amount = BigDecimal.valueOf(amountDouble);

        try {
            creditCardService.pay(card.getAccountNumber(), amount);
            // El éxito se imprime desde el servicio, pero aquí podemos dar feedback extra
            System.out.println("✅ Pago procesado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }
}