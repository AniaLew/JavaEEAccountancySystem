package com.example.javaee_jpa_hibernate.services;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;
import com.example.javaee_jpa_hibernate.test_helper.InvoiceGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.persistence.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;


class InvoiceServiceTest {

    @Test
    public void shouldReturnInvoiceService() {
        //given
        EntityManager entityManager = mock(EntityManager.class);
        //when
        InvoiceService invoiceService = new InvoiceService(entityManager);
        //then
        assertNotNull(invoiceService);
    }

    @Test
    public void shouldCreateInvoiceWhenInvoiceBodyNotNull() {
        //given
        InvoiceBody invoiceBody = InvoiceGenerator.generateInvoiceBody();
        InvoiceService invoiceService = mock(InvoiceService.class);
        when((invoiceService).getEntityManager()).thenReturn(mock(EntityManager.class));
        when(invoiceService.getEntityManager().getTransaction()).thenReturn(mock(EntityTransaction.class));
        doNothing().when(invoiceService).createInvoice(invoiceBody);
        ArgumentCaptor<InvoiceBody> argumentCaptor = ArgumentCaptor.forClass(InvoiceBody.class);
        //when
        invoiceService.createInvoice(invoiceBody);
        //then
        verify(invoiceService, times(1)).createInvoice(invoiceBody);
        verify(invoiceService).createInvoice(argumentCaptor.capture());
        assertEquals(invoiceBody, argumentCaptor.getValue());
    }

    @Test
    public void shouldVerifyCreateInvoiceMethodWhenInvoiceBodyNull() {
        //given
        InvoiceService invoiceService = mock(InvoiceService.class);
        when((invoiceService).getEntityManager()).thenReturn(mock(EntityManager.class));
        when(invoiceService.getEntityManager().getTransaction()).thenReturn(mock(EntityTransaction.class));
        doNothing().when(invoiceService).createInvoice(null);
        ArgumentCaptor<InvoiceBody> argumentCaptor = ArgumentCaptor.forClass(InvoiceBody.class);
        //when
        invoiceService.createInvoice(null);
        //then
        verify(invoiceService, times(1)).createInvoice(null);
        verify(invoiceService).createInvoice(argumentCaptor.capture());
        assertEquals(null, argumentCaptor.getValue());
    }

    @Test
    public void shouldFindInvoiceById() throws Exception {
        //given
        Invoice invoice = InvoiceGenerator.generateInvoice();
        Long expectedId = invoice.getId();
        InvoiceService invoiceService = mock(InvoiceService.class);
        when(invoiceService.findInvoiceById(expectedId)).thenReturn(invoice);
        //when
        Invoice actualInvoice = invoiceService.findInvoiceById(expectedId);
        //then
        verify(invoiceService, times(1)).findInvoiceById(expectedId);
        assertEquals(expectedId, actualInvoice.getId());
    }

    @Test
    public void shouldThrowExceptionWhenFindInvoiceById() throws Exception {
        //given
        Invoice invoice = InvoiceGenerator.generateInvoice();
        Long expectedId = invoice.getId();
        InvoiceService invoiceService = mock(InvoiceService.class);
        doThrow(NoResultException.class)
                .when(invoiceService)
                .findInvoiceById(any(Long.class));
//        //then
        Assertions.assertThrows(NoResultException.class, () -> invoiceService.findInvoiceById(expectedId));
    }

    @Test
    void shouldReturnAllInvoices() {
        //given
        int numberOfInvoices = 10;
        List<Invoice> invoices = InvoiceGenerator.generateInvoices(numberOfInvoices);
        InvoiceService invoiceService = mock(InvoiceService.class);
        when(invoiceService.getAllInvoices()).thenReturn(invoices);
        //when
        List<Invoice> actualInvoices = invoiceService.getAllInvoices();
        //then
        assertEquals(invoices, actualInvoices);
        verify(invoiceService, times(1)).getAllInvoices();
    }

    @Test
    void shouldUpdateInvoiceWhenInvoiceBodyNotNull() {
        //given
        InvoiceBody invoiceBody = InvoiceGenerator.generateInvoiceBody();
        Long invoiceId = 1L;
        InvoiceService invoiceService = mock(InvoiceService.class);
        when((invoiceService).getEntityManager()).thenReturn(mock(EntityManager.class));
        when(invoiceService.getEntityManager().getTransaction()).thenReturn(mock(EntityTransaction.class));
        doNothing().when(invoiceService).updateInvoice(invoiceId, invoiceBody);
        ArgumentCaptor<Long> invoiceIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<InvoiceBody> invoiceBodyCaptor = ArgumentCaptor.forClass(InvoiceBody.class);
        //when
        invoiceService.updateInvoice(invoiceId, invoiceBody);
        //then
        verify(invoiceService, times(1)).updateInvoice(invoiceId, invoiceBody);
        verify(invoiceService).updateInvoice(invoiceIdCaptor.capture(), invoiceBodyCaptor.capture());
        assertEquals(invoiceId, invoiceIdCaptor.getValue());
        assertEquals(invoiceBody, invoiceBodyCaptor.getValue());
    }

    @Test
    void shouldDeleteInvoice() {
        //given
        Long invoiceId = 1L;
        InvoiceService invoiceService = mock(InvoiceService.class);
        when((invoiceService).getEntityManager()).thenReturn(mock(EntityManager.class));
        doNothing().when(invoiceService).deleteInvoice(invoiceId);
        ArgumentCaptor<Long> invoiceIdCaptor = ArgumentCaptor.forClass(Long.class);
        //when
        invoiceService.deleteInvoice(invoiceId);
        //then
        verify(invoiceService, times(1)).deleteInvoice(invoiceId);
        verify(invoiceService).deleteInvoice(invoiceIdCaptor.capture());
        assertEquals(invoiceId, invoiceIdCaptor.getValue());
    }
}