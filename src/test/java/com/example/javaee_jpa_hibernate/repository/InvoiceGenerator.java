package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;
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

    public static Invoice getAnInvoice(LocalDate date, String companyName, String town, List<String> items) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        Random random = new Random();
        for (String item : items) {
            int numberOfItems = random.nextInt(20) + 1;
            int amount = random.nextInt(500) + 1;
            invoiceItems.add(new InvoiceItem(item, numberOfItems, BigDecimal.valueOf(amount),
                    BigDecimal.valueOf(amount * 0.23), Vat.VAT_23));
        }
        Address address = new Address("44-234", town, "StreetA", "673");
        Counterparty counterparty1 = new Counterparty(companyName, address, "696875432",
                "0123456089", "Dummy Bank", "bank-1234561");
        return new Invoice(date, counterparty1, invoiceItems);
    }

    public static List<Invoice> getManyInvoices(int nbOfInvoices) {
        List<Invoice> invoices = new ArrayList<>();
        for(int i = 0; i < nbOfInvoices; i++) {
            invoices.add(getAnInvoice(LocalDate.now(), "Dummy company no " + i,
                    "Dummy City " + i, Arrays.asList("First dummy item " + i, "Second dummy item" + i)));
        }
        return invoices;
    }

    public static Counterparty getACounterparty() {
        Random random = new Random(50);
        int number = random.nextInt() + 1;
        return new Counterparty("Company name" + number, new Address("44-2" + number,
                "City " + number, "StreetA" + number, "123" + number),
                "1111111" + number, "0123456089", "Dummy Bank", "bank-1234561");
    }
    public static List<InvoiceItem> getAnInvoiceItem() {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 2; i++) {
            int number = random.nextInt(20) + 1;
            int amount = random.nextInt(500) +1 ;
            invoiceItems.add(new InvoiceItem("Invoice item" + number, number, BigDecimal.valueOf(amount),
                    BigDecimal.valueOf(amount * 0.23), Vat.VAT_23));
        }
        return invoiceItems;
    }
}
