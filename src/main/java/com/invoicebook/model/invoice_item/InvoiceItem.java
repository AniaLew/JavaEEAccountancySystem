package com.invoicebook.model.invoice_item;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_items")
    private int numberOfItems;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "vat_amount")
    private BigDecimal vatAmount;

    @JoinColumn(name = "vat")
    @Enumerated(EnumType.STRING)
    private Vat vat = Vat.VAT_23;

    public InvoiceItem(String description, int numberOfItems, BigDecimal amount,
                       BigDecimal vatAmount, Vat vat) {
        this.description = description;
        this.numberOfItems = numberOfItems;
        this.amount = amount;
        this.vatAmount = vatAmount;
        this.vat = vat;
    }

    public InvoiceItem(String description, int numberOfItems, BigDecimal amount, BigDecimal vatAmount) {
        new InvoiceItem(description, numberOfItems, amount, vatAmount, Vat.VAT_23);
    }

    public InvoiceItem() {
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public Vat getVat() {
        return vat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public void setVat(Vat vat) {
        this.vat = vat;
    }

    @Override
    public String toString() {
        return "InvoiceItem{ "
                + "description = " + description + '\''
                + ", numberOfItems = " + numberOfItems
                + ", amount= " + amount
                + ", vatAmount= " + vatAmount
                + ", VAT = " + vat
                + '}';
    }
}
