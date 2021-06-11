package com.example.javaee_jpa_hibernate.model;

import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Invoice implements Serializable, Comparable<Invoice> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "INVOICE_ID")
    private Long id;

    @FutureOrPresent(message = "Data must be in present or future")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    @NotNull(message = "Date cannot be NULL")
    private LocalDate date = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "COUNTERPARTY_ID")
    @NotNull(message = "Counterparty cannot be NULL")
    private Counterparty counterparty;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems;

    public Invoice() {
    }
    
    public Invoice(LocalDate date, Counterparty counterparty,
                    List<InvoiceItem> itemList) {
        this.date = date;
        this.counterparty = counterparty;
        this.invoiceItems = itemList;

        for (InvoiceItem item: invoiceItems){
            item.setInvoice(this);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Counterparty getCounterparty() {
        return counterparty;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Invoice)) {
            return false;
        }
        Invoice invoice = (Invoice) obj;

        if (getId() != invoice.getId()) {
            return false;
        }
        if (!getDate().equals(invoice.getDate())) {
            return false;
        }
        return getCounterparty().equals(invoice.getCounterparty());
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(getId());
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getCounterparty().hashCode();
        return result;
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
        return "Invoice{"
                + "id=" + id
                + ", date=" + date
                + ", counterparty='" + counterparty + '\''
                + ", invoiceItems=" + invoiceItems
                + '}';
    }
}

