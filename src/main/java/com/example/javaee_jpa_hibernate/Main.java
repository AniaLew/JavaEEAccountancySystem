package com.example.javaee_jpa_hibernate;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.Vat;
import com.example.javaee_jpa_hibernate.repository.InvoiceRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        EntityManagerFactory entityManagerFactory= Persistence.createEntityManagerFactory("Invoices");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        InvoiceRepository invoiceRepository = new InvoiceRepository(entityManager);
//
//        List<InvoiceItem> invoiceItems1 = Arrays.asList(
//                new InvoiceItem("Bread",2, BigDecimal.valueOf(100),BigDecimal.valueOf(23), Vat.VAT_23),
//                new InvoiceItem("Milk", 1,BigDecimal.valueOf(50), BigDecimal.valueOf(11.5), Vat.VAT_23));
//
//        Address address1 = new Address("44-234", "Lodz", "StreetA", "673");
//
//        Counterparty counterparty1 =  new Counterparty("Company no 1", address1, "696875432",
//                "0123456089", "Bank1", "bank-1234561");
//        Invoice invoice = new Invoice(LocalDate.now(), counterparty1, invoiceItems1);
//
//        try {
//            entityManager.persist(invoice);
////            System.out.println("Created: " + createdInvoice);
//        }
//        finally {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//        }
//
//
//
//        entityManager.close();
//        entityManagerFactory.close();

    }

}