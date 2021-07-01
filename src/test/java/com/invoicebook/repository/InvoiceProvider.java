package com.invoicebook.repository;

import com.invoicebook.model.Invoice;
import com.invoicebook.model.counterparty.Address;
import com.invoicebook.model.counterparty.Counterparty;
import com.invoicebook.model.counterparty.CounterpartyBody;
import com.invoicebook.model.invoice_item.InvoiceItem;
import com.invoicebook.model.invoice_item.InvoiceItemBody;
import com.invoicebook.model.invoice_item.Vat;

import java.math.BigDecimal;
import java.util.*;

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

    public static InvoiceItemBody getInvoiceItemBody() {
        Random random = new Random();
        int number = Math.abs(random.nextInt(50)) + 1;
        int amount = Math.abs(random.nextInt(500)) + 1;
        return new InvoiceItemBody("Invoice_item_" + number, number, BigDecimal.valueOf(amount),
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
            invoices.add(new Invoice(new Date(), getCounterparty(), getInvoiceItems(2)));
        }
        return invoices;
    }

    public static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static CounterpartyBody getCounterpartyBody() {
        Random random = new Random(9);
        int number = Math.abs(random.nextInt()) + 1;
        return new CounterpartyBody( "Compan_name_" + number, getAddress(), "+48 " + number, Integer.toString(number % 9),
                "Bank_name_" + number % 10, "bank_number_" + number);
    }

    public static List<InvoiceItemBody> getInvoiceItemBodies(int nbOfInvoiceItems) {
        List<InvoiceItemBody> invoiceItems = new ArrayList<>();
        for (int i = 1; i <= nbOfInvoiceItems; i++) {
            invoiceItems.add(getInvoiceItemBody());
        }
        return invoiceItems;
    }

    public static String getJsonInvoiceBodyForPostMethod() {
        return "{\"date\": \"2021-06-23\"," +
                " \"counterpartyBody\": {" +
                "     \"nip\": \"123456789\"," +
                "     \"companyName\": \"Dummy Company\"," +
                "     \"phoneNumber\": \"696556677\"," +
                "     \"bankName\": \"PKO\"," +
                "     \"bankNumber\": \"123 456 778 990\"," +
                "     \"address\": {" +
                "         \"zipCode\": \"97-420\"," +
                "         \"townName\": \"Lodz\"," +
                "         \"streetName\": \"Piotrkowska\"," +
                "         \"houseNumber\": \"34\"}" +
                "     }," +
                " \"invoiceItemBodies\": [{\"description\": \"milk\"," +
                "                  \"numberOfItems\": \"2\"," +
                "          \"amount\": 18.23," +
                "          \"vatAmount\": 2.45," +
                "         \"vat\": \"vat_23\"}," +
                "                 {\"description\": \"bread\"," +
                "                  \"numberOfItems\": \"3\"," +
                "          \"amount\": 58.23," +
                "          \"vatAmount\": 12.45," +
                "                  \"vat\": \"vat_23\"}]" +
                "}";
    }

    public static String getJsonInvoiceBodyForPutMethod() {
        return "{\"date\": \"2021-12-12\"," +
                " \"counterpartyBody\": {" +
                "     \"nip\": \"987654321\"," +
                "     \"companyName\": \"ANY Company\"," +
                "     \"phoneNumber\": \"44657899\"," +
                "     \"bankName\": \"PKO\"," +
                "     \"bankNumber\": \"123 456 778 100\"," +
                "     \"address\": {" +
                "         \"zipCode\": \"97-420\"," +
                "         \"townName\": \"Lodz\"," +
                "         \"streetName\": \"Piotrkowska\"," +
                "         \"houseNumber\": \"34\"}" +
                "     }," +
                " \"invoiceItemBodies\": [{\"description\": \"butter\"," +
                "                  \"numberOfItems\": \"5\"," +
                "                  \"amount\": 18.23," +
                "                  \"vatAmount\": 2.45," +
                "                  \"vat\": \"vat_23\"}," +
                "                 {\"description\": \"coffee\"," +
                "                  \"numberOfItems\": \"3\"," +
                "                  \"amount\": 60," +
                "                  \"vatAmount\": 12.45," +
                "                  \"vat\": \"vat_23\"}]" +
                "}";
    }

}
