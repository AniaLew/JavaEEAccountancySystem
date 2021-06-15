package com.example.javaee_jpa_hibernate.repository;

import com.example.javaee_jpa_hibernate.model.Invoice;
import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.model.invoice_item.InvoiceItem;
import com.example.javaee_jpa_hibernate.model.invoice_item.Vat;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class InvoiceRepositoryTest {
    @Inject
    private InvoiceRepository invoiceRepository;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(InvoiceRepository.class)
                .addClass(Invoice.class)
                .addClass(Counterparty.class)
                .addClass(InvoiceItem.class)
                .addClass(Address.class)
                .addClass(Vat.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }

    @Test
    public void shouldCountInvoicesWhenNoneCreated() throws Exception {
        Long nbOfInvoices = invoiceRepository.countAll();
        assertEquals(Long.valueOf(0), nbOfInvoices);
        assertEquals(0, invoiceRepository.findAll().size());
    }

    @Test
    public void shouldCreateAnInvoice() throws Exception {
        //given
        LocalDate date = LocalDate.now();
        List<InvoiceItem> invoiceItems = Arrays.asList(
                new InvoiceItem("Bread",2, BigDecimal.valueOf(100),BigDecimal.valueOf(23), Vat.VAT_23),
                new InvoiceItem("Milk", 1,BigDecimal.valueOf(50), BigDecimal.valueOf(11.5), Vat.VAT_23));

        Address address1 = new Address("44-234", "Lodz", "StreetA", "673");

        Counterparty counterparty =  new Counterparty("Company no 1", address1, "696875432",
                "0123456089", "Bank1", "bank-1234561");
        Invoice invoice = new Invoice(date, counterparty, invoiceItems);
        //when
        Invoice expectedInvoice = invoiceRepository.create(invoice);
        //then
        assertNotNull(expectedInvoice.getId());
        assertEquals(date, expectedInvoice.getDate());
        assertEquals(counterparty, expectedInvoice.getCounterparty());
        assertEquals(invoiceItems, expectedInvoice.getInvoiceItems());
    }
}
