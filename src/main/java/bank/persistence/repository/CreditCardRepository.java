package bank.persistence.repository;

import bank.domain.CreditCard;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreditCardRepository  {


    List<CreditCard> cards = new ArrayList<>(
            Arrays.asList(
                    new CreditCard("ACC001", BigDecimal.valueOf(1000.0), BigDecimal.valueOf(5000.0),1),
                    new CreditCard("ACC002", BigDecimal.valueOf(1500.0), BigDecimal.valueOf(6000.0),2),
                    new CreditCard("ACC003", BigDecimal.valueOf(2000.0), BigDecimal.valueOf(7000.0),3),
                    new CreditCard("ACC004", BigDecimal.valueOf(2500.0), BigDecimal.valueOf(8000.0),4),
                    new CreditCard("ACC005", BigDecimal.valueOf(3000.0), BigDecimal.valueOf(9000.0),5)
            )
    );




}
