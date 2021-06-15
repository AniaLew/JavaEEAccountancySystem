package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;

import java.util.List;

public interface CrudOperations {
    public Invoice create(Invoice invoice);
    public Invoice findById(Long id);
    public List<Invoice> findAll();
    public void delete(Long id);
}
