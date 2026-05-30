package bank.services;

import bank.services.outputport.ICheckingAccountRepository;
import bank.services.outputport.ITransactionRepository;
import bank.domain.SavingsAccount;
import bank.domain.Transaction;
import bank.domain.enums.AccountState;
import bank.domain.enums.TransactionType;
import bank.services.inputs.SavingsAccountService;
import bank.services.outputport.ISavingsAccountRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SavingsAccountServiceImpl implements SavingsAccountService {
    private final ISavingsAccountRepository iSavingsAccountRepository;
    private final ITransactionRepository transactionRepository;
    private final ICheckingAccountRepository checkingRepository;

    public SavingsAccountServiceImpl(ISavingsAccountRepository repository , ITransactionRepository transactionRepository ,  ICheckingAccountRepository checkingRepository ) {
        this.iSavingsAccountRepository = repository;
        this.transactionRepository = transactionRepository;
        this.checkingRepository = checkingRepository;
    }


    // En SavingsAccountServiceImpl

    @Override
    public SavingsAccount createAccount(String accountNumber, BigDecimal initialBalance, int clientId) {
        Optional<SavingsAccount> existing = iSavingsAccountRepository.findById(accountNumber);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("La cuenta " + accountNumber + " ya existe.");
        }

        // Aquí ya tienes tus 8 parámetros (String, BigDecimal, LocalDate, Enum, String, List, double, int)
        SavingsAccount newAccount = new SavingsAccount(
                accountNumber,
                initialBalance,
                LocalDate.now(),
                AccountState.ACTIVE,
                "SAVINGS",
                new ArrayList<>(),
                0.02,
                clientId
        );

        iSavingsAccountRepository.save(newAccount);
        System.out.println("✅ Cuenta de ahorros creada con éxito.");
        return newAccount;
    }


    @Override
    public void deposit(String accountNumber, BigDecimal amount) {
        Optional<SavingsAccount> accountOpt = iSavingsAccountRepository.findById(accountNumber);

        if (accountOpt.isEmpty()) {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
            return;
        }

        SavingsAccount account = accountOpt.get();

        if (account.getAccountState() != AccountState.ACTIVE) {
            System.out.println("Error: La cuenta no está activa.");
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Error: El monto a depositar debe ser mayor a cero.");
            return;
        }


        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        Transaction depositRecord = new Transaction(
                accountNumber,
                TransactionType.DEPOSIT,
                amount,
                newBalance,
                "Depósito en cuenta de ahorros desde el servicio"
        );

        transactionRepository.save(depositRecord);
        account.getTransactions().add(depositRecord);

        iSavingsAccountRepository.update(account);
        System.out.println("Depósito exitoso. Nuevo saldo: $" + newBalance);
    }

    @Override
    public void withdraw(String accountNumber, BigDecimal amount) {
        // 1. Buscamos la cuenta
        Optional<SavingsAccount> accountOpt = iSavingsAccountRepository.findById(accountNumber);

        // 2. Validación de existencia: ¡Esta es la clave!
        if (accountOpt.isEmpty()) {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
            return; // Detiene la ejecución aquí mismo si no existe
        }

        SavingsAccount account = accountOpt.get();

        // 3. Validación de saldo suficiente
        if (account.getBalance().compareTo(amount) >= 0) {
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(account.getBalance().subtract(amount));

            Transaction withdrawRecord = new Transaction(
                    accountNumber,
                    TransactionType.WITHDRAWAL,
                    amount,
                    newBalance,
                    "Retiro realizado desde el servicio"

            );

            transactionRepository.save(withdrawRecord);
            account.getTransactions().add(withdrawRecord);

            iSavingsAccountRepository.update(account);
            System.out.println("✅ Retiro realizado correctamente.");
        } else {
            System.out.println("Error: Saldo insuficiente para realizar el retiro.");
        }

    }

    @Override
    public void applyInterest(String accountNumber) {
        Optional<SavingsAccount> accountOpt = iSavingsAccountRepository.findById(accountNumber);

        if (accountOpt.isEmpty()) {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
            return;
        }

        SavingsAccount account = accountOpt.get();

        if (account.getAccountState() != AccountState.ACTIVE) {
            System.out.println("Error: La cuenta no está activa para recibir intereses.");
            return;
        }

        BigDecimal interestRate = BigDecimal.valueOf(account.getInterestRate() / 100.0);
        BigDecimal interestAmount = account.getBalance().multiply(interestRate);
        BigDecimal newBalance = account.getBalance().add(interestAmount);

        Transaction interestRecord = new Transaction(
                accountNumber,
                TransactionType.DEPOSIT,
                interestAmount,
                newBalance,
                "Abono por liquidación de intereses"
        );
        transactionRepository.save(interestRecord);
        account.getTransactions().add(interestRecord);

        iSavingsAccountRepository.update(account);
        System.out.println("Intereses aplicados a la cuenta " + accountNumber + ": $" + interestAmount);
    }

    @Override
    public    SavingsAccount getAccount(String accountNumber) {
        Optional<SavingsAccount> accountOpt = iSavingsAccountRepository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("La cuenta " + accountNumber + " no existe.");
        }
        return accountOpt.get();
    }


    @Override
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        Optional<SavingsAccount> accountOpt = iSavingsAccountRepository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            System.out.println("⚠️ La cuenta no existe.");
            return new ArrayList<>();
        }
        // Va directo al repositorio de tus compañeros a buscar en la tabla 'transactions'
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {


        Optional<SavingsAccount> originOpt = iSavingsAccountRepository.findById(fromAccount);
        if (originOpt.isEmpty()) {
            System.out.println("⚠️ Error: La cuenta de ahorros origen (" + fromAccount + ") no existe.");
            return;
        }
        SavingsAccount origin = originOpt.get();


        // 3. Validar que la cuenta origen esté activa y tenga saldo suficiente
        if (origin.getAccountState() != AccountState.ACTIVE) {
            System.out.println("⚠️ Error: La cuenta origen no está activa.");
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("⚠️ Error: El monto a transferir debe ser mayor a cero.");
            return;
        }

        if (origin.getBalance().compareTo(amount) < 0) {
            System.out.println("⚠️ Error: Fondos insuficientes en la cuenta de ahorros origen.");
            return;
        }
        Optional<SavingsAccount> destSavingsOpt = iSavingsAccountRepository.findById(toAccount);
        bank.domain.CheckingAccount destChecking = checkingRepository.findByAccountNumber(toAccount);
        // 3. Efectuar la transferencia según el tipo de cuenta destino encontrada
        if (destSavingsOpt.isPresent()) {
            // CASO A: Transferencia de Ahorros a Ahorros
            SavingsAccount destSavings = destSavingsOpt.get();

            origin.setBalance(origin.getBalance().subtract(amount));
            destSavings.setBalance(destSavings.getBalance().add(amount));

            // Persistir saldos actualizados
            iSavingsAccountRepository.update(origin);
            iSavingsAccountRepository.update(destSavings);

            System.out.println("\n💸 ¡Transferencia exitosa de Ahorros a Ahorros!");

        } else if (destChecking != null) {
            // CASO B: Transferencia de Ahorros a Cuenta Corriente 🔥
            origin.setBalance(origin.getBalance().subtract(amount));
            destChecking.setBalance(destChecking.getBalance().add(amount));

            // Persistir saldos en sus respectivas tablas correspondientes
            iSavingsAccountRepository.update(origin); // Actualiza la tabla de ahorros
            checkingRepository.update(destChecking); // Actualiza la tabla de corrientes


            System.out.println("\n💸 ¡Transferencia  exitosa (Ahorros -> Corriente)!");

        } else {
            // CASO C: No existe en ningún lado
            System.out.println("⚠️ Error: La cuenta destino (" + toAccount + ") no existe en el sistema.");
            return;
        }

        // 4. Registrar los comprobantes de transacciones en la base de datos
        Transaction transferOutRecord = new Transaction(
                fromAccount,
                TransactionType.TRANSFER_OUT,
                amount,
                origin.getBalance(),
                "Transferencia enviada a cuenta " + toAccount
        );
        transactionRepository.save(transferOutRecord);

        // Nota: El saldo destino varía según el tipo de cuenta, por simplicidad registramos el monto enviado
        Transaction transferInRecord = new Transaction(
                toAccount,
                TransactionType.TRANSFER_IN,
                amount,
                destSavingsOpt.isPresent() ? destSavingsOpt.get().getBalance() : destChecking.getBalance(),
                "Transferencia recibida de cuenta de ahorros " + fromAccount
        );
        transactionRepository.save(transferInRecord);


    }
    @Override
    public List<SavingsAccount> findByClientId(int clientId) {
        // Le pide al repositorio que busque las cuentas de ahorro de ese cliente en la BD
        return iSavingsAccountRepository.findByClientId(clientId);
    }
}
