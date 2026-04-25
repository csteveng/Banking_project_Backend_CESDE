package application.service.outputs;

import application.domain.CreditCard;
import java.util.List;

public interface CreditCardService {
    void createCreditCard(CreditCard card);
    CreditCard getCard(String cardNumber);
    List<CreditCard> getAllCards();
}

