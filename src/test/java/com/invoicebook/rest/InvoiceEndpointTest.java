package com.invoicebook.rest;

import com.invoicebook.model.Invoice;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceEndpointTest {

    @Test
    void createInvoice() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/javaee-jpa-hibernate-1.0-SNAPSHOT/api/invoices/123");
        Invocation invocation = target.request(MediaType.APPLICATION_JSON).buildGet();
        Response response = ClientBuilder.newClient()
                .target("http://localhost:8080/javaee-jpa-hibernate-1.0-SNAPSHOT/api/invoices/123")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertTrue(response.getStatusInfo() == Response.Status.OK);
        assertNotNull(response.getDate());
        assertTrue(response.getHeaderString("Content-type").equals("application/json"));
        String body = response.readEntity(String.class);
        Invoice invoice = response.readEntity(Invoice.class);
    }

    @Test
    void getInvoices() {
    }

    @Test
    void getInvoice() {
    }

    @Test
    void deleteInvoice() {
    }
}