package com.example.javaee_jpa_hibernate.model;

import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;

import java.time.LocalDate;
import java.util.List;

public class InvoiceBody {

        private LocalDate date = LocalDate.now();
        private Counterparty counterparty;
        private List<InvoiceItem> invoiceItems;

        public InvoiceBody() {
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
