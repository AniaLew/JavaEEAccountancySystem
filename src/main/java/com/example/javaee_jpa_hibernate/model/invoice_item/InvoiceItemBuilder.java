package com.example.javaee_jpa_hibernate.model.invoice_item;

import java.math.BigDecimal;

public class InvoiceItemBuilder {
    private String description = "";
    private int numberOfItems = 0;
    private BigDecimal amount = new BigDecimal(0);
    private BigDecimal vatAmount = new BigDecimal(0);
    private Vat vat = Vat.VAT_0;

    public static InvoiceItemBuilder builder() {
        return new InvoiceItemBuilder();
    }

    public InvoiceItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    public InvoiceItemBuilder withNumberOfItem(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        return this;
    }
    public InvoiceItemBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public InvoiceItemBuilder withVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
        return this;
    }

    public InvoiceItemBuilder withVat(Vat vat) {
        this.vat = vat;
        return this;
    }

    public InvoiceItem build() {
        return new InvoiceItem(description, numberOfItems, amount, vatAmount, vat);
    }

}
