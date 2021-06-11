package com.example.javaee_jpa_hibernate.model;

import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.test_helper.InvoiceBuilder;
import com.example.javaee_jpa_hibernate.test_helper.InvoiceGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {
    @Test
    public void shouldReturnDefaultInvoice() throws Exception {
        //when
        Invoice invoice = new Invoice();
        //then
        assertNotNull(invoice);
    }

    @Test
    public void shouldReturnInvoice() throws Exception {
        //given
        LocalDate date = LocalDate.now();
        Counterparty counterparty = InvoiceGenerator.generateCounterparty();
        List<InvoiceItem> invoiceItems = InvoiceGenerator.generateInvoiceItems(1);
        //when
        Invoice invoice = InvoiceBuilder.builder()
                .withDate(date)
                .withCounterparty(counterparty)
                .withInvoiceItems(invoiceItems)
                .build();
//        I did not know how to handle it:
        invoice.setId(1L);
        //then
        assertNotNull(invoice);
        assertEquals(1L, invoice.getId());
        assertEquals(date, invoice.getDate());
        assertEquals(counterparty, invoice.getCounterparty());
        assertEquals(invoiceItems, invoice.getInvoiceItems());
    }

    @Test
    public void shouldGetId() throws Exception {
        //given
        Invoice invoice = InvoiceGenerator.generateOneInvoice();
        List<Invoice> invoices = InvoiceGenerator.generateListOfInvoices(10);
        //when
        long actualId = invoice.getId();
        List<Long> actualIds = invoices
                .stream().map( invoice1-> invoice1.getId())
                .sorted()
                .collect(Collectors.toList());
        //then
        assertEquals(1, actualId);
        for (int i = 1; i <= invoices.size(); i++) {
            actualId = actualIds.get(i - 1);
            assertEquals(i, actualId);
        }
    }

    @Test
    public void shouldGetDate() throws Exception {
        //given
        LocalDate expectedDate = LocalDate.now();
        Invoice invoice = InvoiceGenerator.generateOneInvoice(expectedDate, InvoiceGenerator.generateCounterparty(),
                InvoiceGenerator.generateInvoiceItems(1));
        //when
        LocalDate actualDate = invoice.getDate();
        //then
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void shouldGetCounterparty() throws Exception {
        //given
        Address address = InvoiceGenerator.generateAddress();
        Counterparty expectedCounterparty = InvoiceGenerator
                .generateCounterparty("Company name", address, "696 54 24 64",
                        "123456789", "PKO", "87 1010 1397 0055 0022 2100 0000");
        Invoice invoice = InvoiceGenerator.generateOneInvoice(LocalDate.now(), expectedCounterparty,
                InvoiceGenerator.generateInvoiceItems(1));
        //when
        Counterparty actualCounterparty = invoice.getCounterparty();
        //then
        assertEquals(expectedCounterparty, actualCounterparty);
        assertEquals(expectedCounterparty.getCompanyName(), actualCounterparty.getCompanyName());
        assertEquals(expectedCounterparty.getNip(), actualCounterparty.getNip());
        assertEquals(expectedCounterparty.getAddress(), actualCounterparty.getAddress());
        assertEquals(expectedCounterparty.getBankName(), actualCounterparty.getBankName());
        assertEquals(expectedCounterparty.getBankNumber(), actualCounterparty.getBankNumber());
        assertEquals(expectedCounterparty.getPhoneNumber(), actualCounterparty.getPhoneNumber());
    }

    @Test
    public void shouldGetInvoiceItems() throws Exception {
        //given
        List<InvoiceItem> expectedInvoiceItem = InvoiceGenerator.generateInvoiceItems(1);
        Invoice invoice =InvoiceGenerator.generateOneInvoice(LocalDate.now(), InvoiceGenerator.generateCounterparty(),
                expectedInvoiceItem);
        //when
        List<InvoiceItem> actualInvoiceItem = invoice.getInvoiceItems();
        //then
        assertEquals(expectedInvoiceItem, actualInvoiceItem);
        assertEquals(expectedInvoiceItem.get(0), actualInvoiceItem.get(0));
    }

    @Test
    public void shouldCheckIfInvoicesAreNotEqual() throws Exception {
        //given
        List<Invoice> invoices = InvoiceGenerator
                .generateListOfInvoices( 2);
        Boolean expected = true;
        //when
        Boolean actual = invoices.get(0).equals(invoices.get(1));
        //then
        assertNotEquals(expected, invoices.get(0).equals(null));
        assertNotEquals(expected, invoices.get(1).equals(null));
        assertNotEquals(expected, actual);
    }

    @Test
    public void shouldReturnHashCode() throws Exception {
        //given
        Invoice invoice = InvoiceGenerator
                .generateOneInvoice();
        Long id = invoice.getId();
        int expectedHashCode = 31 * (31 * id.hashCode() + invoice.getDate().hashCode())
                + invoice.getCounterparty().hashCode();
        //when
        int actualHashCode = invoice.hashCode();
        //then
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void shouldCompareInvoices() throws Exception {
        //given
        LocalDate date = LocalDate.of(2017, 3, 12);
        List<Invoice> invoices = InvoiceGenerator
                .generateListOfInvoices(2);
        Invoice invoice3 = null;
        int expected = -1;
        //when
        int actual = invoices.get(0).compareTo(invoices.get(1));
        //then
        assertEquals(expected, actual);
        assertEquals(-1, invoices.get(0).compareTo(null));
        assertEquals(-1, invoices.get(1).compareTo(null));
        assertEquals(Math.abs(invoices.get(0).compareTo(invoices.get(1))),
                Math.abs(invoices.get(1).compareTo(invoices.get(0))));
    }

    @Test
    public void shouldPrintOutInvoiceAsString() throws Exception {
        //given
        Invoice invoice = InvoiceGenerator
                .generateOneInvoice();
        String expected = String.valueOf(invoice);
        //when
        String actual = invoice.toString();
        //then
        assertEquals(expected, actual);
    }
}