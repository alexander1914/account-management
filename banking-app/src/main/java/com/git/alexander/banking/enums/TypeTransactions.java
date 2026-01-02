package com.git.alexander.banking.enums;

import lombok.Getter;

@Getter
public enum TypeTransactions {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),
    TRANSFER("TRANSFER");

    private final String description;

    TypeTransactions(String description) {
        this.description = description;
    }
}
