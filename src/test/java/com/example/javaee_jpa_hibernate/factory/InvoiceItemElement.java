package com.example.javaee_jpa_hibernate.factory;

import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.Vat;

import java.math.BigDecimal;
import java.util.Random;

public class InvoiceItemElement implements InvoiceObject {
    @Override
    public InvoiceItem getObject() {
        Random random = new Random();
        int number = random.nextInt(50) + 1;
        int amount = random.nextInt(500) + 1;
        return new InvoiceItem("Invoice_item_" + number, number, BigDecimal.valueOf(amount),
                BigDecimal.valueOf(amount * 0.23), Vat.VAT_23);
    }
}
