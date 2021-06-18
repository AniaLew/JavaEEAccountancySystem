package com.example.javaee_jpa_hibernate.factory;

import com.example.javaee_jpa_hibernate.model.Invoice;

public class InvoiceElement implements InvoiceObject{
    private Invoice invoice;

    public InvoiceElement() {
    }

    @Override
    public Invoice getObject() {
        return new Invoice();
    }
}
