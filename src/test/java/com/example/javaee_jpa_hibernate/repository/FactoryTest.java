package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.factory.ElementType;
import com.example.javaee_jpa_hibernate.factory.InvoiceObjectFactory;
import org.junit.jupiter.api.Test;

public class FactoryTest {
    @Test
    public void shouldReturnFactory() {
        InvoiceObjectFactory invoiceObjectFactory = new InvoiceObjectFactory();
        System.out.println(invoiceObjectFactory.getInvoiceObject(ElementType.ADDRESS).getObject());
        System.out.println(invoiceObjectFactory.getInvoiceObject(ElementType.COUNTERPARTY).getObject());
    }
}
