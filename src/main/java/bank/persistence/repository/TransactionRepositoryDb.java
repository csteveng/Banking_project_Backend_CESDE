package bank.persistence.repository;

import bank.services.outputport.ITransactionRepository;
import bank.domain.Transaction;
import bank.persistence.mapper.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionRepositoryDb implements ITransactionRepository {

    private final Connection connection;
    private final RowMapper<Transaction> transactionRowMapper;

    public TransactionRepositoryDb(Connection connection, RowMapper<Transaction> transactionRowMapper) {
        this.connection = connection;
        this.transactionRowMapper = transactionRowMapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_number, transaction_date, transaction_type, amount, balance_after_transaction, description) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Asignamos los valores básicos
            ps.setString(1, transaction.getAccountNumber());

            // 2. Convertimos LocalDateTime a java.sql.Timestamp para la BD
            ps.setTimestamp(2, Timestamp.valueOf(transaction.getTimestamp()));

            // 3. Extraemos el texto exacto del Enum
            ps.setString(3, transaction.getTransactionType().name());

            // 4. Los BigDecimal pasan directo usando setBigDecimal
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setBigDecimal(5, transaction.getBalanceAfterTransaction());

            ps.setString(6, transaction.getDescription());

            // Ejecutamos la inserción
            ps.executeUpdate();

            // 5. Recuperamos el ID que le asignó la base de datos
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    transaction.setId(generatedId); // Actualizamos el objeto en memoria
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error al guardar la transacción de la cuenta: " + transaction.getAccountNumber(), ex);
        }

        return transaction;
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        List<Transaction> history = new ArrayList<>();

        // TRUCO BANCARIO: Usamos ORDER BY transaction_date DESC para que
        // las transacciones más recientes aparezcan primero (como en un extracto real).
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = transactionRowMapper.mapRow(rs);
                    history.add(transaction);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el historial de la cuenta: " + accountNumber, e);
        }

        return history;
    }

    @Override
    public Optional<Transaction> findById(int id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = transactionRowMapper.mapRow(rs);
                    return Optional.of(transaction);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la transacción con ID: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> allTransactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Transaction transaction = transactionRowMapper.mapRow(rs);
                allTransactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar todas las transacciones.", e);
        }

        return allTransactions;
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        String sql = "SELECT * FROM transactions WHERE account_number = ?"; // Verifica si el campo es account_number o parecido
        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Aquí usamos el TransactionRowMapper
                    transactions.add(transactionRowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            // 🔥 ESTA LÍNEA ES CLAVE: imprime el error real de SQL
            e.printStackTrace();
            throw new RuntimeException("Error al buscar el historial de la cuenta: " + accountNumber, e);
        }
        return transactions;
    }
}