package bank.persistence.repository;

import bank.services.outputport.ICheckingAccountRepository;
import bank.domain.CheckingAccount;
import bank.persistence.mapper.RowMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckingAccountRepositoryDb implements ICheckingAccountRepository {

    private final Connection connection;
    private final RowMapper<CheckingAccount> rowMapper;

    public CheckingAccountRepositoryDb(Connection connection, RowMapper<CheckingAccount> rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public void save(CheckingAccount account) {
        // Implementación de tu lógica INSERT
    }

    @Override
    public CheckingAccount findByAccountNumber(String accountNumber) {
        // CAMBIO CRÍTICO: Asegúrate de que diga 'FROM accounts' en plural
        String sql = "SELECT * FROM accounts WHERE account_number = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Usamos tu mapeador de siempre
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar la cuenta por número: " + accountNumber, e);
        }
        return null; // Si no la encuentra
    }

    @Override
    public List<CheckingAccount> findAll() {
        String sql = "SELECT * FROM accounts"; // Asegúrate de que el nombre de la tabla sea correcto
        List<CheckingAccount> accounts = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al recuperar todas las cuentas", e);
        }
        return accounts;
    }

    @Override
    public void update(CheckingAccount account) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, account.getBalance());
            ps.setString(2, account.getAccountNumber());

            int filasAfectadas = ps.executeUpdate();




            // Si por alguna razón el autocommit está apagado, esto fuerza a que se guarde:
            if (!connection.getAutoCommit()) {
                connection.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el saldo en la base de datos", e);
        }
    }

    @Override
    public void delete(String accountNumber) {
        // Implementación de tu lógica DELETE
    }

    @Override
    public List<CheckingAccount> findByClientId(int clientId) {
        // Usamos 'account_type' que es el nombre real en tu base de datos
        String sql = "SELECT * FROM accounts WHERE client_id = ? AND account_type = 'CHECKING'";
        List<CheckingAccount> accounts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar cuentas corrientes del cliente: " + clientId, e);
        }
        return accounts;
    }
}