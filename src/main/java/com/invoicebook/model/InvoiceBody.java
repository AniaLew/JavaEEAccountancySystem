package com.invoicebook.model;

import com.invoicebook.model.counterparty.Counterparty;
import com.invoicebook.model.invoice_item.InvoiceItem;

import javax.json.bind.annotation.JsonbDateFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class InvoiceBody implements Serializable {

        @JsonbDateFormat(value = "yyyy-MM-dd")
        private LocalDate date;
        private Counterparty counterparty;
        private List<InvoiceItem> invoiceItems;

        public InvoiceBody() {
        }

        public InvoiceBody(Counterparty counterparty, List<InvoiceItem> invoiceItems) {
            new InvoiceBody(LocalDate.now(), counterparty, invoiceItems);
        }

    public InvoiceBody(LocalDate date, Counterparty counterparty, List<InvoiceItem> invoiceItems) {
        this.date = date;
        this.counterparty = counterparty;
        this.invoiceItems = invoiceItems;
    }

    public LocalDate getDate() {
            return date;
        }

        public Counterparty getCounterparty() {
            return counterparty;
        }

        public List<InvoiceItem> getInvoiceItems() {
            return invoiceItems;
        }
}
