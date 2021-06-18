package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
public class InvoiceRepository implements CrudOperations {
    @PersistenceContext(unitName = "Invoices")
    private final EntityManager entityManager;

    public InvoiceRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @Transactional(REQUIRED)
    public Invoice create(@NotNull Invoice invoice) {
        entityManager.persist(invoice);
        return invoice;
    }

    @Override
    public Invoice findById(@NotNull @Min(1) @Max(Long.MAX_VALUE) Long id) {
        return entityManager.find(Invoice.class, id);
    }

    @Override
    public List<Invoice> findAll() {
        return entityManager
                .createQuery("SELECT i FROM Invoice i ORDER BY i.date", Invoice.class)
                .getResultStream()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(REQUIRED)
    public void delete(@NotNull @Min(1) @Max(Long.MAX_VALUE) Long id) {
        entityManager.remove(entityManager.getReference(Invoice.class, id));
    }

    @Transactional(REQUIRED)
    public Invoice update(@NotNull Long id, @NotNull InvoiceBody invoiceBody) {
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
