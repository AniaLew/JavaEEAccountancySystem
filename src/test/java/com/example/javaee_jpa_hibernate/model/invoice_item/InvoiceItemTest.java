package com.example.javaee_jpa_hibernate.model.invoice_item;

import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.test_helper.InvoiceGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceItemTest {
    @Test
    public void shouldReturnDefaultInvoiceItem() throws Exception {
        //when
        Counterparty counterparty = new Counterparty();
        //then
        assertNotNull(counterparty);
    }

    @Test
    public void shouldReturnInvoiceItem() throws Exception {
        //given
        String description = "Dummy stuff";
        int numberOfItems  = 5;
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal vatAmount = BigDecimal.valueOf(23);
        Vat vat = Vat.VAT_23;
        //when
        InvoiceItem invoiceItem = new InvoiceItem(description, numberOfItems, amount, vatAmount,vat);
        //then
        assertNotNull(invoiceItem);
        assertEquals(description, invoiceItem.getDescription());
        assertEquals(numberOfItems, invoiceItem.getNumberOfItems());
        assertEquals(amount, invoiceItem.getAmount());
        assertEquals(vatAmount, invoiceItem.getVatAmount());
        assertEquals(vat, invoiceItem.getVat());
    }
}