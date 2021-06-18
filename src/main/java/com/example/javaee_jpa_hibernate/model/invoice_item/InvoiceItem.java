package com.example.javaee_jpa_hibernate.model.invoice_item;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private int id;

    @Column(name = "description", nullable = false, columnDefinition = "varchar(255) default 'Bread'")
    private String description;

    @Column(name = "number_of_items", nullable = false, columnDefinition = "integer default 1")
    private int numberOfItems;

    @Min(value = 0)
    @Column(name = "amount", nullable = false)
    private BigDecimal amount = new BigDecimal(0);

    @Column(name = "vat_amount")
    private BigDecimal vatAmount = new BigDecimal(0);

    @JoinColumn(name = "vat", referencedColumnName = "VAT_CODE")
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
