package application.service.ports;

import application.domain.CreditCard;
import java.util.List;

public interface CreditCardRepositoryPort {
    void save(CreditCard card);
    CreditCard findByCardNumber(String cardNumber);
    List<CreditCard> findAll();
    void update(CreditCard card);
}
