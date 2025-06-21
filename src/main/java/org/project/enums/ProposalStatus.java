package org.project.enums;

public enum ProposalStatus {
    CREATED(1, "Created"),
    ACCEPTED(2, "Accepted");

    private final int value;
    private final String description;

    ProposalStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ProposalStatus fromValue(int value) {
        for (ProposalStatus status : ProposalStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}
