package com.invoicebook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.invoicebook.model.counterparty.CounterpartyBody;
import com.invoicebook.model.invoice_item.InvoiceItemBody;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InvoiceBody implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private CounterpartyBody counterpartyBody;
    private List<InvoiceItemBody> invoiceItemBodies;

    public InvoiceBody(Date date, CounterpartyBody counterpartyBody, List<InvoiceItemBody> invoiceItemBodies) {
        this.date = date;
        this.counterpartyBody = counterpartyBody;
        this.invoiceItemBodies = invoiceItemBodies;
    }

    public InvoiceBody() {
    }

    public Date getDate() {
        return date;
    }

    public CounterpartyBody getCounterpartyBody() {
        return counterpartyBody;
    }

    @JsonBackReference
    public List<InvoiceItemBody> getInvoiceItemBodies() {
        return invoiceItemBodies;
    }

    public InvoiceBody setDate(Date date) {
        this.date = date;
        return this;
    }

    public InvoiceBody setCounterpartyBody(CounterpartyBody counterpartyBody) {
        this.counterpartyBody = counterpartyBody;
        return this;
    }

    public InvoiceBody setInvoiceItemBodies(List<InvoiceItemBody> invoiceItemBodies) {
        this.invoiceItemBodies = invoiceItemBodies;
        return this;
    }

    public InvoiceBody getInvoiceBody() {
        return this;
    }

    @Override
    public String toString() {
        return "InvoiceBody{" +
                "date=" + date +
                ", counterpartyBody=" + counterpartyBody +
                ", invoiceItemBodies=" + invoiceItemBodies +
                '}';
    }
}
