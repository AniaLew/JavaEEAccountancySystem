package com.invoicebook.repository;

import com.invoicebook.model.Invoice;
import com.invoicebook.model.InvoiceBody;
import com.invoicebook.model.counterparty.Counterparty;
import com.invoicebook.model.invoice_item.InvoiceItem;
import com.invoicebook.model.invoice_item.InvoiceItemBody;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.invoicebook.appsettings.AppConfig.PERSISTENCE_UNIT;
import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
@ApplicationScoped
public class InvoiceRepository implements CrudOperations {
    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager entityManager;

    public InvoiceRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public InvoiceRepository() {
    }

    @Override
    @Transactional(REQUIRED)
    public void create(Invoice invoice) {
        entityManager.persist(invoice);
    }

    @Override
    public Invoice findById(Long id) {
        return entityManager.find(Invoice.class, id);
    }

    @Override
    public List<Invoice> findAll() {
        return entityManager
                .createQuery("SELECT i FROM Invoice i", Invoice.class)
                .getResultList();
    }

    @Override
    @Transactional(REQUIRED)
    public void delete(Long id) {
        entityManager.remove(entityManager.getReference(Invoice.class, id));
    }

    @Transactional(REQUIRED)
    public Invoice update(Long id, InvoiceBody invoiceBody) throws ParseException {
       Invoice invoice = entityManager.getReference(Invoice.class, id);
       if(Optional.ofNullable(invoiceBody.getDate()).isPresent()) {
           invoice.setDate(invoiceBody.getDate());
       }
       if(Optional.ofNullable(invoiceBody.getCounterpartyBody()).isPresent()) {
           invoice.setCounterparty(new Counterparty(invoiceBody.getCounterpartyBody()));
       }
       if ((Optional.ofNullable(invoiceBody.getInvoiceItemBodies()).isPresent())) {
           List<InvoiceItem> invoiceItems = new ArrayList<>();
           for(InvoiceItemBody invoiceItemBody: invoiceBody.getInvoiceItemBodies()) {
               invoiceItems.add(new InvoiceItem(invoiceItemBody));
           }
           invoice.setInvoiceItems(invoiceItems);
       }
        return invoice;
    }

    public Long countAll() {
        return entityManager
                .createQuery("SELECT i FROM Invoice i", Invoice.class)
                .getResultList()
                .stream()
                .count();
    }

    public boolean exists(Long id) {
        Invoice invoice = entityManager.find(Invoice.class, id);
        return Optional
                .ofNullable(invoice)
                .isPresent();
    }

    public List<Invoice> findInvoicesByDate(Date dateFrom, Date dateTo) {
        return entityManager
                .createQuery("SELECT i FROM Invoice i WHERE i.date >= :dateFrom AND i.date <= :dateTo ORDER BY i.date", Invoice.class)
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .getResultList();
    }

    public List<Invoice> findInvoicesFromDateToDate(Date dateFrom, Date dateTo) {
        List<Invoice> invoices = entityManager
                .createQuery("SELECT i FROM Invoice i", Invoice.class)
                .getResultList();
        invoices.removeIf(invoice -> invoice.getDate().before(dateFrom));
        invoices.removeIf(invoice -> invoice.getDate().after(dateTo));
        return invoices;
    }
}
