package bank.persistence.mapper;

import bank.domain.Client;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet rs) throws SQLException {
        Client client = new Client();

        // Estos nombres deben coincidir EXACTAMENTE con los de tu tabla en MySQL
        client.setId(rs.getInt("id"));
        client.setIdentification(rs.getString("identification"));
        client.setFullName(rs.getString("full_name"));   // Cambiado de "fullName"
        client.setCellPhone(rs.getString("cell_phone")); // Cambiado de "cellPhone"
        client.setUserName(rs.getString("user_name"));   // Cambiado de "userName"
        client.setPassword(rs.getString("password"));
        client.setFailedIntents(rs.getInt("failed_intents")); // Cambiado de "failedIntents"
        client.setBlocked(rs.getBoolean("is_blocked"));       // Cambiado de "blocked"

        return client;
    }
}