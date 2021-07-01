package com.invoicebook.rest;

import com.invoicebook.model.Invoice;
import com.invoicebook.model.InvoiceBody;
import com.invoicebook.repository.InvoiceRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

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
    public Response createInvoice(InvoiceBody invoiceBody, @Context UriInfo uriInfo) throws ParseException {
        Invoice invoice = new Invoice(invoiceBody);
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
        if (Optional.ofNullable(invoice).isPresent()) {
            return Response.ok(invoice).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    @Consumes(APPLICATION_JSON)
    public Response deleteInvoice(@PathParam("id") Long id) {
        if(!invoiceRepository.exists(id)) {
            return Response.noContent().build();
        }
        invoiceRepository.delete(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id : \\d+}")
    @Consumes(APPLICATION_JSON)
    public Response updateInvoice(@PathParam("id") Long id, InvoiceBody invoiceBody,
                                  @Context UriInfo uriInfo) throws ParseException {
        if(!invoiceRepository.exists(id)) {
            Invoice invoice = new Invoice(invoiceBody);
            invoiceRepository.create(invoice);
            URI uri = uriInfo.getBaseUriBuilder().path(String.valueOf(invoice.getId())).build();
            return Response.created(uri).build();
        }
        invoiceRepository.update(id, invoiceBody);
        return Response.ok().build();
    }

    @GET
    @Path("/count")
    @Produces(APPLICATION_JSON)
    public Response countInvoices() {
        Long nbOfInvoices = invoiceRepository.countAll();
        return Response.ok(nbOfInvoices).build();
    }

    @GET
    @Path("/exist/{id : \\d+}")
    @Produces(APPLICATION_JSON)
    public Response exist(@PathParam("id") Long id) {
        boolean exists = invoiceRepository.exists(id);
        return Response.ok(exists).build();
    }
}
