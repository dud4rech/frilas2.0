package org.project.enums;

public enum UserType {
    FREELANCER(1),
    HIRER(2);

    private final int value;

    UserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
