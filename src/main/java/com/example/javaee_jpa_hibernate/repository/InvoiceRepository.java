package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
public class InvoiceRepository implements CrudOperations {
    @PersistenceContext(unitName = "Invoices")
    EntityManager entityManager;

    public InvoiceRepository() {
    }

    @Override
    @Transactional(REQUIRED)
    public Invoice create(Invoice invoice) {
        entityManager.persist(invoice);
        return invoice;
    }

    @Override
    public Invoice findById(Long id) {
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
    public void delete(Long id) {
        entityManager.remove(entityManager.getReference(Invoice.class, id));
    }

    @Transactional(REQUIRED)
    public Invoice update(Invoice invoice) {
        entityManager.merge(invoice);
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
