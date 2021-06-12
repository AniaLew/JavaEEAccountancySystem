package com.example.javaee_jpa_hibernate.test_helper;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceGenerator {
    public static Address generateAddress() {
        return new Address("97-420", "Lodz", "Piotrkowska", "34");
    }
    public static Counterparty generateCounterparty() {
        Counterparty counterparty = CounterpartyBuilder.builder()
                .withCompanyName("LPT")
                .withAddress(generateAddress())
                .withPhoneNumber("345678912")
                .withNIP("12345678")
                .withBankName("PKO")
                .withBankNumber("2345")
                .build();
        return counterparty;
    }

    public static Counterparty generateCounterparty(String companyName, Address address, String phoneNumber,
                                                    String nip, String bankName, String bankNumber) {
        Counterparty counterparty = CounterpartyBuilder.builder()
                .withCompanyName(companyName)
                .withAddress(address)
                .withPhoneNumber(phoneNumber)
                .withNIP(nip)
                .withBankName(bankName)
                .withBankNumber(bankNumber)
                .build();
        return counterparty;
    }
    public static InvoiceItem generateInvoiceItem(String description, int numberOfItems, BigDecimal amount,
                                                  BigDecimal vatAmount, Vat vat) {
        return InvoiceItemBuilder.builder()
                .withDescription(description)
                .withNumberOfItem(numberOfItems)
                .withAmount(amount)
                .withVatAmount(vatAmount)
                .withVat(vat)
                .build();
    }

    public static List<InvoiceItem> generateInvoiceItems(int numberOfInvoiceItems) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        String description = "Invoice item No.";
       for(int i = 1; i <= numberOfInvoiceItems; i++) {
           BigDecimal amount = BigDecimal.valueOf(10 + i);
           BigDecimal vatAmount = BigDecimal.valueOf(amount.doubleValue() * 0.23);
            invoiceItems.add(generateInvoiceItem(description + i, i, BigDecimal.valueOf(10 + i),
                    BigDecimal.valueOf(amount.doubleValue() * Vat.VAT_23.getVatValue()), Vat.VAT_23));
        }
        return invoiceItems;
    }

    public static Invoice generateOneInvoice() {
        LocalDate date = LocalDate.now();
        Counterparty counterparty = InvoiceGenerator.generateCounterparty();
        List<InvoiceItem> invoiceItems = InvoiceGenerator.generateInvoiceItems(1);

        Invoice invoice = InvoiceBuilder.builder()
                .withDate(date)
                .withCounterparty(counterparty)
                .withInvoiceItems(invoiceItems)
                .build();
        invoice.setId(1L);
        return invoice;
    }

    public static Invoice generateOneInvoice(LocalDate date, Counterparty counterparty,
                                             List<InvoiceItem> invoiceItems) {

        Invoice invoice = InvoiceBuilder.builder()
                .withDate(date)
                .withCounterparty(counterparty)
                .withInvoiceItems(invoiceItems)
                .build();
        invoice.setId(1L);
        return invoice;
    }

    public static List<Invoice> generateListOfInvoices(LocalDate date, int invoiceNumber) {
        Counterparty counterparty = InvoiceGenerator.generateCounterparty();
        List<InvoiceItem> invoiceItems = InvoiceGenerator.generateInvoiceItems(1);
        List<Invoice> invoices = new ArrayList<>();

        for(int i = 1; i <= invoiceNumber; i++ ) {
            Invoice invoice = InvoiceBuilder.builder()
                    .withDate(date)
                    .withCounterparty(counterparty)
                    .withInvoiceItems(invoiceItems)
                    .build();
            invoice.setId(Long.valueOf(i));
            invoices.add(invoice);
        }
        return invoices;
    }

    public static List<Invoice> generateListOfInvoices(int invoiceNumber) {
       LocalDate date = LocalDate.now();
       return generateListOfInvoices(date, invoiceNumber);
    }
}
