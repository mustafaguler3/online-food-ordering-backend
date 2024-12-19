package com.example.food_ordering.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AddressType {
    HOME("Home"),
    WORK("Work");

    private final String value;

    AddressType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AddressType fromValue(String value) {
        for (AddressType type : AddressType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for AddressType: " + value);
    }
}
