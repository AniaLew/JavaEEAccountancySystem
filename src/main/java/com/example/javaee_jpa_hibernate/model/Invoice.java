package com.example.javaee_jpa_hibernate.model;

import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "invoices")
public class Invoice implements Serializable, Comparable<Invoice>, Comparator<Invoice> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "invoice_id")
    private Long id;

    @FutureOrPresent(message = "Data must be in present or future")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    @NotNull(message = "Date cannot be NULL")
    private LocalDate date = LocalDate.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Counterparty counterparty;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "invoice_items", nullable = false)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> invoiceItems;

    public Invoice() {
    }
    
    public Invoice(LocalDate date, Counterparty counterparty,
                    List<InvoiceItem> invoiceItems) {
        this.date = date;
        this.counterparty = counterparty;
        this.invoiceItems = invoiceItems;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
            comparison = (this.date.getMonthValue() - object.date.getMonthValue());
            if (comparison == 0) {
                comparison = (this.date.getDayOfMonth() - object.date.getDayOfMonth());
            }
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

