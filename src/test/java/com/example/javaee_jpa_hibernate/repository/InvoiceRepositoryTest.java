package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceRepositoryTest {

    @Test
    public void shouldCreateAnInvoice() throws Exception {
        //given
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Invoices");
        InvoiceRepository repository = new InvoiceRepository(entityManagerFactory);
        Invoice invoice = InvoiceGenerator.getAnInvoice(LocalDate.now(), "Dummy Company", "Dummy City", Arrays.asList("Item one", "Item two"));
        repository.getEntityManager().getTransaction().begin();
        //when
        Invoice expectedInvoice = repository.create(invoice);
        repository.getEntityManager().getTransaction().commit();
        //then
        assertNotNull(repository);
        assertEquals(invoice.getId(), expectedInvoice.getId());
        Assertions.assertSame(invoice, expectedInvoice);
        repository.getEntityManager().close();
        entityManagerFactory.close();
    }

    @Test
    public void shouldFindAnInvoiceById() throws Exception {
        //given
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Invoices");
        InvoiceRepository repository = new InvoiceRepository(entityManagerFactory);
        Invoice invoice = InvoiceGenerator.getAnInvoice(LocalDate.now(), "Dummy Company", "Dummy City", Arrays.asList("Item one", "Item two"));
        repository.getEntityManager().getTransaction().begin();
        repository.create(invoice);
        repository.getEntityManager().getTransaction().commit();
        //when
        Invoice expectedInvoice = repository.findById(invoice.getId());
        //then
        assertEquals(invoice.getId(), expectedInvoice.getId());
        Assertions.assertSame(invoice, expectedInvoice);
        repository.getEntityManager().close();
        entityManagerFactory.close();
    }

    @Test
    public void shouldFindAllInvoices() throws Exception {
        //given
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Invoices");
        InvoiceRepository repository = new InvoiceRepository(entityManagerFactory);
        List<Invoice> invoices = InvoiceGenerator.getManyInvoices(5);
        repository.getEntityManager().getTransaction().begin();
        for (Invoice invoice : invoices) {
            repository.create(invoice);
        }
        repository.getEntityManager().flush();
        repository.getEntityManager().getTransaction().commit();
        //when
        List<Invoice> expectedInvoices = repository.findAll();
        //then
        assertEquals(invoices.size(), expectedInvoices.size());
        List<Invoice> sortedInvoices = invoices.stream().sorted().collect(Collectors.toList());
        List<Invoice> sortedExpectedInvoices = expectedInvoices.stream().sorted().collect(Collectors.toList());
        assertArrayEquals(sortedInvoices.toArray(), sortedExpectedInvoices.toArray());
        repository.getEntityManager().close();
        entityManagerFactory.close();
    }

    @Test
    public void shouldDeleteAnInvoiceById() throws Exception {
        //given
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Invoices");
        InvoiceRepository repository = new InvoiceRepository(entityManagerFactory);
        List<Invoice> invoices = InvoiceGenerator.getManyInvoices(5);
        try {
            repository.getEntityManager().getTransaction().begin();
            for (Invoice invoice : invoices) {
                repository.create(invoice);
            }
            repository.getEntityManager().getTransaction().commit();
        } finally {
            if(repository.getEntityManager().getTransaction().isActive()) {
                repository.getEntityManager().getTransaction().rollback();
            }
        }
        try {
            repository.getEntityManager().getTransaction().begin();
            //when
            repository.delete(invoices.get(0).getId());
            //then
            repository.getEntityManager().getTransaction().commit();
        } finally {
            if(repository.getEntityManager().getTransaction().isActive()) {
                repository.getEntityManager().getTransaction().rollback();
            }
        }
        List<Invoice> expectedInvoices = repository.findAll();
        assertEquals(invoices.size() - expectedInvoices.size(), 1);
        assertFalse(expectedInvoices.contains(invoices.get(0)));
        repository.getEntityManager().close();
        entityManagerFactory.close();
    }

    @Test
    public void shouldUpdateAnInvoice() throws Exception {
        //given
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Invoices");
        InvoiceRepository repository = new InvoiceRepository(entityManagerFactory);
        Invoice invoice = InvoiceGenerator.getAnInvoice(LocalDate.now(), "Dummy Company", "Dummy City",
                Arrays.asList("Item one", "Item two"));
        repository.getEntityManager().getTransaction().begin();
        repository.create(invoice);
        Counterparty expectedCounterparty = InvoiceGenerator.getACounterparty();
        List<InvoiceItem> expectedInvoiceItems = InvoiceGenerator.getAnInvoiceItem();
        InvoiceBody invoiceBody = new InvoiceBody(invoice.getDate().plusDays(10),
                expectedCounterparty, expectedInvoiceItems);
        //when
        Invoice actualInvoice = repository.update(invoice.getId(), invoiceBody);
        //then
        Invoice actualInvoiceDb = repository.findById(actualInvoice.getId());
        repository.getEntityManager().getTransaction().commit();
        assertEquals(invoiceBody.getDate(), actualInvoice.getDate());
        assertEquals(invoiceBody.getCounterparty(), actualInvoice.getCounterparty());
        assertEquals(invoiceBody.getInvoiceItems(), actualInvoice.getInvoiceItems());
        System.out.println(invoiceBody.getDate());
        System.out.println(actualInvoiceDb.getDate());
        assertEquals(invoiceBody.getDate(), actualInvoiceDb.getDate());
        assertEquals(invoiceBody.getCounterparty(), actualInvoiceDb.getCounterparty());
        assertEquals(invoiceBody.getInvoiceItems(), actualInvoiceDb.getInvoiceItems());
        repository.getEntityManager().close();
        entityManagerFactory.close();
    }

    @Test
    public void shouldCountAllInvoices() throws Exception {
        //given
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Invoices");
        InvoiceRepository repository = new InvoiceRepository(entityManagerFactory);
        List<Invoice> invoices = InvoiceGenerator.getManyInvoices(5);
        repository.getEntityManager().getTransaction().begin();
        for (Invoice invoice : invoices) {
            repository.create(invoice);
        }
        repository.getEntityManager().getTransaction().commit();
        //when
        Long expectedNumber = repository.countAll();
        //then
        assertEquals(invoices.size(), expectedNumber);
        repository.getEntityManager().close();
        entityManagerFactory.close();
    }

}