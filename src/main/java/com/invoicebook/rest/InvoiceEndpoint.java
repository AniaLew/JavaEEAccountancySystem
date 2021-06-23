package com.invoicebook.rest;

import com.invoicebook.model.Invoice;
import com.invoicebook.model.InvoiceBody;
import com.invoicebook.model.counterparty.Counterparty;
import com.invoicebook.model.invoice_item.InvoiceItem;
import com.invoicebook.repository.InvoiceRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/invoices")
@RequestScoped
public class InvoiceEndpoint {
    private InvoiceRepository invoiceRepository;

    public InvoiceEndpoint() {
    }

    @Inject
    public InvoiceEndpoint(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createInvoice(InvoiceBody invoiceBody, @Context UriInfo uriInfo) {
        Invoice invoice = getInvoice(invoiceBody);
        invoiceRepository.create(invoice);
        URI createdUri = uriInfo.getBaseUriBuilder().path(String.valueOf(invoice.getId())).build();
        return Response.created(createdUri).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        if (invoices.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(invoices).build();
    }

    @GET
    @Path("/{id : \\d+}")
    @Produces(APPLICATION_JSON)
    public Response getInvoice(@PathParam("id") Long id) {
        Invoice invoice = invoiceRepository.findById(id);
        if (invoice == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(invoice).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    @Consumes(APPLICATION_JSON)
    public Response deleteInvoice(@PathParam("id") Long id) {
        invoiceRepository.delete(id);
       return Response.noContent().build();
    }

    @GET
    @Path("/hello")
    @Produces(APPLICATION_JSON)
    public String getSomething() {
        return "Hello in Invoice App";
    }

    private Invoice getInvoice(InvoiceBody invoiceBody) {
        Counterparty counterparty = new Counterparty(invoiceBody.getCounterparty().getCompanyName(),
                invoiceBody.getCounterparty().getAddress(),
                invoiceBody.getCounterparty().getPhoneNumber(),
                invoiceBody.getCounterparty().getNip(),
                invoiceBody.getCounterparty().getBankName(),
                invoiceBody.getCounterparty().getBankNumber());
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for(InvoiceItem invoiceItem: invoiceBody.getInvoiceItems()) {
            invoiceItems.add(new InvoiceItem(invoiceItem.getDescription(), invoiceItem.getNumberOfItems(),
                    invoiceItem.getAmount(), invoiceItem.getVatAmount(), invoiceItem.getVat()));
        }
        return new Invoice(invoiceBody.getDate(), counterparty,invoiceItems);
    }
}
