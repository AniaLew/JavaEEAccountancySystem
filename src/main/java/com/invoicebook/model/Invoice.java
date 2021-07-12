package com.invoicebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.invoicebook.model.counterparty.Counterparty;
import com.invoicebook.model.invoice_item.InvoiceItem;
import com.invoicebook.model.invoice_item.InvoiceItemBody;

import javax.json.bind.JsonbBuilder;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "invoices")
public class Invoice implements Serializable, Comparable<Invoice>, Comparator<Invoice>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Counterparty counterparty;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column(name = "invoice_items")
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> invoiceItems;

    public Invoice() {
    }
    
    public Invoice(Date date, Counterparty counterparty,
                    List<InvoiceItem> invoiceItems) {
        this.date = date;
        this.counterparty = counterparty;
        this.invoiceItems = invoiceItems;
    }

    public Invoice(InvoiceBody invoiceBody) {
        this.date = invoiceBody.getDate();
        this.counterparty = new Counterparty(invoiceBody.getCounterpartyBody());
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for(InvoiceItemBody invoiceItemBody: invoiceBody.getInvoiceItemBodies()) {
            invoiceItems.add(new InvoiceItem(invoiceItemBody));
        }
        this.invoiceItems = invoiceItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Counterparty getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(Counterparty counterparty) {
        this.counterparty = counterparty;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    @Override
    public int compareTo(Invoice object) {
        if (object == null) {
            return -1;
        }
        if (this == null) {
            return -1;
        }
        int comparison = (this.date.getYear() - object.date.getYear());
        if (comparison == 0) {
            comparison = (this.date.compareTo(object.date));
            }
        if (comparison < 0) {
            return -1;
        }
        if (comparison > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", date=" + date +
                ", counterparty='" + counterparty + '\'' +
                ", invoiceItems=" + invoiceItems +
                '}';
    }

    @Override
    public int compare(Invoice invoice1, Invoice invoice2) {
        return  invoice1.getCounterparty().getCompanyName().compareTo(invoice2.getCounterparty().getCompanyName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        Invoice invoice = (Invoice) o;
        return getDate().equals(invoice.getDate())
                && getCounterparty().equals(invoice.getCounterparty())
                && getInvoiceItems().equals(invoice.getInvoiceItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getCounterparty(), getInvoiceItems());
    }
}

