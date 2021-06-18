package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InvoiceProvider {

   public static Address getAddress() {
       Random random = new Random();
       int number = random.nextInt(20) + 1;
       return new Address("zip-code_" + number, "City_" + number,
               "street_name_" + number, "No_" + number);
   }

    public static Counterparty getCounterparty() {
        Random random = new Random(9);
        int number = random.nextInt() + 1;
        return getCounterparty("Compan_name_" + number, "+48 " + number,
                Integer.toString(number%9), "Bank_name_" + number%10, "bank_number_" + number);
    }

    public static Counterparty getCounterparty(String companyName, String phoneNumber, String nip,
                                               String bankName, String bankNumber) {
       return new Counterparty(companyName, getAddress(), phoneNumber, nip, bankName, bankNumber);
    }

    public static InvoiceItem getInvoiceItem() {
        Random random = new Random();
        int number = random.nextInt(50) + 1;
        int amount = random.nextInt(500) + 1;
        return new InvoiceItem("Invoice_item_" + number, number, BigDecimal.valueOf(amount),
                BigDecimal.valueOf(amount * 0.23), Vat.VAT_23);
    }

    public static List<InvoiceItem> getInvoiceItems(int nbOfInvoiceItems) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= nbOfInvoiceItems; i++) {
           invoiceItems.add(getInvoiceItem());
        }
        return invoiceItems;
    }

    public static Invoice getInvoice() {
       return new Invoice(LocalDate.now(), getCounterparty(), getInvoiceItems(2));
    }

    public static List<Invoice> getInvoices(int nbOfInvoices) {
        List<Invoice> invoices = new ArrayList<>();
        for(int i = 0; i < nbOfInvoices; i++) {
            invoices.add(getInvoice());
        }
        return invoices;
    }
}
