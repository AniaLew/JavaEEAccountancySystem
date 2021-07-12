package com.invoicebook.repository;

import com.invoicebook.model.Invoice;

import java.util.List;

public interface CrudOperations {
    void create(Invoice invoice);
    Invoice findById(Long id);
    List<Invoice> findAll();
    void delete(Long id);
}
