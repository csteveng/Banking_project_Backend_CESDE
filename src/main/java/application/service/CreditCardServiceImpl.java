package application.service;

import application.domain.CreditCard;
import application.service.outputs.CreditCardService;
import application.service.ports.CreditCardRepositoryPort;

import java.util.List;

public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepositoryPort repository;

    public CreditCardServiceImpl(CreditCardRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void createCreditCard(CreditCard card) {
        repository.save(card);
    }

    @Override
    public CreditCard getCard(String cardNumber) {
        return repository.findByCardNumber(cardNumber);
    }

    @Override
    public List<CreditCard> getAllCards() {
        return repository.findAll();
    }

    // Tabla de tasas según número de cuotas
    private double getRateByInstallments(int installments) {
        if (installments <= 2) return 0.0;
        if (installments <= 6) return 0.019;
        return 0.023;
    }

    // Fórmula de cuota mensual
    public double calculateMonthlyInstallment(double amount, int installments) {
        double rate = getRateByInstallments(installments);
        if (rate == 0.0) {
            return amount / installments;
        }
        return (amount * rate) / (1 - Math.pow(1 + rate, -installments));
    }

    // Simulación de compra con tarjeta
    public void purchase(String cardNumber, double amount, int installments) {
        CreditCard card = repository.findByCardNumber(cardNumber);
        if (card == null) {
            System.out.println("⚠️ Tarjeta no encontrada.");
            return;
        }

        if (card.getDebt() + amount > card.getQuota() || amount > card.getCreditLimit()) {
            System.out.println("⚠️ Cupo insuficiente o supera el límite de crédito.");
            return;
        }

        double rate = getRateByInstallments(installments);
        double cuota = calculateMonthlyInstallment(amount, installments);
        double totalConInteres = cuota * installments;

        card.setDebt(card.getDebt() + amount);
        card.setNumberOfInstallments(installments);
        repository.update(card);

        System.out.println("\n💳 Compra realizada con tarjeta " + cardNumber);
        System.out.printf("Monto: $%.2f | Cuotas: %d%n", amount, installments);
        System.out.printf("Tasa aplicada: %.2f%%%n", rate * 100);
        System.out.printf("Cuota mensual: $%.2f%n", cuota);
        System.out.printf("Total a pagar con intereses: $%.2f%n", totalConInteres);
        System.out.printf("Nueva deuda acumulada (capital): $%.2f%n", card.getDebt());
    }
}

