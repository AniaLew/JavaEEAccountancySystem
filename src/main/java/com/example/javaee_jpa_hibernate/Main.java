package com.example.javaee_jpa_hibernate;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.InvoiceBody;
import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.Vat;
import com.example.javaee_jpa_hibernate.services.InvoiceService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Invoices
        List<InvoiceItem> invoiceItems1 = Arrays.asList(
                new InvoiceItem("Bread",2, BigDecimal.valueOf(100),BigDecimal.valueOf(23), Vat.VAT_23),
                new InvoiceItem("Milk", 1,BigDecimal.valueOf(50), BigDecimal.valueOf(11.5), Vat.VAT_23));
        List<InvoiceItem> invoiceItems2 = Arrays.asList(
                new InvoiceItem("Cheese",4, BigDecimal.valueOf(234),BigDecimal.valueOf(7), Vat.VAT_23),
                new InvoiceItem("Coffee",7, BigDecimal.valueOf(54),BigDecimal.valueOf(6), Vat.VAT_23));
        List<InvoiceItem> invoiceItems3 = Arrays.asList(
                new InvoiceItem("Eggs",2, BigDecimal.valueOf(24),BigDecimal.valueOf(12), Vat.VAT_23),
                new InvoiceItem("Jam",3, BigDecimal.valueOf(45),BigDecimal.valueOf(10), Vat.VAT_23));
        List<InvoiceItem> invoiceItems4 = Arrays.asList(
                new InvoiceItem("Tea",3, BigDecimal.valueOf(76),BigDecimal.valueOf(5), Vat.VAT_23),
                new InvoiceItem("Cherries",7, BigDecimal.valueOf(34),BigDecimal.valueOf(4), Vat.VAT_23));

        Address address1 = new Address("44-234", "Lodz", "StreetA", "673");
        Address address2 = new Address("44-235", "Warszawa", "StreetB", "671");
        Address address3 = new Address("44-236", "Wroclaw", "StreetC", "467");
        Address address4 = new Address("44-237", "Gdynia", "StreetD", "167");

        Counterparty counterparty1 =  new Counterparty("Company no 1", address1, "696875432", "0123456089", "Bank1", "bank-1234561");
        Counterparty counterparty2 =  new Counterparty("Company no 12", address2, "696875435", "0123406789", "Bank12", "bank-1234562");
        Counterparty counterparty3 =  new Counterparty("Company no 13", address3, "696875437", "0120056789", "Bank13", "bank-1234563");
        Counterparty counterparty4 =  new Counterparty("Company no 14", address4, "496875432", "0123456009", "Bank14", "bank-1234564");
        Counterparty counterparty5 =  new Counterparty("Big Ben", address1, "496875432", "0123456009", "Bank14", "bank-1234564");

        InvoiceBody invoiceBody1 = new InvoiceBody(LocalDate.of(2021, 6, 13),
                counterparty1, invoiceItems1);
        InvoiceBody invoiceBody2 = new InvoiceBody(LocalDate.of(2021, 6, 15),
                counterparty2, invoiceItems2);
        InvoiceBody invoiceBody3 = new InvoiceBody(LocalDate.of(2021, 6, 16),
                counterparty3, invoiceItems3);
        InvoiceBody invoiceBody4 = new InvoiceBody(LocalDate.of(2021, 6, 17),
                counterparty4, invoiceItems4);

//----------------------- create invoices in DG
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Invoices");
        InvoiceService manager = new InvoiceService(entityManagerFactory.createEntityManager());
        manager.createInvoice(invoiceBody1);
        manager.createInvoice(invoiceBody2);
        manager.createInvoice(invoiceBody3);
        manager.createInvoice(invoiceBody4);
//----------------------- get invoice by id
        System.out.println();
        System.out.println("--------------------One invoices:");
        Invoice invoice = manager.findInvoiceById(1L);
        System.out.println("Invoice got by id: " + invoice.toString());

//----------------------- get all invoices
        System.out.println();
        System.out.println("--------------------All invoices:");
        List<Invoice> invoices = manager.getAllInvoices();
        invoices.forEach(System.out::println);
//----------------------- update invoice
        System.out.println();
        System.out.println("--------------------updated invoice invoices:");
        List<InvoiceItem> invoiceItems5 = Arrays.asList(
                new InvoiceItem("soap",7, BigDecimal.valueOf(38),BigDecimal.valueOf(16), Vat.VAT_8));
        InvoiceBody invoiceBody5 = new InvoiceBody(LocalDate.of(2021, 6, 10),
                counterparty5, invoiceItems5);
        manager.updateInvoice(1L, invoiceBody5);
        System.out.println("After updating invoice: " + manager.findInvoiceById(1L));

//----------------------- Delete invoice ----------------
//        manager.deleteInvoice(13L);


//--------------------------CLOSING ------------------------------------
        manager.getEntityManager().close();
        entityManagerFactory.close();
    }






}
