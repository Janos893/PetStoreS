package com.example.sandbox.util.swagger.definitions;

import lombok.Getter;

public enum Status {
    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");

    @Getter
    private String value;

    Status(String value) {
        this.value = value;
    }


}
