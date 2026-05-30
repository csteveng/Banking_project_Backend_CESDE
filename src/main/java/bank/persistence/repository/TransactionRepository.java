package bank.persistence.repository;

import bank.domain.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionRepository {

    // Nuestra tabla de base de datos simulada para auditoría global
    // Clave: Integer (ID de la transacción) | Valor: Transaction (El registro completo)
    private final Map<Integer, Transaction> database = new HashMap<>();


    public void save(Transaction transaction) {
        // Usamos el ID de la transacción como llave maestra
        database.put(transaction.getId(), transaction);
    }


    public Optional<Transaction> findById(int id) {
        // Búsqueda instantánea O(1) gracias al HashMap
        return Optional.ofNullable(database.get(id));
    }


    public List<Transaction> findAll() {
        // Convertimos los valores del mapa en una lista para retornarlos
        return new ArrayList<>(database.values());
    }
}