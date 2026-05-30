package bank.persistence.repository;

import bank.services.outputport.IClientRepository;
import bank.domain.Client;
import bank.persistence.mapper.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryDb implements IClientRepository {

    private final Connection connection;
    private final RowMapper<Client> clientRowMapper;


    public ClientRepositoryDb(Connection connection, RowMapper<Client> clientRowMapper) {
        this.connection = connection;
        this.clientRowMapper = clientRowMapper;
    }

    @Override
    public Optional<Client> findByUserName(String userName) {
        String sql = "SELECT * FROM clients WHERE user_name = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    Client client = (Client) clientRowMapper.mapRow(rs);
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el nombre de usuario del cliente",e);
        }


        return Optional.empty();
    }

    @Override
    public Optional<Client> findById(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client client = (Client) clientRowMapper.mapRow(rs);
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el id del cliente ", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clientList = new ArrayList<>();

        String sql = "SELECT * FROM clients";

        try(PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Client client = (Client) clientRowMapper.mapRow(rs);
                clientList.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al mostrar todos los clientes ", e);
        }

        return  clientList;
    }

    @Override
    public Client save(Client client) {

        String sql = "INSERT INTO clients (identification, full_name, cell_phone, user_name, password, failed_intents, is_blocked) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, client.getIdentification());
            ps.setString(2, client.getFullName());
            ps.setString(3, client.getCellPhone());
            ps.setString(4, client.getUserName()); // Faltaba el userName
            ps.setString(5, client.getPassword());
            ps.setInt(6, client.getFailedIntents());
            ps.setBoolean(7, client.isBlocked());

            ps.executeUpdate();

            // Opcional pero recomendado: Recuperar el ID que la base de datos le asignó
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    client.setId(generatedId);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error al guardar el cliente", ex);
        }

        return client;
    }

    @Override
    public Client update(Client client) {
        String sql = "UPDATE clients SET full_name = ?, cell_phone = ?, password = ?, failed_intents = ?, is_blocked = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, client.getFullName());
            ps.setString(2, client.getCellPhone());
            ps.setString(3, client.getPassword());
            ps.setInt(4, client.getFailedIntents());
            ps.setBoolean(5, client.isBlocked());
            ps.setInt(6, client.getId()); // Necesitas un método getId() en Client

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("No se puede actualizar el cliente", e);
        }

        return client;
    }

    @Override
    public void deleteClient(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Advertencia: No se encontró ningún cliente con el id " + id + " para eliminar");
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se puede eliminar el cliente", e);
        }
    }
}