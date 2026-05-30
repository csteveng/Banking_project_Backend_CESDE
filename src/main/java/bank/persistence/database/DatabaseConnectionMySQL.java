package bank.persistence.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionMySQL {

    private final Connection connection;
    private static DatabaseConnectionMySQL instance;

    private static final String URL = "jdbc:mysql://localhost:3306/hapi_bank";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DatabaseConnectionMySQL() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database successfully");
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database: " + ex.getMessage());
        }
    }

    public static synchronized DatabaseConnectionMySQL getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionMySQL();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
