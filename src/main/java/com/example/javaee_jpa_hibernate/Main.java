package com.example.javaee_jpa_hibernate;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.Vat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Address address = new Address("97-420", "Lodz", "Piotrkowska", "34");

        Counterparty counterparty =  new Counterparty("LPT", address, "345678912",
                "123456789", "PKO", "234");

        List<InvoiceItem> invoiceItems = Arrays.asList(
                new InvoiceItem("Item1", 1, BigDecimal.TEN, BigDecimal.ONE, Vat.VAT_23));

        Invoice invoice = new Invoice(LocalDate.now(), counterparty, invoiceItems);

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("Invoices");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(counterparty);
            entityManager.persist(invoice);
            transaction.commit();
        } finally {
            if(transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}
