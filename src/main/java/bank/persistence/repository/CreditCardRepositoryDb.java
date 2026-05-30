package bank.persistence.repository;

import bank.services.outputport.CreditCardRepositoryPort;
import bank.domain.CreditCard;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreditCardRepositoryDb implements CreditCardRepositoryPort {
    private final Connection connection;

    public CreditCardRepositoryDb(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveCreditCard(CreditCard card) {
        String sql = "INSERT INTO accounts (account_number, balance, date_opened, account_state, account_type, client_id, quota, debt, number_of_installments, credit_limit) " +
                "VALUES (?, ?, ?, ?, 'CREDIT_CARD', ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, card.getAccountNumber());
            ps.setBigDecimal(2, java.math.BigDecimal.ZERO);
            ps.setDate(3, Date.valueOf(java.time.LocalDate.now()));
            ps.setString(4, "ACTIVE");
            ps.setInt(5, card.getClientId());
            ps.setBigDecimal(6, card.getQuota());
            ps.setBigDecimal(7, card.getDebt());
            ps.setInt(8, card.getNumberOfInstallments());
            ps.setBigDecimal(9, card.getCreditLimit());

            ps.executeUpdate();
            System.out.println("💾 [SQL] Tarjeta de crédito guardada exitosamente.");

        } catch (SQLIntegrityConstraintViolationException e) {
            // Interceptamos la violación de MySQL específicamente
            throw new IllegalArgumentException("Violación de integridad: La tarjeta " + card.getAccountNumber() + " ya existe en el sistema.");
        } catch (SQLException e) {
            throw new RuntimeException("Error interno de base de datos al guardar la tarjeta.", e);
        }
    }

    @Override
    public CreditCard findByCardNumber(String cardNumber) {
        String sql = "SELECT account_number, quota, credit_limit, client_id, debt, number_of_installments " +
                "FROM accounts WHERE account_number = ? AND account_type = 'CREDIT_CARD'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cardNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Usamos la nueva lógica optimizada de mapeo de tipos
                    return mapRowToCreditCard(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la tarjeta de crédito por número: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public CreditCard findByClientId(int clientId) {
        String sql = "SELECT account_number, quota, credit_limit, client_id, debt, number_of_installments " +
                "FROM accounts WHERE client_id = ? AND account_type = 'CREDIT_CARD'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Llama al método encargado de mapear la fila (ResultSet) a tu objeto CreditCard
                    return mapRowToCreditCard(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tarjeta por ID de cliente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<CreditCard> findAll() {
        List<CreditCard> cards = new ArrayList<>();
        String sql = "SELECT account_number, quota, credit_limit, client_id, debt, number_of_installments " +
                "FROM accounts WHERE account_type = 'CREDIT_CARD'";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cards.add(mapRowToCreditCard(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todas las tarjetas de crédito: " + e.getMessage(), e);
        }
        return cards;
    }

    @Override
    public void updateCreditCard(CreditCard card) {
        // Actualiza la deuda acumulada y las cuotas tras compras o abonos
        String sql = "UPDATE accounts" +
                " SET debt = ?, number_of_installments = ? " +
                "WHERE account_number = ? AND account_type = 'CREDIT_CARD'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // ✅ Enviamos la deuda de forma nativa como BigDecimal a la base de datos
            ps.setBigDecimal(1, card.getDebt());
            ps.setInt(2, card.getNumberOfInstallments());
            ps.setString(3, card.getAccountNumber());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar los saldos de la tarjeta de crédito: " + e.getMessage(), e);
        }
    }

    // 🛠️ Mapeador interno adaptado a BigDecimal
    private CreditCard mapRowToCreditCard(ResultSet rs) throws SQLException {
        CreditCard card = new CreditCard(
                rs.getString("account_number"),
                rs.getBigDecimal("quota"),
                rs.getBigDecimal("credit_limit"),
                rs.getInt("client_id")
        );
        card.setDebt(rs.getBigDecimal("debt")); // Captura precisa sin double
        card.setNumberOfInstallments(rs.getInt("number_of_installments"));
        return card;
    }
}