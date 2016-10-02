package com.tide.domain.enums;

public enum TRANSACTION_TYPE {
    DIRECT_DEBIT("direct debit"),
    CREDIT_CARD("credit card"),
    CHEQUE("cheque"),
    WITHDRAWAL("withdrawal"),
    DEPOSIT("deposit"),
    TRANSFER("transfer");

    private final String name;

    TRANSACTION_TYPE(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
