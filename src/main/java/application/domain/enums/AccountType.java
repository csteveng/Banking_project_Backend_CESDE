package application.domain.enums;

public enum AccountType {
    SAVINGS("Cuenta de Ahorros"),
    CHECKING("Cuenta Corriente"),
    CREDIT_CARD("Tarjeta de Crédito");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}