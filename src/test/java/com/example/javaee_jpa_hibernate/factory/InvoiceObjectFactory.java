package com.example.javaee_jpa_hibernate.factory;

public class InvoiceObjectFactory {
    public InvoiceObject getInvoiceObject(ElementType objectType) {
        if (objectType == null) {
            return null;
        }
        if (objectType.equals(ElementType.ADDRESS)) {
            return new AddressElement();
        } else if (objectType.equals(ElementType.COUNTERPARTY)) {
            return new CounterpartyElement();
        } else if (objectType.equals(ElementType.INVOICE_ITEM)) {
            return new InvoiceItemElement();
        } else if (objectType.equals(ElementType.INVOICE)) {
            return new InvoiceElement();
        }
        throw new IllegalArgumentException("No such object type " + objectType.name());
    }
}
