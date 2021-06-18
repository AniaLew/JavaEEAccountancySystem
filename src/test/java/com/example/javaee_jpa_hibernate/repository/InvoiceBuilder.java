package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;

import java.time.LocalDate;
import java.util.List;

public class InvoiceBuilder {
    private LocalDate date = LocalDate.now();
    private Counterparty counterparty;
    private List<InvoiceItem> invoiceItems;

    public static InvoiceBuilder builder() {
        return new InvoiceBuilder();
    }
    public InvoiceBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public InvoiceBuilder withCounterparty(Counterparty counterparty) {
        this.counterparty = counterparty;
        return this;
    }

    public InvoiceBuilder withInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
        return this;
    }

    public Invoice build() {
        return new Invoice(date, counterparty, invoiceItems);
    }


}
