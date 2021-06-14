package com.example.javaee_jpa_hibernate.test_helper;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;
import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class InvoiceGenerator {

    public static InvoiceBody generateInvoiceBody() {
        return generateInvoiceBody(LocalDate.now(), "Lodz", "Solution Company",
                Arrays.asList("Coffee", "Tea"));
    }

    public static InvoiceBody generateInvoiceBody(LocalDate date, String town, String companyName, List<String> items) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        Random random = new Random();
        for(String item:items) {
            int numberOfItems = random.nextInt(20);
            int amount = random.nextInt(500);
            invoiceItems.add(new InvoiceItem(item,numberOfItems, BigDecimal.valueOf(amount),
                                 BigDecimal.valueOf(amount * 0.23), Vat.VAT_23));
        }
        Address address = new Address("44-234", town, "StreetA", "673");
        Counterparty counterparty1 =  new Counterparty(companyName, address, "696875432",
                "0123456089", "Dummy Bank", "bank-1234561");
        return new InvoiceBody(date, counterparty1, invoiceItems);
    }

    public static Invoice generateInvoice() {
        InvoiceBody invoiceBody = generateInvoiceBody();
        Invoice invoice = new Invoice(invoiceBody.getDate(), invoiceBody.getCounterparty(), invoiceBody.getInvoiceItems());
        invoice.setId(1L);
        return invoice;
    }

    public static Invoice generateInvoice(int id, InvoiceBody invoiceBody) {
        Invoice invoice = new Invoice(invoiceBody.getDate(), invoiceBody.getCounterparty(), invoiceBody.getInvoiceItems());
        invoice.setId(Long.valueOf(id));
        return invoice;
    }

    public static List<Invoice> generateInvoices(int number) {
        List<Invoice> invoices = new ArrayList<>();
        InvoiceBody invoiceBody = null;
        for(int i = 1; i <= number; i++) {
            invoiceBody = generateInvoiceBody(LocalDate.of(2021, i%12, i%25), "Dummy_Town_" + i,
                    "Dummy company " + i, Arrays.asList("Dummy item_A" + i, "Dummy item_B" + i));
            invoices.add(generateInvoice(i, invoiceBody));
        }
       return invoices;
    }
}
