package com.example.javaee_jpa_hibernate.model.invoice_item;

import com.example.javaee_jpa_hibernate.model.Invoice;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ITEM_ID")
    private int id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Min(value = 1, message = "There need to be at least one item" )
    @Column(name = "NUMBER_OF_ITEMS")
    private int numberOfItems;

    @Column(name = "AMOUNT")
    private BigDecimal amount = new BigDecimal(0);

    @Column(name = "VAT_AMOUNT")
    private BigDecimal vatAmount = new BigDecimal(0);

    @JoinColumn(name = "VAT", referencedColumnName = "VAT_CODE")
    @Enumerated(EnumType.ORDINAL)
    private Vat vat = Vat.VAT_23;

    @ManyToOne
//            (cascade = {CascadeType.DETACH,
//            CascadeType.MERGE, CascadeType.PERSIST,
//            CascadeType.REFRESH})
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

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
