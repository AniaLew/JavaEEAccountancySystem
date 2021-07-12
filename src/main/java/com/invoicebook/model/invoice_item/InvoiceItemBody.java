package com.invoicebook.model.invoice_item;

import java.io.Serializable;
import java.math.BigDecimal;

public class InvoiceItemBody implements Serializable {
    private String description;
    private int numberOfItems;
    private BigDecimal amount;
    private BigDecimal vatAmount;
    private Vat vat;

    public InvoiceItemBody(String description, int numberOfItems, BigDecimal amount, BigDecimal vatAmount, Vat vat) {
        this.description = description;
        this.numberOfItems = numberOfItems;
        this.amount = amount;
        this.vatAmount = vatAmount;
        this.vat = vat;
    }

    public InvoiceItemBody() {
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public Vat getVat() {
        return vat;
    }

    @Override
    public String toString() {
        return "InvoiceItemBody{" +
                "description='" + description + '\'' +
                ", numberOfItems=" + numberOfItems +
                ", amount=" + amount +
                ", vatAmount=" + vatAmount +
                ", vat='" + vat + '\'' +
                '}';
    }
}
