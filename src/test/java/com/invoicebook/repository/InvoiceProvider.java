package com.invoicebook.repository;

import com.invoicebook.model.Invoice;
import com.invoicebook.model.counterparty.Address;
import com.invoicebook.model.counterparty.Counterparty;
import com.invoicebook.model.invoice_item.InvoiceItem;
import com.invoicebook.model.invoice_item.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InvoiceProvider {

    public static Address getAddress() {
        Random random = new Random();
        int number = Math.abs(random.nextInt(20)) + 1;
        return new Address("zip-code_" + number, "City_" + number,
                "street_name_" + number, "No_" + number);
    }

    public static Counterparty getCounterparty() {
        Random random = new Random(9);
        int number = Math.abs(random.nextInt()) + 1;
        return new Counterparty("Compan_name_" + number, getAddress(), "+48 " + number,
                Integer.toString(number % 9), "Bank_name_" + number % 10, "bank_number_" + number);
    }

    public static InvoiceItem getInvoiceItem() {
        Random random = new Random();
        int number = Math.abs(random.nextInt(50)) + 1;
        int amount = Math.abs(random.nextInt(500)) + 1;
        return new InvoiceItem("Invoice_item_" + number, number, BigDecimal.valueOf(amount),
                BigDecimal.valueOf(amount * 0.23), Vat.VAT_23);
    }

    public static List<InvoiceItem> getInvoiceItems(int nbOfInvoiceItems) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (int i = 1; i <= nbOfInvoiceItems; i++) {
            invoiceItems.add(getInvoiceItem());
        }
        return invoiceItems;
    }

    public static List<Invoice> getInvoices(int nbOfInvoices) {
        List<Invoice> invoices = new ArrayList<>();
        for (int i = 0; i < nbOfInvoices; i++) {
            invoices.add(new Invoice(LocalDate.now(), getCounterparty(), getInvoiceItems(2)));
        }
        return invoices;
    }
}
