package org.project.enums;

public enum ProjectStatus {
    START(1, "Start"),
    ONGOING(2, "Ongoing"),
    FINISHED(3, "Finished");

    private final int value;
    private final String description;

    ProjectStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ProjectStatus fromValue(int value) {
        for (ProjectStatus status : ProjectStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}