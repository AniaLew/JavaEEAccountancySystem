package com.example.javaee_jpa_hibernate;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBuilder;
import com.example.javaee_jpa_hibernate.model.counterparty.AddressBuilder;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.counterparty.CounterpartyBuilder;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItemBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Counterparty counterparty = CounterpartyBuilder.builder()
                .withCompanyName("LPT")
                .withAddress(AddressBuilder.builder()
                        .withZipCode("97-420")
                        .withTownName("Lodz")
                        .withStreetName("Piotrkowska")
                        .withHouseNumber("34")
                        .build())
                .withPhoneNumber("345678912")
                .withNIP("")
                .withBankName("PKO")
                .withBankNumber("2345")
                .build();
        Invoice invoice = InvoiceBuilder.builder()
//                .withId(id)   unused property!
                .withDate(LocalDate.now())
                .withCounterparty(counterparty)
                .withInvoiceItems(Arrays.asList(
                        InvoiceItemBuilder.builder()
                                .withDescription("Item1")
                                .withAmount(BigDecimal.TEN)
                                .withVatAmount(BigDecimal.ONE)
                                .build()))
                .build();


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
