package bank.persistence.mapper;

import bank.domain.CreditCard;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCardRowMapper {

    public CreditCard mapRow(ResultSet rs) throws SQLException {
        // 1. Construimos la entidad usando el constructor automático de precisión
        CreditCard card = new CreditCard(
                rs.getString("account_number"),
                rs.getBigDecimal("quota"),          // Nativo BigDecimal
                rs.getBigDecimal("credit_limit"),     // Nativo BigDecimal
                rs.getInt("client_id")
        );

        // 2. Mapeamos las propiedades dinámicas transaccionales acumuladas
        card.setDebt(rs.getBigDecimal("debt")); // ✅ CORREGIDO: Ahora lee como BigDecimal directo de MySQL
        card.setNumberOfInstallments(rs.getInt("number_of_installments"));

        return card;

}
}

