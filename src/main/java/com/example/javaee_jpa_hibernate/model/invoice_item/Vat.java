package com.example.javaee_jpa_hibernate.model.invoice_item;

import java.util.stream.Stream;

public enum Vat {
    VAT_23(23),
    VAT_8(8),
    VAT_5(5),
    VAT_0(0);

    private final int vatValue;

    Vat(int vatCode) {
        this.vatValue = vatCode;
    }

    public int getVatValue() {
        return vatValue;
    }

    public static Vat ofVatValue(int  vatValue) {
        return Stream.of(Vat.values())
                .filter(v -> v.getVatValue() ==  vatValue)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}