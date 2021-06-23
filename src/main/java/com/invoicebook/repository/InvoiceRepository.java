package com.invoicebook.repository;

import com.invoicebook.model.Invoice;
import com.invoicebook.model.InvoiceBody;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

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
                .createQuery("SELECT i FROM Invoice i ORDER BY i.date", Invoice.class)
                .getResultList();
    }

    @Override
    @Transactional(REQUIRED)
    public void delete(Long id) {
        entityManager.remove(entityManager.getReference(Invoice.class, id));
    }

    @Transactional(REQUIRED)
    public Invoice update(Long id, InvoiceBody invoiceBody) {
        delete(id);
        Invoice invoice = new Invoice(invoiceBody.getDate(),
                invoiceBody.getCounterparty(), invoiceBody.getInvoiceItems());
        entityManager.persist(invoice);
        return invoice;
    }

    public Long countAll() {
        return entityManager
                .createQuery("SELECT i FROM Invoice i ORDER BY i.date", Invoice.class)
                .getResultList()
                .stream()
                .count();
    }
}
