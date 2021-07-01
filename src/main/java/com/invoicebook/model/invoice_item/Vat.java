package com.invoicebook.model.invoice_item;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum Vat implements Serializable {
    VAT_23(23, "vat_23"),
    VAT_8(8, "vat_8"),
    VAT_5(5, "vat_5"),
    VAT_0(0, "vat_0");

    private final int vatValue;
    private final String name;

    Vat(int vatCode, String name) {
        this.vatValue = vatCode;
        this.name = name;
    }

    public int getVatValue() {
        return vatValue;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}