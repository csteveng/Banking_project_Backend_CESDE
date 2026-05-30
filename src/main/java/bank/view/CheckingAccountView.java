package bank.view;

import bank.services.inputs.IClientManagement;
import bank.domain.CheckingAccount;
import bank.services.inputs.CheckingAccountService;
import bank.utils.FormValidationUtil;

import java.math.BigDecimal;
import java.util.List;

public class CheckingAccountView {
    private final CheckingAccountService checkingAccountService;
    private final IClientManagement clientManagement;
    private int currentClientId;

    public CheckingAccountView(CheckingAccountService checkingAccountService, IClientManagement clientManagement) {
        this.checkingAccountService = checkingAccountService;
        this.clientManagement = clientManagement;
    }

    public void setLoggedInClientId(int clientId) {
        this.currentClientId = clientId;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- MENÚ CUENTA CORRIENTE ---");
            System.out.println("1. Crear cuenta");
            System.out.println("2. Consultar cuenta");
            System.out.println("3. Ver todas las cuentas");
            System.out.println("4. Depositar");
            System.out.println("5. Retirar");
            System.out.println("6. Transferir");
            System.out.println("7. Ver movimientos");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> createAccount();
                case 2 -> getAccount();
                case 3 -> getAllAccounts();
                case 4 -> performTransaction("depositar");
                case 5 -> performTransaction("retirar");
                case 6 -> transfer();
                case 7 -> showTransactions();
                case 0 -> System.out.println("👋 Saliendo...");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }

    // --- MÉTODOS AUXILIARES ---

    private CheckingAccount selectAccount() {
        List<CheckingAccount> accounts = checkingAccountService.findByClientId(this.currentClientId);
        if (accounts.isEmpty()) {
            System.out.println("⚠️ No tienes cuentas registradas.");
            return null;
        }
        if (accounts.size() == 1) return accounts.get(0);

        System.out.println("Seleccione una cuenta:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i).getAccountNumber());
        }
        int index = FormValidationUtil.validateInt("Opción: ") - 1;
        return (index >= 0 && index < accounts.size()) ? accounts.get(index) : null;
    }

    private void performTransaction(String type) {
        CheckingAccount acc = selectAccount();
        if (acc == null) return;
        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a " + type + ": ");
        if (type.equals("depositar")) checkingAccountService.deposit(acc.getAccountNumber(), amount);
        else checkingAccountService.withdraw(acc.getAccountNumber(), amount);
    }

    // --- MÉTODOS ORIGINALES ---

    private void createAccount() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        int clientId = FormValidationUtil.validateInt("Ingrese ID del cliente dueño: ");
        BigDecimal balance = FormValidationUtil.validateBigDecimal("Ingrese saldo inicial: ");
        BigDecimal overdraftLimit = FormValidationUtil.validateBigDecimal("Ingrese límite de sobregiro: ");
        double overdraftPercentage = FormValidationUtil.validateDouble("Ingrese porcentaje de sobregiro: ");

        CheckingAccount newAccount = new CheckingAccount(accountNumber, balance, "Cuenta Corriente", clientId, overdraftPercentage, overdraftLimit);
        checkingAccountService.createAccount(newAccount);
        System.out.println("\n✅ Cuenta creada exitosamente.");
    }


    private void printAccountDetails(CheckingAccount acc) {
        System.out.println("\n==========================================");
        System.out.println("          DETALLES DE LA CUENTA           ");
        System.out.println("==========================================");
        System.out.printf(" %-20s : %s%n", "Número de cuenta", acc.getAccountNumber());
        System.out.printf(" %-20s : $ %,.2f%n", "Saldo disponible", acc.getBalance());
        System.out.printf(" %-20s : %s%n", "Estado", acc.getAccountState());
        System.out.printf(" %-20s : %s%n", "Tipo", acc.getAccountType());
        System.out.println("------------------------------------------");
        System.out.printf(" %-20s : %s%n", "Fecha de apertura", acc.getDateOpened());
        System.out.printf(" %-20s : $ %,.2f%n", "Límite sobregiro", acc.getOverdraftLimit());
        System.out.printf(" %-20s : %.1f%%%n", "Porcentaje sobregiro", acc.getOverdraftPercentage());
        System.out.println("==========================================\n");
    }

    private void getAccount() {
        CheckingAccount acc = selectAccount();
        if (acc != null) {
            printAccountDetails(acc);
        }
    }

    private void getAllAccounts() {
        List<CheckingAccount> accounts = checkingAccountService.getAllAccounts(this.currentClientId);
        if (accounts.isEmpty()) System.out.println("⚠️ No hay cuentas registradas.");
        else accounts.forEach(a -> System.out.println(formatAccountForDisplay(a)));
    }

    public void transfer() {
        System.out.println("\n--- TRANSFERENCIA CUENTA CORRIENTE ---");
        CheckingAccount fromAcc = selectAccount();
        if (fromAcc == null) return;

        String toAccount = FormValidationUtil.validateString("Ingrese número de cuenta destino: ");
        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a transferir: ");

        try {
            // 1. Ejecutamos la transferencia en el servicio
            checkingAccountService.transfer(fromAcc.getAccountNumber(), toAccount, amount);

            // 2. Buscamos los datos del destinatario para el mensaje final
            String destClientName = "Desconocido";
            try {
                CheckingAccount destAcc = checkingAccountService.findByAccountNumber(toAccount);
                if (destAcc != null) {
                    destClientName = clientManagement.getClient(destAcc.getClientId()).getFullName();
                }
            } catch (Exception e) {
                // Si no encuentra los datos adicionales, dejamos el nombre por defecto
            }

            // 3. Imprimimos el comprobante detallado y bonito
            System.out.println("\n==================================================");
            System.out.println("         💸 ¡TRANSFERENCIA EXITOSA!               ");
            System.out.println("==================================================");
            System.out.printf(" Origen     : Cuenta %s%n", fromAcc.getAccountNumber());
            System.out.printf(" Destino    : Cuenta %s%n", toAccount);
            System.out.printf(" Beneficiario: %s%n", destClientName);
            System.out.printf(" Monto      : $ %,.2f%n", amount);
            System.out.println("==================================================\n");

        } catch (Exception e) {
            System.out.println("\n⚠️ Error al realizar la transferencia: " + e.getMessage());
        }
    }



    public void showTransactions() {
        try {
            CheckingAccount account = selectAccount();
            if (account == null) return;

            String clientName = clientManagement.getClient(account.getClientId()).getFullName();
            String accountNumber = account.getAccountNumber();
            java.util.List<bank.domain.Transaction> transactions = checkingAccountService.getTransactionsByAccount(accountNumber);

            System.out.println("\n==================================================");
            System.out.println("             📄 REPORTE DE MOVIMIENTOS            ");
            System.out.println("==================================================");
            System.out.println(" Titular: " + clientName);
            System.out.println(" Cuenta : " + accountNumber);
            System.out.println("==================================================");

            if (transactions.isEmpty()) {
                System.out.println("   No se registraron movimientos en esta cuenta.  ");
                System.out.println("==================================================");
                return;
            }

            for (bank.domain.Transaction t : transactions) {
                // --- 1. FORMATEO DE FECHA (Igual que en ahorros) ---
                String fechaOriginal = t.getTimestamp().toString();
                String fechaFormateada = fechaOriginal;
                if (fechaOriginal.contains("T")) {
                    fechaFormateada = fechaOriginal.split("T")[0]; // Nos quedamos solo con YYYY-MM-DD
                }

                // --- 2. DETECCIÓN Y TRADUCCIÓN DEL TIPO ---
                String tipoMovimiento = t.getTransactionType().toString();
                String detallesAdicionales = "";

                if (tipoMovimiento.equals("DEPOSIT")) {
                    tipoMovimiento = "DEPÓSITO";
                } else if (tipoMovimiento.equals("WITHDRAW")) {
                    tipoMovimiento = "RETIRO";
                } else if (tipoMovimiento.equals("TRANSFER_OUT")) {
                    tipoMovimiento = "TRANSFERENCIA ENVIADA";

                    // Extracción con el método que limpia la descripción
                    String cuentaDestino = extractAccountNumber(t.getDescription());
                    if (cuentaDestino != null) {
                        try {
                            CheckingAccount destAcc = checkingAccountService.findByAccountNumber(cuentaDestino);
                            if (destAcc != null) {
                                String destClientName = clientManagement.getClient(destAcc.getClientId()).getFullName();
                                detallesAdicionales = "\n Destino : " + destClientName + " (Cta: " + cuentaDestino + ")";
                            } else {
                                detallesAdicionales = "\n Destino : Cuenta " + cuentaDestino;
                            }
                        } catch (Exception e) {
                            detallesAdicionales = "\n Destino : Cuenta " + cuentaDestino;
                        }
                    }
                } else if (tipoMovimiento.equals("TRANSFER_IN")) {
                    tipoMovimiento = "TRANSFERENCIA RECIBIDA";

                    String cuentaOrigen = extractAccountNumber(t.getDescription());
                    if (cuentaOrigen != null) {
                        try {
                            CheckingAccount origAcc = checkingAccountService.findByAccountNumber(cuentaOrigen);
                            if (origAcc != null) {
                                String origClientName = clientManagement.getClient(origAcc.getClientId()).getFullName();
                                detallesAdicionales = "\n Origen  : " + origClientName + " (Cta: " + cuentaOrigen + ")";
                            } else {
                                detallesAdicionales = "\n Origen  : Cuenta " + cuentaOrigen;
                            }
                        } catch (Exception e) {
                            detallesAdicionales = "\n Origen  : Cuenta " + cuentaOrigen;
                        }
                    }
                }

                // --- 3. IMPRESIÓN CON FORMATO LIMPIO ---
                System.out.println(" Fecha  : " + fechaFormateada);
                System.out.println(" Tipo   : " + tipoMovimiento + detallesAdicionales);
                System.out.printf(" Monto  : $ %,.2f%n", t.getAmount());
                System.out.println("--------------------------------------------------");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al procesar el reporte de movimientos.");
            e.printStackTrace();
        }
    }



    private String extractAccountNumber(String description) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d{6}");
        java.util.regex.Matcher matcher = pattern.matcher(description);
        return matcher.find() ? matcher.group() : null;
    }

    private String formatAccountForDisplay(CheckingAccount account) {
        return "--------------------------------------------------\n" +
                "Número de Cuenta: " + account.getAccountNumber() + "\n" +
                "Saldo: $" + account.getBalance() + "\n" +
                "--------------------------------------------------";
    }
}