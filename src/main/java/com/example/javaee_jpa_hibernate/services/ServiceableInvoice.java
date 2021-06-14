package com.example.javaee_jpa_hibernate.services;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;

import java.util.List;

public interface ServiceableInvoice {
    void createInvoice(InvoiceBody invoiceBody);
    Invoice findInvoiceById(Long id);
    List<Invoice> getAllInvoices();
    void updateInvoice(Long id, InvoiceBody invoiceBody);
    void  deleteInvoice(Long id);
}
