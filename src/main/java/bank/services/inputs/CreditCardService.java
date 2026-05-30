package bank.services.inputs;

import bank.domain.CreditCard;
import bank.domain.PurchaseResult;

import java.math.BigDecimal;
import java.util.List;

public interface CreditCardService {
    PurchaseResult purchaseCreditCard(String cardNumber, BigDecimal amount, int installments);
    void createCreditCard(CreditCard card);
    CreditCard getCard(String cardNumber);


    CreditCard getCardByClientId(int clientId);

    List<CreditCard> getAllCards();
    void pay(String cardNumber, BigDecimal amount);

}





