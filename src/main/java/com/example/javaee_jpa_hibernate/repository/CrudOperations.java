package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;

import java.util.List;

public interface CrudOperations {
    Invoice create(Invoice invoice);
    Invoice findById(Long id);
    List<Invoice> findAll();
    void delete(Long id);
}
