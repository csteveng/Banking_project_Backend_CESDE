package bank.services;

import bank.domain.CreditCard;
import bank.services.inputs.CreditCardService;
import bank.domain.PurchaseResult;
import bank.services.outputport.CreditCardRepositoryPort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepositoryPort creditCardRepositoryPort;

    public CreditCardServiceImpl(CreditCardRepositoryPort creditCardRepository) {
        this.creditCardRepositoryPort = creditCardRepository;
    }

    @Override
    public void createCreditCard(CreditCard card) {
        CreditCard tarjetaExistente = creditCardRepositoryPort.findByCardNumber(card.getAccountNumber());
        if (tarjetaExistente != null) {
            throw new IllegalArgumentException("Ya existe una tarjeta registrada con el número: " + card.getAccountNumber());
        }
        creditCardRepositoryPort.saveCreditCard(card);
    }

    @Override
    public CreditCard getCard(String cardNumber) {
        return creditCardRepositoryPort.findByCardNumber(cardNumber);
    }

    @Override
    public CreditCard getCardByClientId(int clientId) {
        return creditCardRepositoryPort.findByClientId(clientId);
    }

    @Override
    public List<CreditCard> getAllCards() {
        return creditCardRepositoryPort.findAll();
    }

    @Override
    public PurchaseResult purchaseCreditCard(String cardNumber, BigDecimal amount, int installments) {
        // Validación de entradas ("Fail Fast")
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de la compra debe ser mayor a cero.");
        }
        if (installments <= 0) {
            throw new IllegalArgumentException("El número de cuotas debe ser al menos 1.");
        }

        CreditCard card = creditCardRepositoryPort.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada.");
        }

        BigDecimal nuevaDeuda = card.getDebt().add(amount);

        if (nuevaDeuda.compareTo(card.getQuota()) > 0 || amount.compareTo(card.getCreditLimit()) > 0) {
            throw new IllegalArgumentException("Cupo insuficiente o supera el límite de crédito.");
        }

        // Cálculos financieros con precisión estricta
        BigDecimal rate = getRateByInstallments(installments);
        BigDecimal cuota = calculateMonthlyInstallment(amount, rate, installments);
        BigDecimal totalConInteres = cuota.multiply(BigDecimal.valueOf(installments));

        // Actualización de estado
        card.setDebt(nuevaDeuda);
        card.setNumberOfInstallments(installments);
        creditCardRepositoryPort.updateCreditCard(card);

        // Se usa .doubleValue() asumiendo que tu DTO (PurchaseResult) actual requiere double.
        // ⚠️ Recomendación: Actualiza PurchaseResult para que reciba BigDecimal.
        return new PurchaseResult(
                amount.doubleValue(),
                installments,
                rate.doubleValue(),
                cuota.doubleValue(),
                totalConInteres.doubleValue(),
                nuevaDeuda.doubleValue()
        );
    }

    // Retorna BigDecimal usando String en el constructor para evitar pérdida de precisión inicial
    private BigDecimal getRateByInstallments(int installments) {
        if (installments <= 1) return BigDecimal.ZERO;
        if (installments <= 3) return new BigDecimal("0.02");
        if (installments <= 6) return new BigDecimal("0.03");
        if (installments <= 12) return new BigDecimal("0.04");
        return new BigDecimal("0.05");
    }

    private BigDecimal calculateMonthlyInstallment(BigDecimal amount, BigDecimal monthlyRate, int installments) {
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return amount.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);
        }

        // Fórmula matemática de amortización convertida a BigDecimal:
        // Cuota = [ Monto * Tasa * (1 + Tasa)^N ] / [ (1 + Tasa)^N - 1 ]
        BigDecimal factor = BigDecimal.ONE.add(monthlyRate).pow(installments);
        BigDecimal numerator = amount.multiply(monthlyRate).multiply(factor);
        BigDecimal denominator = factor.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    @Override
    public void pay(String cardNumber, BigDecimal amount) {
        // Validaciones tempranas
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a pagar debe ser mayor que cero.");
        }

        CreditCard card = creditCardRepositoryPort.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada.");
        }

        if (amount.compareTo(card.getDebt()) > 0) {
            throw new IllegalArgumentException("El monto excede la deuda actual.");
        }

        BigDecimal nuevaDeuda = card.getDebt().subtract(amount);
        card.setDebt(nuevaDeuda);

        if (nuevaDeuda.compareTo(BigDecimal.ZERO) == 0) {
            card.setNumberOfInstallments(0);
        }

        creditCardRepositoryPort.updateCreditCard(card);
    }
}