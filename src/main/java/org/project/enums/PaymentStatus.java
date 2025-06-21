package org.project.enums;

public enum PaymentStatus {
    PENDING(1, "Pending"),
    PAYED(2, "Payed") ;

    private final int value;
    private final String description;

    PaymentStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentStatus fromValue(int value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}