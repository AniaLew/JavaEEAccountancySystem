package com.example.javaee_jpa_hibernate.services;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;


public class InvoiceService {
    @PersistenceContext
    private EntityManager entityManager;

    public InvoiceService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean saveInvoice(InvoiceBody invoiceBody) {
        try {
            entityManager.getTransaction().begin();
            Invoice invoice = new Invoice(invoiceBody.getDate(),
                    invoiceBody.getCounterparty(), invoiceBody.getInvoiceItems());
            entityManager.persist(invoice);
            entityManager.getTransaction().commit();
        } catch (Exception exception) {
            System.out.println("Saving the invoice failed: " + exception.getStackTrace());
            return false;
        }
        return true;
    }

    public Invoice getInvoice(Long id) {
        String query = "SELECT i FROM Invoice i WHERE i.id = :invoiceId";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, Invoice.class);
        typedQuery.setParameter("invoiceId", id);
        Invoice invoice = null;
        try {
            invoice = typedQuery.getSingleResult();
        } catch (NoResultException exception) {
            System.out.println(exception.getStackTrace());
        }
        return invoice;
    }

    public List<Invoice> getInvoices() {
        String query = "SELECT i FROM Invoice i WHERE i.id IS NOT NULL";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, Invoice.class);

        List<Invoice> invoices = null;
        try {
            invoices = typedQuery.getResultList();
        } catch (NoResultException exception) {
            System.out.println(exception.getStackTrace());
        }
        return invoices;
    }

    public Invoice changeInvoice(Long id, InvoiceBody invoiceBody) {
        Invoice invoice = null;
        try {
            entityManager.getTransaction().begin();
            invoice = entityManager.find(Invoice.class, id);
            invoice.setDate(invoiceBody.getDate());
            invoice.setCounterparty(invoiceBody.getCounterparty());
            invoice.setInvoiceItems(invoiceBody.getInvoiceItems());
            entityManager.persist(invoice);
            entityManager.getTransaction().commit();
        } catch (Exception exception) {
            System.out.println("Changing the invoice failed: " + exception.getStackTrace());
        }
        return invoice;
    }

    public Invoice deleteInvoice(Long id) {
        Invoice invoice = null;
        try {
            entityManager.getTransaction().begin();
            invoice = entityManager.find(Invoice.class, id);
            entityManager.remove(invoice);
            entityManager.getTransaction().commit();
        } catch (Exception exception) {
            System.out.println("Deleting the invoice failed: " + exception.getStackTrace());
        }
        return invoice;
    }

}
