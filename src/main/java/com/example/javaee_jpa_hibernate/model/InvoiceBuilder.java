package com.example.javaee_jpa_hibernate.model;

import com.example.javaee_jpa_hibernate.model.counterparty.AddressBuilder;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.counterparty.CounterpartyBuilder;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItemBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvoiceBuilder {
//    private Long id;
    private LocalDate date = LocalDate.now();
    private Counterparty counterparty ;
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

    public InvoiceBuilder withInvoiceItems(InvoiceItemBuilder... invoiceItemBuilders) {
        this.invoiceItems = new ArrayList<>();
        for (InvoiceItemBuilder invoiceItemBuilder : invoiceItemBuilders) {
            this.invoiceItems.add(invoiceItemBuilder.build());
        }
        return this;
    }

    // unused !
//    public InvoiceBuilder withId(Long id) {
//        this.id = id;
//        return this;
//    }

    public Invoice build() {
        return new Invoice(date, counterparty, invoiceItems);
    }


}

