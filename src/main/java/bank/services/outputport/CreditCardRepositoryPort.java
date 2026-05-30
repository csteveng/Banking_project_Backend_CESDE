package bank.services.outputport;

import bank.domain.CreditCard;
import java.util.List;

public interface CreditCardRepositoryPort {
    void saveCreditCard(CreditCard card);
    CreditCard findByCardNumber(String cardNumber);

    CreditCard findByClientId(int clientId);

    List<CreditCard> findAll();
    void updateCreditCard(CreditCard card);


}
