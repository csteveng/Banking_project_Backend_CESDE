package bank.persistence.mapper;

import bank.domain.CheckingAccount;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckingAccountRowMapper implements RowMapper<CheckingAccount>{


    @Override
    public CheckingAccount mapRow(ResultSet rs) throws SQLException {
        // 1. Extraemos los campos adicionales que ahora son obligatorios
        int clientId = rs.getInt("client_id");
        String accountType = rs.getString("account_type"); // O pon "CHECKING" si es fijo


        return new CheckingAccount(
                rs.getString("account_number"),
                rs.getBigDecimal("balance"),
                accountType,
                clientId,
                rs.getDouble("overdraft_percentage"),
                rs.getBigDecimal("overdraft_limit")
        );
    }
}
