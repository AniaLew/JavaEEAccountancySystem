package com.invoicebook.repository;

import com.invoicebook.model.Invoice;
import com.invoicebook.model.InvoiceBody;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.invoicebook.appsettings.AppConfig.PERSISTENCE_UNIT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        repository = new InvoiceRepository(entityManager);
    }

    @Test
    public void shouldCreateRepository() {
        assertNotNull(repository);
    }

    @Test
    public void shouldCreateAnInvoice() throws Exception {
        //given
        Invoice invoice = new Invoice(new Date(), InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        //when
        entityManager.getTransaction().begin();
        repository.create(invoice);
        entityManager.getTransaction().commit();
        //then
        List<Invoice> invoices = entityManager.createQuery("SELECT i FROM Invoice i").getResultList();
        assertEquals(invoices.size(), 1);
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
        Invoice invoice = new Invoice(new Date(), InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        entityManager.getTransaction().begin();
        entityManager.persist(invoice);
        entityManager.getTransaction().commit();
        //when
        Invoice actualInvoice = repository.findById(invoice.getId());
        //then
        assertSame(invoice.getCounterparty(), actualInvoice.getCounterparty());
        Invoice invoiceFromDatabase = entityManager.getReference(Invoice.class, actualInvoice.getId());
        assertSame(invoice.getInvoiceItems(), invoiceFromDatabase.getInvoiceItems());
    }

    @Test
    public void shouldFindAllInvoices() throws Exception {
        //given
        List<Invoice> invoices = InvoiceProvider.getInvoices(5);
        entityManager.getTransaction().begin();
        for (Invoice invoice : invoices) {
            entityManager.persist(invoice);
        }
        entityManager.getTransaction().commit();
        //when
        List<Invoice> actualInvoices = repository.findAll();
        //then
        assertEquals(invoices.size(), actualInvoices.size());
        assertArrayEquals(invoices.toArray(), actualInvoices.toArray());
        List<Invoice> invoicesFromDb = entityManager
                .createQuery("SELECT i FROM Invoice i ORDER BY i.date", Invoice.class)
                .getResultList();
        assertArrayEquals(invoices.toArray(), invoicesFromDb.toArray());
    }

    @Test
    public void shouldDeleteAnInvoiceById() throws Exception {
        //given
        List<Invoice> invoices = InvoiceProvider.getInvoices(5);
        entityManager.getTransaction().begin();
        for (Invoice invoice : invoices) {
            entityManager.persist(invoice);
        }
        entityManager.getTransaction().commit();
        //when
        entityManager.getTransaction().begin();
        repository.delete(invoices.get(0).getId());
        entityManager.getTransaction().commit();
        //then
        List<Invoice> expectedInvoices = entityManager
                .createQuery("SELECT i FROM Invoice i", Invoice.class)
                .getResultList()
        ;
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
        Invoice invoice = new Invoice(new Date(), InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        entityManager.getTransaction().begin();
        entityManager.persist(invoice);
        entityManager.getTransaction().commit();
        InvoiceBody invoiceBody = new InvoiceBody(InvoiceProvider.addDaysToDate(invoice.getDate(), 10),
                InvoiceProvider.getCounterpartyBody(), InvoiceProvider.getInvoiceItemBodies(2));
        //when
        Invoice actualInvoice = repository.update(invoice.getId(), invoiceBody);
        //then
        Invoice actualInvoiceDb = repository.findById(actualInvoice.getId());
        assertEquals(invoiceBody.getDate(), actualInvoice.getDate());
        assertEquals(invoiceBody.getDate(), actualInvoiceDb.getDate());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateInvoiceWithNullId() {
        //given
        InvoiceBody invoiceBody = new InvoiceBody(new Date(), InvoiceProvider.getCounterpartyBody(),
                InvoiceProvider.getInvoiceItemBodies(2));
        //then
        Exception exception = assertThrows(Exception.class,
                () -> repository.update(null, invoiceBody));
        System.out.println("Message: " + exception.getMessage());
        assertEquals("id to load is required for loading", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateInvoiceWithInvalidNullBody() {
        //when
        Exception exception = assertThrows(Exception.class,
                () -> repository.update(1L, null));
        //then
        assertThat(exception).isNotNull();
    }

    @Test
    public void shouldCountAllInvoices() throws Exception {
        //given
        List<Invoice> invoices = InvoiceProvider.getInvoices(5);
        entityManager.getTransaction().begin();
        for (Invoice invoice : invoices) {
            entityManager.persist(invoice);
        }
        entityManager.getTransaction().commit();
        //when
        Long expectedNumber = repository.countAll();
        //then
        assertEquals(expectedNumber, invoices.size());
    }

    @Test
    public void shouldFindInvoicesFromGivenDateRange() throws ParseException {
        //given
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTo = formatter.parse("2021-06-20");
        Date dateFrom = formatter.parse("2021-01-20");
        Date date3 = formatter.parse("2021-03-20");
        Date date4 = formatter.parse("2020-03-20");
        Date date5 = formatter.parse("2022-03-20");
        Invoice invoice1 = new Invoice(dateTo, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice2 = new Invoice(dateFrom, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice3 = new Invoice(date3, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice4 = new Invoice(date4, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice5 = new Invoice(date5, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        entityManager.getTransaction().begin();
        entityManager.persist(invoice1);
        entityManager.persist(invoice2);
        entityManager.persist(invoice3);
        entityManager.persist(invoice4);
        entityManager.persist(invoice5);
        entityManager.getTransaction().commit();
        //when
        List<Invoice> invoices = repository.findInvoicesByDate(dateFrom, dateTo);
        //then
        assertEquals(3, invoices.size());
    }

    @Test
    public void shouldFindInvoicesFromDateToDate() throws ParseException {
        //given
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date3 = formatter.parse("2021-03-20");
        Date date4 = formatter.parse("2020-03-20");
        Date date5 = formatter.parse("2022-03-20");
        Date dateTo = formatter.parse("2021-06-20");
        Date dateFrom = formatter.parse("2021-01-20");
        Invoice invoice1 = new Invoice(dateTo, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice2 = new Invoice(dateFrom, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice3 = new Invoice(date3, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice4 = new Invoice(date4, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice5 = new Invoice(date5, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        entityManager.getTransaction().begin();
        entityManager.persist(invoice1);
        entityManager.persist(invoice2);
        entityManager.persist(invoice3);
        entityManager.persist(invoice4);
        entityManager.persist(invoice5);
        entityManager.getTransaction().commit();
        //when
        List<Invoice> invoices = repository.findInvoicesFromDateToDate(dateFrom, dateTo);
        //then
        assertEquals(3, invoices.size());
    }

    @Test
    public void shouldFindInvoicesFromOneDate() throws ParseException {
        //given
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date3 = formatter.parse("2021-03-20");
        Date date4 = formatter.parse("2020-03-20");
        Date date5 = formatter.parse("2022-03-20");
        Date dateFrom = formatter.parse("2021-01-20");
        Invoice invoice1 = new Invoice(dateFrom, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice2 = new Invoice(dateFrom, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice3 = new Invoice(date3, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice4 = new Invoice(date4, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        Invoice invoice5 = new Invoice(date5, InvoiceProvider.getCounterparty(),
                InvoiceProvider.getInvoiceItems(2));
        entityManager.getTransaction().begin();
        entityManager.persist(invoice1);
        entityManager.persist(invoice2);
        entityManager.persist(invoice3);
        entityManager.persist(invoice4);
        entityManager.persist(invoice5);
        entityManager.getTransaction().commit();
        //when
        List<Invoice> invoices = repository.findInvoicesByDate(dateFrom, dateFrom);
        //then
        assertEquals(2, invoices.size());
        assertEquals(dateFrom, invoices.get(0).getDate());
        assertEquals(dateFrom, invoices.get(1).getDate());
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
                entityManager.remove(entityManager.getReference(Invoice.class, invoiceIterator.next().getId()));
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }
}