package application.userinterface;

import application.service.ClientService;
import application.domain.CreditCard;
import application.service.ClientServiceImpl;
import application.util.FormValidationUtil;

import java.util.Map;

public class MenuCreditCard {
    private final ClientService clientService;

    public MenuCreditCard(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n=== MENÚ TARJETAS DE CRÉDITO (USUARIO) ===");
            System.out.println("1. Listar tarjetas disponibles");
            System.out.println("2. Asignar tarjeta a cliente");
            System.out.println("3. Ver tarjetas asignadas");
            System.out.println("0. Volver al menú principal");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> listAvailableCards();
                case 2 -> assignCard();
                case 3 -> listAssignedCards();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }

    private void listAvailableCards() {
        System.out.println("Tarjetas disponibles para asignación:");
        var cards = clientService.getAvailableCards();
        if (cards.isEmpty()) {
            System.out.println("⚠️ No hay tarjetas creadas en el sistema.");
        } else {
            cards.forEach(card ->
                    System.out.println("Número: " + card.getAccountNumber() +
                            " | Cupo: " + card.getQuota() +
                            " | Límite: " + card.getCreditLimit()));
        }
    }

    private void assignCard() {
        int clientId = FormValidationUtil.validateInt("Ingrese ID del cliente: ");
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        try {
            clientService.assignCardToClient(clientId, cardNumber);
            System.out.println("✅ Tarjeta asignada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private void listAssignedCards() {
        Map<Integer, CreditCard> assigned = clientService.getAssignedCards();
        if (assigned.isEmpty()) {
            System.out.println("⚠️ No hay tarjetas asignadas.");
        } else {
            System.out.println("\nTarjetas asignadas:");
            assigned.forEach((clientId, card) ->
                    System.out.println("Cliente ID: " + clientId + " -> Tarjeta: " + card.getAccountNumber()));
        }
    }
}
