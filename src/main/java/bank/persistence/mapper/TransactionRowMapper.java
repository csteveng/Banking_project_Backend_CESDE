package bank.persistence.mapper;

import bank.domain.Transaction;
import bank.domain.enums.TransactionType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs) throws SQLException {
        // Obtenemos el timestamp usando el nombre de columna de tu tabla (transaction_data)
        Timestamp ts = rs.getTimestamp("transaction_date");

        return new Transaction(
                rs.getInt("id"),
                rs.getString("account_number"),
                (ts != null) ? ts.toLocalDateTime() : null,
                TransactionType.valueOf(rs.getString("transaction_type")),
                rs.getBigDecimal("amount"),
                rs.getBigDecimal("balance_after_transaction"),
                rs.getString("description")
        );
    }
}