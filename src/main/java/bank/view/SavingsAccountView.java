package bank.view;

import bank.services.inputs.IClientManagement;
import bank.domain.SavingsAccount;
import bank.services.inputs.SavingsAccountService;
import bank.utils.FormValidationUtil;

import java.math.BigDecimal;
import java.util.List;

public class SavingsAccountView {
    private final SavingsAccountService savingsAccountService;
    private final IClientManagement clientManagement;

    // 🌟 Variable de sesión para saber qué usuario está operando
    private int currentClientId;

    public SavingsAccountView(SavingsAccountService savingsAccountService, IClientManagement clientManagement) {
        this.savingsAccountService = savingsAccountService;
        this.clientManagement = clientManagement;
    }

    // 🌟 Método clave para recibir el ID del cliente al entrar al menú
    public void setCurrentClientId(int currentClientId) {
        this.currentClientId = currentClientId;
    }

    // 🌟 SELECTOR INTELIGENTE AUTOMÁTICO (Igual que en Cuentas Corrientes)
    private SavingsAccount selectAccount() {
        // CORREGIDO: Llamamos a findByClientId pasando el ID del cliente logueado
        List<SavingsAccount> accounts = savingsAccountService.findByClientId(this.currentClientId);

        if (accounts.isEmpty()) {
            System.out.println("⚠️ No tienes cuentas de ahorros registradas.");
            return null;
        }

        // Si el cliente tiene una sola cuenta, se selecciona automáticamente
        if (accounts.size() == 1) {
            return accounts.get(0);
        }

        // Si tiene múltiples cuentas de ahorro, despliega el menú de selección
        System.out.println("\nSeleccione una cuenta de ahorros:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i).getAccountNumber());
        }

        int index = FormValidationUtil.validateInt("Opción: ") - 1;
        if (index >= 0 && index < accounts.size()) {
            return accounts.get(index);
        } else {
            System.out.println("⚠️ Opción inválida.");
            return null;
        }
    }

    // Opcional: Auxiliar regex que usas en el proyecto para extraer números de cuenta
    private String extractAccountNumber(String text) {
        if (text == null) return null;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d{6,}").matcher(text);
        return m.find() ? m.group() : null;
    }

    // 1. CREAR CUENTA AUTOMATIZADA (Usa el ID del login de forma interna y segura)
    public void createAccount() {
        System.out.println("\n--- CREAR NUEVA CUENTA DE AHORROS ---");

        // Generador automático de cuenta aleatoria de 6 dígitos
        java.util.Random random = new java.util.Random();
        int numeroAleatorio = 100000 + random.nextInt(900000);
        String accountNumber = String.valueOf(numeroAleatorio);

        // Solo le pedimos el saldo inicial
        BigDecimal initialBalance = FormValidationUtil.validateBigDecimal("Ingrese saldo inicial para la apertura: ");

        try {
            // 🌟 CORREGIDO: Usamos estrictamente 'this.currentClientId' que viene desde el Main Menu
            SavingsAccount newAccount = savingsAccountService.createAccount(accountNumber, initialBalance, this.currentClientId);

            System.out.println("\n==================================================");
            System.out.println("     🎉 ¡CUENTA DE AHORROS CREADA CON ÉXITO!     ");
            System.out.println("==================================================");
            System.out.println(" Cuenta Nro : " + newAccount.getAccountNumber() + " [ASIGNADA AUTOMÁTICAMENTE]");
            System.out.printf(" Saldo Base : $ %,.2f%n", initialBalance);
            System.out.println("==================================================\n");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    // 2. DEPOSITAR AUTOMATIZADO
    public void deposit() {
        SavingsAccount acc = selectAccount();
        if (acc == null) return;

        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a depositar: ");
        try {
            savingsAccountService.deposit(acc.getAccountNumber(), amount);
            System.out.println("\n✅ Depósito realizado correctamente.");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    // 3. RETIRAR AUTOMATIZADO
    public void withdraw() {
        SavingsAccount acc = selectAccount();
        if (acc == null) return;

        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a retirar: ");
        try {
            savingsAccountService.withdraw(acc.getAccountNumber(), amount);
            System.out.println("\n✅ Retiro realizado correctamente.");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    // 4. APLICAR INTERESES AUTOMATIZADO
    public void applyInterest() {
        SavingsAccount acc = selectAccount();
        if (acc == null) return;

        try {
            savingsAccountService.applyInterest(acc.getAccountNumber());
            System.out.println("\n📈 Intereses aplicados correctamente a la cuenta " + acc.getAccountNumber());
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    // 5. CONSULTAR SALDO CON DISEÑO PROFESIONAL
    public void showBalance() {
        SavingsAccount acc = selectAccount();
        if (acc == null) return;

        try {
            // Volvemos a consultar para tener el valor actualizado en tiempo real
            SavingsAccount updatedAcc = savingsAccountService.getAccount(acc.getAccountNumber());
            System.out.println("\n==========================================");
            System.out.println("          DETALLES DE TU CUENTA           ");
            System.out.println("==========================================");
            System.out.printf(" %-20s : %s%n", "Número de cuenta", updatedAcc.getAccountNumber());
            System.out.printf(" %-20s : $ %,.2f%n", "Saldo disponible", updatedAcc.getBalance());
            System.out.printf(" %-20s : %s%n", "Estado", updatedAcc.getAccountState());
            System.out.printf(" %-20s : %s%n", "Tipo de cuenta", updatedAcc.getAccountType());
            System.out.println("==========================================\n");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    // 6. REPORTE DE MOVIMIENTOS EN ESPAÑOL, FECHAS LIMPIAS Y CRUCE DE NOMBRES
    public void showTransactions() {
        SavingsAccount acc = selectAccount();
        if (acc == null) return;

        try {
            String clientName = clientManagement.getClient(this.currentClientId).getFullName();
            String accountNumber = acc.getAccountNumber();
            List<bank.domain.Transaction> transactions = savingsAccountService.getTransactionsByAccount(accountNumber);

            System.out.println("\n==================================================");
            System.out.println("             📄 REPORTE DE MOVIMIENTOS            ");
            System.out.println("==================================================");
            System.out.println(" Titular: " + clientName);
            System.out.println(" Cuenta : " + accountNumber);
            System.out.println("==================================================");

            if (transactions == null || transactions.isEmpty()) {
                System.out.println("   No se registraron movimientos en esta cuenta.  ");
                System.out.println("==================================================");
                return;
            }

            for (bank.domain.Transaction t : transactions) {
                // --- Limpieza estética de fecha (Eliminamos la T y la hora) ---
                String fechaOriginal = t.getTimestamp() != null ? t.getTimestamp().toString() : "";
                String fechaFormateada = fechaOriginal.contains("T") ? fechaOriginal.split("T")[0] : fechaOriginal;

                String tipoMovimiento = t.getTransactionType().toString();
                String detallesAdicionales = "";

                if (tipoMovimiento.equals("DEPOSIT")) {
                    tipoMovimiento = "DEPÓSITO";
                } else if (tipoMovimiento.equals("WITHDRAW")) {
                    tipoMovimiento = "RETIRO";
                } else if (tipoMovimiento.equals("TRANSFER_OUT")) {
                    tipoMovimiento = "TRANSFERENCIA ENVIADA";
                    String cuentaDestino = extractAccountNumber(t.getDescription());
                    if (cuentaDestino != null) {
                        try {
                            SavingsAccount destAcc = savingsAccountService.getAccount(cuentaDestino);
                            if (destAcc != null) {
                                String destClientName = clientManagement.getClient(destAcc.getClientId()).getFullName();
                                detallesAdicionales = "\n Destino : " + destClientName + " (Cta: " + cuentaDestino + ")";
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
                            SavingsAccount origAcc = savingsAccountService.getAccount(cuentaOrigen);
                            if (origAcc != null) {
                                String origClientName = clientManagement.getClient(origAcc.getClientId()).getFullName();
                                detallesAdicionales = "\n Origen  : " + origClientName + " (Cta: " + cuentaOrigen + ")";
                            }
                        } catch (Exception e) {
                            detallesAdicionales = "\n Origen  : Cuenta " + cuentaOrigen;
                        }
                    }
                }

                System.out.println(" Fecha  : " + fechaFormateada);
                System.out.println(" Tipo   : " + tipoMovimiento + detallesAdicionales);
                System.out.printf(" Monto  : $ %,.2f%n", t.getAmount());
                System.out.println("--------------------------------------------------");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al procesar el reporte de movimientos.");
        }
    }

    // 7. TRANSFERENCIA AUTOMATIZADA CON RECIBO DE COMPROBACIÓN COMPLETO
    public void transfer() {
        System.out.println("\n--- TRANSFERENCIA CUENTA DE AHORROS ---");
        SavingsAccount fromAcc = selectAccount();
        if (fromAcc == null) return;

        String toAccount = FormValidationUtil.validateString("Ingrese número de cuenta destino: ");
        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a transferir: ");

        try {
            savingsAccountService.transfer(fromAcc.getAccountNumber(), toAccount, amount);

            // Intentamos buscar dinámicamente quién es el titular de la cuenta destino
            String destClientName = "Desconocido";
            try {
                SavingsAccount destAcc = savingsAccountService.getAccount(toAccount);
                if (destAcc != null) {
                    destClientName = clientManagement.getClient(destAcc.getClientId()).getFullName();
                }
            } catch (Exception e) {
                // Si la cuenta destino no pertenece a ahorros o no se encuentra, se queda por defecto
            }

            System.out.println("\n==================================================");
            System.out.println("         💸 ¡TRANSFERENCIA EXITOSA!               ");
            System.out.println("==================================================");
            System.out.printf(" Origen     : Cuenta de Ahorros %s%n", fromAcc.getAccountNumber());
            System.out.printf(" Destino    : Cuenta %s%n", toAccount);
            System.out.printf(" Beneficiario: %s%n", destClientName);
            System.out.printf(" Monto      : $ %,.2f%n", amount);
            System.out.println("==================================================\n");

        } catch (Exception e) {
            System.out.println("\n⚠️ Error al realizar la transferencia: " + e.getMessage());
        }
    }
}