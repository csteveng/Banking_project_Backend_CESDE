package bank.services;

import bank.services.outputport.ISavingsAccountRepository;
import bank.services.outputport.ITransactionRepository;
import bank.domain.CheckingAccount;
import bank.domain.SavingsAccount;
import bank.domain.Transaction;
import bank.domain.enums.TransactionType;
import bank.services.inputs.CheckingAccountService;
import bank.services.outputport.ICheckingAccountRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckingAccountServiceImpl implements CheckingAccountService {
    private final ICheckingAccountRepository checkingAccountRepository;
    private final ITransactionRepository transactionRepository;
    private final ISavingsAccountRepository savingsRepository;

    public CheckingAccountServiceImpl(ICheckingAccountRepository checkingAccountRepository, ITransactionRepository transactionRepository , ISavingsAccountRepository savingsRepository) {
        this.checkingAccountRepository = checkingAccountRepository;
        this.transactionRepository = transactionRepository;
        this.savingsRepository = savingsRepository;
    }



    @Override
    public void createAccount(CheckingAccount account) {
        checkingAccountRepository.save(account);
    }

    @Override
    public CheckingAccount getAccount(String accountNumber) {
        return checkingAccountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public List<CheckingAccount> getAllAccounts(int clientId) {
        return checkingAccountRepository.findByClientId(clientId);
    }



    @Override
    public CheckingAccount getAccountByClientId(int clientId) {
        List<CheckingAccount> accounts = checkingAccountRepository.findByClientId(clientId);
        // Si la lista no está vacía, devuelve la primera cuenta, si no, devuelve null
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    @Override
    public void deposit(String accountNumber, BigDecimal amount) {
        CheckingAccount account =checkingAccountRepository.findByAccountNumber(accountNumber);


        if (account == null) {
            System.out.println("⚠️ Error: La cuenta " + accountNumber + " no existe.");
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("⚠️ Error: El monto a depositar debe ser mayor a cero.");
            return;
        }
        // Operación matemática segura con BigDecimal
        account.setBalance(account.getBalance().add(amount));


        Transaction depositRecord = new Transaction(
                accountNumber, // Asegúrate de que tu constructor reciba estos campos
                TransactionType.DEPOSIT,
                amount,
                account.getBalance(),
                "Depósito realizado desde ventanilla/servicio"
        );
        transactionRepository.save(depositRecord);
        checkingAccountRepository.update(account);
        System.out.println("\n✅ Depósito exitoso. Nuevo saldo: $" + account.getBalance());
    }

    @Override
    public int getClientIdByAccountNumber(String accountNumber) {
        CheckingAccount checking = checkingAccountRepository.findByAccountNumber(accountNumber);
        if (checking != null) return checking.getClientId();

        var savings = savingsRepository.findById(accountNumber);
        if (savings.isPresent()) return savings.get().getClientId();

        return -1;
    }

    @Override
    public CheckingAccount findByAccountNumber(String accountNumber) {
        // Tu repositorio ya sabe hacer esta consulta en la base de datos
        return checkingAccountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public void withdraw(String accountNumber, BigDecimal amount) {
        CheckingAccount account = checkingAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("⚠️ Error: La cuenta " + accountNumber + " no existe.");
            return;
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("⚠️ Error: El monto a retirar debe ser mayor a cero.");
            return;
        }

        BigDecimal availableFunds = account.getBalance().add(account.getOverdraftLimit());

        if (availableFunds.compareTo(amount) >= 0) {
            // 1. Primero, restamos el saldo
            account.setBalance(account.getBalance().subtract(amount));

            // 2. 🔥 AQUÍ VA EL BLOQUE QUE ME PREGUNTAS:
            // Registramos la transacción en la base de datos de movimientos
            Transaction withdrawalRecord = new Transaction(
                    accountNumber,
                    TransactionType.WITHDRAWAL,
                    amount,
                    account.getBalance(),
                    "Retiro en efectivo"
            );
            transactionRepository.save(withdrawalRecord);

            // 3. Finalmente, guardamos el nuevo saldo en la tabla de cuentas
            checkingAccountRepository.update(account);

            System.out.println("\n✅ Retiro exitoso. Nuevo saldo: $" + account.getBalance());
        } else {
            System.out.println("⚠️ Fondos insuficientes (incluso con sobregiro).");
        }
    }
    @Override
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        CheckingAccount account = checkingAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("⚠️ La cuenta corriente no existe.");
            return new ArrayList<>();
        }
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        CheckingAccount origin = checkingAccountRepository.findByAccountNumber(fromAccount);
        if (origin == null) {
            System.out.println("⚠️ Error: La cuenta corriente origen no existe.");
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("⚠️ Error: El monto debe ser mayor a cero.");
            return;
        }

        if (origin.getBalance().compareTo(amount) < 0) {
            System.out.println("⚠️ Error: Fondos insuficientes en la cuenta corriente.");
            return;
        }

        // Búsqueda cruzada en ambas tablas
        CheckingAccount destChecking = checkingAccountRepository.findByAccountNumber(toAccount);
        Optional<SavingsAccount> destSavingsOpt = savingsRepository.findById(toAccount);

        if (destChecking != null) {
            // Caso A: Corriente -> Corriente
            origin.setBalance(origin.getBalance().subtract(amount));
            destChecking.setBalance(destChecking.getBalance().add(amount));

            checkingAccountRepository.update(origin);
            checkingAccountRepository.update(destChecking);
            System.out.println("\n💸 ¡Transferencia exitosa de Corriente a Corriente!");

        } else if (destSavingsOpt.isPresent()) {
            // Caso B: Corriente -> Ahorros 🔥
            SavingsAccount destSavings = destSavingsOpt.get();

            origin.setBalance(origin.getBalance().subtract(amount));
            destSavings.setBalance(destSavings.getBalance().add(amount));

            checkingAccountRepository.update(origin);            // Guarda en la tabla de corrientes
            savingsRepository.update(destSavings); // Guarda en la tabla de ahorros
            System.out.println("\n💸 ¡Transferencia interbancaria exitosa (Corriente -> Ahorros)!");

        } else {
            System.out.println("⚠️ Error: La cuenta destino (" + toAccount + ") no existe.");
            return;
        }

        // Registrar comprobante de salida (TRANSFER_OUT)
        Transaction transferOutRecord = new Transaction(
                fromAccount,
                TransactionType.TRANSFER_OUT,
                amount,
                origin.getBalance(),
                "Transferencia enviada a cuenta " + toAccount
        );
        transactionRepository.save(transferOutRecord);

        // Registrar comprobante de entrada (TRANSFER_IN)
        Transaction transferInRecord = new Transaction(
                toAccount,
                TransactionType.TRANSFER_IN,
                amount,
                destChecking != null ? destChecking.getBalance() : destSavingsOpt.get().getBalance(),
                "Transferencia recibida de cuenta corriente " + fromAccount
        );
        transactionRepository.save(transferInRecord);



    }
    @Override
    public List<CheckingAccount> findByClientId(int clientId) {

        return checkingAccountRepository.findByClientId(clientId);
    }
}
