package com.invoicebook.repository;

import com.invoicebook.model.Invoice;
import com.invoicebook.model.InvoiceBody;
import com.invoicebook.model.counterparty.Counterparty;
import com.invoicebook.model.invoice_item.InvoiceItem;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.invoicebook.appsettings.AppConfig.PERSISTENCE_UNIT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InvoiceRepositoryTest {
    private static EntityManagerFactory entityManagerFactory;
    private InvoiceRepository repository;
    private EntityManager entityManager;

    @BeforeAll
    public static void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    @BeforeEach
    public void setRepository() {
        entityManager = entityManagerFactory.createEntityManager();
        repository = new InvoiceRepository(entityManagerFactory.createEntityManager());
    }

    @Test
    public void shouldCreateRepository() {
        assertNotNull(repository);
    }

    @Test
    public void shouldCreateAnInvoice() throws Exception {
        //given
        Invoice invoice = new Invoice(LocalDate.now(), InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        //when
        entityManager.getTransaction().begin();
        repository.create(invoice);
        entityManager.getTransaction().commit();
        //then
//        assertTrue(entityManager.contains(invoice));
        List<Invoice> invoices = entityManager.createQuery("SELECT i FROM Invoice i").getResultList();
        assertEquals(invoices.size(), 0);
        Invoice invoiceFromDB = invoices.get(0);
        assertThat(invoiceFromDB.getDate())
                .isNotNull()
                .isEqualTo(invoice.getDate());
        assertAll(
                () -> Assertions.assertEquals(invoice.getCounterparty().getCompanyName(),
                        invoiceFromDB.getCounterparty().getCompanyName()),
                () -> Assertions.assertEquals(invoice.getCounterparty().getAddress(),
                        invoiceFromDB.getCounterparty().getAddress()),
                () -> Assertions.assertEquals(invoice.getCounterparty().getPhoneNumber(),
                        invoiceFromDB.getCounterparty().getPhoneNumber()),
                () -> Assertions.assertEquals(invoice.getCounterparty().getNip(),
                        invoiceFromDB.getCounterparty().getNip()),
                () -> Assertions.assertEquals(invoice.getCounterparty().getBankName(),
                        invoiceFromDB.getCounterparty().getBankName()),
                () -> Assertions.assertEquals(invoice.getCounterparty().getBankNumber(),
                        invoiceFromDB.getCounterparty().getBankNumber())
        );
        assertSame(invoice.getInvoiceItems(), invoiceFromDB.getInvoiceItems());
    }

    @Test
    public void shouldThrowExceptionWhenCreateInvoiceWithValidId() {
        Exception exception = assertThrows(Exception.class,
                () -> repository.findById(null));
        System.out.println("Message: " + exception.getMessage());
        assertEquals("id to load is required for loading", exception.getMessage());
    }

    @Test
    public void shouldFindAnInvoiceById() throws Exception {
        //given
        Invoice invoice = new Invoice(LocalDate.now(), InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        repository.create(invoice);
        //when
        Invoice actualInvoice = repository.findById(invoice.getId());
        //then
        assertSame(invoice, actualInvoice);
        Invoice invoiceFromDatabase = entityManager.getReference(Invoice.class, actualInvoice.getId());
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
        List<Invoice> invoicesFromDb =entityManager
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
        List<Invoice> expectedInvoices =entityManager
                .createQuery("SELECT i FROM Invoice i ORDER BY i.date", Invoice.class)
                .getResultStream()
                .collect(Collectors.toList());;
        assertEquals(invoices.size() - expectedInvoices.size(), 1);
        assertFalse(expectedInvoices.contains(invoices.get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenDeleteInvoiceWithNullId() {
        //when
        Exception exception = assertThrows(Exception.class,
                () -> repository.delete(null));
        //then
        assertEquals("id to load is required for loading", exception.getMessage());
    }

    @Test
    public void shouldUpdateAnInvoice() throws Exception {
        //given
        Invoice invoice = new Invoice(LocalDate.now(), InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
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
        Assertions.assertEquals(invoiceBody.getCounterparty(), actualInvoice.getCounterparty());
        assertEquals(invoiceBody.getInvoiceItems(), actualInvoice.getInvoiceItems());
        assertEquals(invoiceBody.getDate(), actualInvoiceDb.getDate());
        Assertions.assertEquals(invoiceBody.getCounterparty(), actualInvoiceDb.getCounterparty());
        assertEquals(invoiceBody.getInvoiceItems(), actualInvoiceDb.getInvoiceItems());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateInvoiceWithNullId() {
        //given
        Counterparty expectedCounterparty = InvoiceProvider.getCounterparty();
        List<InvoiceItem> expectedInvoiceItems = InvoiceProvider.getInvoiceItems(2);
        InvoiceBody invoiceBody = new InvoiceBody(LocalDate.now(), expectedCounterparty, expectedInvoiceItems);
        //then
        Exception exception = assertThrows(Exception.class,
                () -> repository.update(null, invoiceBody));
        System.out.println("Message: " + exception.getMessage());
        assertEquals("id to load is required for loading", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateInvoiceWithInvalidNullBody() {
        //given
        Invoice invoice = new Invoice(LocalDate.now(), InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        entityManager.persist(invoice);
        //when
        Exception exception = assertThrows(Exception.class,
                () -> repository.update(invoice.getId(), null));
        //then
        assertThat(exception).isNotNull();
    }

    @Test
    public void shouldThrowExceptionWhenUpdateInvoiceWithInvalidId() {
        //when
        Exception exception = assertThrows(Exception.class,
                () -> repository.update(1L, null));
        //then
        assertThat(exception.getMessage())
                .isNotNull()
                .contains("Unable to find");
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
        clearDatabase();
        entityManager.close();
    }

    @AfterAll
    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

    private void clearDatabase() {
        List<Invoice> invoices = repository.findAll();
        try {
            entityManager.getTransaction().begin();
            Iterator<Invoice> invoiceIterator = invoices.iterator();
            while (invoiceIterator.hasNext()) {
                repository.delete(invoiceIterator.next().getId());
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }
}