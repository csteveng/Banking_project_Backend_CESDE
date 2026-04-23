package application.repository;

import application.domain.CreditCard;
import application.service.ports.CreditCardRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class CreditCardRepository implements CreditCardRepositoryPort {
    private final List<CreditCard> cards = new ArrayList<>();

    @Override
    public void save(CreditCard card) {
        cards.add(card);
    }

    @Override
    public CreditCard findByCardNumber(String cardNumber) {
        return cards.stream()
                .filter(c -> c.getAccountNumber().equals(cardNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<CreditCard> findAll() {
        return new ArrayList<>(cards);
    }

    @Override
    public void update(CreditCard card) {
        CreditCard existing = findByCardNumber(card.getAccountNumber());
        if (existing != null) {
            cards.remove(existing);
            cards.add(card);
        }
    }
}
