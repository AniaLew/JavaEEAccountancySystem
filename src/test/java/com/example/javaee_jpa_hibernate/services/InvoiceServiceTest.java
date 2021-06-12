package com.example.javaee_jpa_hibernate.services;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.management.ManagementFactory;

import static jdk.internal.vm.compiler.word.LocationIdentity.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {
    @Before
    public void setUp() {
        EntityManager mockEntityManager = mock(EntityManager.class);
        InvoiceService invoiceService = new InvoiceService(mockEntityManager);
        TypedQuery mockQuery = mock(TypedQuery.class);

        when(invoiceService.getEntityManager().createNamedQuery(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(sampleTasks());
    }

    @Test
    void saveInvoice() {

    }

    @Test
    void getInvoice() {
    }

    @Test
    void getInvoices() {
    }

    @Test
    void changeInvoice() {
    }

    @Test
    void deleteInvoice() {
    }
}