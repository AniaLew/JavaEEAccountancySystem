package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceRepositoryTest {
    private static EntityManagerFactory entityManagerFactory;
    private InvoiceRepository repository;

    @BeforeAll
    public static void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("Invoices");
        System.out.println("setup" + entityManagerFactory);
    }

    @BeforeEach
    public void setRepository() {
        repository = new InvoiceRepository(entityManagerFactory.createEntityManager());
        repository.getEntityManager().getTransaction().begin();
    }

    @Test
    public void shouldCreateRepository() {
        assertNotNull(repository);
    }

    @Test
    public void shouldCreateAnInvoice() throws Exception {
        //given
        Invoice invoice = InvoiceProvider.getInvoice();
        //when
        Invoice expectedInvoice = repository.create(invoice);
        //then
        assertEquals(invoice, expectedInvoice);
        assertTrue(repository.getEntityManager().contains(invoice));
        Invoice invoiceFromDatabase = repository.getEntityManager().getReference(Invoice.class, invoice.getId());
        assertThat(invoiceFromDatabase.getDate())
                .isNotNull()
                .isEqualTo(invoice.getDate());
        assertAll(
                () -> assertEquals(invoice.getCounterparty().getCompanyName(),
                        invoiceFromDatabase.getCounterparty().getCompanyName()),
                () -> assertEquals(invoice.getCounterparty().getAddress(),
                        invoiceFromDatabase.getCounterparty().getAddress()),
                () -> assertEquals(invoice.getCounterparty().getPhoneNumber(),
                        invoiceFromDatabase.getCounterparty().getPhoneNumber()),
                () -> assertEquals(invoice.getCounterparty().getNip(),
                        invoiceFromDatabase.getCounterparty().getNip()),
                () -> assertEquals(invoice.getCounterparty().getBankName(),
                        invoiceFromDatabase.getCounterparty().getBankName()),
                () -> assertEquals(invoice.getCounterparty().getBankNumber(),
                        invoiceFromDatabase.getCounterparty().getBankNumber())
        );
        assertSame(invoice.getInvoiceItems(), invoiceFromDatabase.getInvoiceItems());
    }

    @Test
    public void shouldFindAnInvoiceById() throws Exception {
        //given
        Invoice invoice = InvoiceProvider.getInvoice();
        repository.create(invoice);
        //when
        Invoice actualInvoice = repository.findById(invoice.getId());
        //then
        assertSame(invoice, actualInvoice);
        Invoice invoiceFromDatabase = repository.getEntityManager().getReference(Invoice.class, actualInvoice.getId());
        assertSame(invoice.getInvoiceItems(), invoiceFromDatabase.getInvoiceItems());
    }

    @Test
    public void shouldFindAllInvoices() throws Exception {
        //given
        List<Invoice> invoices = InvoiceProvider.getInvoices(5);
        for (Invoice invoice : invoices) {
            repository.create(invoice);
        }
        //when
        List<Invoice> actualInvoices = repository.findAll();
        //then
        assertEquals(invoices.size(), actualInvoices.size());
        assertArrayEquals(invoices.toArray(), actualInvoices.toArray());
        List<Invoice> invoicesFromDb = repository
                .getEntityManager()
                .createQuery("SELECT i FROM Invoice i ORDER BY i.date", Invoice.class)
                .getResultList();
        assertArrayEquals(invoices.toArray(), invoicesFromDb.toArray());
    }

    @Test
    public void shouldDeleteAnInvoiceById() throws Exception {
        //given
        List<Invoice> invoices = InvoiceProvider.getInvoices(5);
        for (Invoice invoice : invoices) {
            repository.create(invoice);
        }
        //when
        repository.delete(invoices.get(0).getId());
        //then
        List<Invoice> expectedInvoices = repository
                .getEntityManager()
                .createQuery("SELECT i FROM Invoice i ORDER BY i.date", Invoice.class)
                .getResultStream()
                .collect(Collectors.toList());;
        assertEquals(invoices.size() - expectedInvoices.size(), 1);
        assertFalse(expectedInvoices.contains(invoices.get(0)));
    }

    @Test
    public void shouldUpdateAnInvoice() throws Exception {
        //given
        Invoice invoice = InvoiceProvider.getInvoice();
        repository.create(invoice);
        Counterparty expectedCounterparty = InvoiceProvider.getCounterparty();
        List<InvoiceItem> expectedInvoiceItems = InvoiceProvider.getInvoiceItems(2);
        InvoiceBody invoiceBody = new InvoiceBody(invoice.getDate().plusDays(10),
                expectedCounterparty, expectedInvoiceItems);
        //when
        Invoice actualInvoice = repository.update(invoice.getId(), invoiceBody);
        //then
        Invoice actualInvoiceDb = repository.findById(actualInvoice.getId());
        assertEquals(invoiceBody.getDate(), actualInvoice.getDate());
        assertEquals(invoiceBody.getCounterparty(), actualInvoice.getCounterparty());
        assertEquals(invoiceBody.getInvoiceItems(), actualInvoice.getInvoiceItems());
        assertEquals(invoiceBody.getDate(), actualInvoiceDb.getDate());
        assertEquals(invoiceBody.getCounterparty(), actualInvoiceDb.getCounterparty());
        assertEquals(invoiceBody.getInvoiceItems(), actualInvoiceDb.getInvoiceItems());
    }

    @Test
    public void shouldCountAllInvoices() throws Exception {
        //given
        List<Invoice> invoices = InvoiceProvider.getInvoices(5);
        for (Invoice invoice : invoices) {
            repository.create(invoice);
        }
        //when
        Long expectedNumber = repository.countAll();
        //then
        assertEquals(invoices.size(), expectedNumber);
    }

    @AfterEach
    public void closeEntityManager() {
        repository.getEntityManager().getTransaction().commit();
        clearDatabase();
        repository.getEntityManager().close();
    }

    @AfterAll
    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

    private void clearDatabase() {
        List<Invoice> invoices = repository.findAll();
        try {
            repository.getEntityManager().getTransaction().begin();
            Iterator<Invoice> invoiceIterator = invoices.iterator();
            while (invoiceIterator.hasNext()) {
                repository.delete(invoiceIterator.next().getId());
            }
            repository.getEntityManager().getTransaction().commit();
        } finally {
            if (repository.getEntityManager().getTransaction().isActive()) {
                repository.getEntityManager().getTransaction().rollback();
            }
        }
    }
}