package com.example.javaee_jpa_hibernate.services;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;

import javax.persistence.*;
import java.util.List;
import java.util.NoSuchElementException;

public class InvoiceService implements ServiceableInvoice {
    @PersistenceContext
    private EntityManager entityManager;

    public InvoiceService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void createInvoice(InvoiceBody invoiceBody) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(new Invoice(invoiceBody.getDate(),
                    invoiceBody.getCounterparty(), invoiceBody.getInvoiceItems()));
            entityManager.getTransaction().commit();
        } catch (EntityExistsException exception) {
           throw new EntityExistsException("The entity already exists.");
        } catch (IllegalArgumentException exception) {
            throw  new IllegalArgumentException("The instance is not an entity.");
        } catch (TransactionRequiredException exception) {
            throw new TransactionRequiredException("There is no transaction.");
        } catch (Exception exception) {
            exception.getStackTrace();
            exception.getMessage();
        }
        finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public Invoice findInvoiceById(Long id) {
        String query = "SELECT i FROM Invoice i WHERE i.id = :invoiceId";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, Invoice.class);
        System.out.println("typedQuery: " + typedQuery.toString());
        TypedQuery<Invoice> t = typedQuery.setParameter("invoiceId", id);
        System.out.println("Set parameter = " + t.toString());
        Invoice invoice = null;
        try {
            invoice = typedQuery.getSingleResult();
        } catch (NoResultException exception) {
            throw new NoResultException("The invoice has not been found.");
        } catch (PersistenceException exception) {
            throw new PersistenceException("Problems persisting the invoice.");
        } catch (Exception exception) {
            exception.getStackTrace();
            exception.getMessage();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return invoice;
    }

    @Override
    public List<Invoice> getAllInvoices() {
        String query = "SELECT i FROM Invoice i WHERE i.id IS NOT NULL";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, Invoice.class);
        List<Invoice> invoices = null;
        try {
            invoices = typedQuery.getResultList();
        } catch (NoSuchElementException exception) {
            System.out.println("The invoices were not found.");
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return invoices;
    }

    @Override
    public void updateInvoice(Long id, InvoiceBody invoiceBody) {
        try {
            Invoice invoice = entityManager.find(Invoice.class, id);
            if (entityManager.contains(invoice)) {
                invoice.setDate(invoiceBody.getDate());
                invoice.setCounterparty(invoiceBody.getCounterparty());
//        Todo: If the list as an object is merged into the existing record we get:
//         Exception in thread "main" java.lang.UnsupportedOperationException
//        invoice.setInvoiceItems(invoiceBody.getInvoiceItems());
                entityManager.getTransaction().begin();
                entityManager.persist(invoice);
                entityManager.getTransaction().commit();
            }
        } catch (NoSuchElementException exception) {
            System.out.println("The invoice was not found.");
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public void deleteInvoice(Long id) {
        try {
            entityManager.getTransaction().begin();
            Invoice invoice = entityManager.find(Invoice.class, id);
            System.out.println("Invoice found: "+ invoice.toString());
            if(entityManager.contains(invoice)) {
                entityManager.remove(invoice);
                entityManager.getTransaction().commit();
            }
        } catch (Exception exception) {
            System.out.println("Deleting the invoice failed: " + exception.getStackTrace().toString());
        }
    }

    public Invoice findInvoiceByCounterparty(String counterparty) {
        String query = "SELECT i FROM Invoice i WHERE i.counterparty = :companyName";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, Invoice.class);
        typedQuery.setParameter("companyName", counterparty);
        Invoice invoice = null;
        try {
            invoice = typedQuery.getSingleResult();
        } catch (NoSuchElementException exception) {
            System.out.println("The invoice was not found.");
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return invoice;
    }
}
