package bank.persistence.mapper;

import bank.domain.SavingsAccount;
import bank.domain.enums.AccountState;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SavingsAccountRowMapper implements RowMapper<SavingsAccount> {

    @Override
    public SavingsAccount mapRow(ResultSet rs) throws SQLException {
        // El constructor necesita exactamente los 8 argumentos que definimos:
        return new SavingsAccount(
                rs.getString("account_number"),
                rs.getBigDecimal("balance"),
                rs.getDate("date_opened").toLocalDate(),
                AccountState.valueOf(rs.getString("account_state")),
                rs.getString("account_type"),
                new ArrayList<>(),           // Lista de transacciones vacía
                rs.getDouble("interest_rate"), // Tasa de interés
                rs.getInt("client_id")
        );
    }
}