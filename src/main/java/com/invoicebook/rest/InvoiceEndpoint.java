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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        if (invoiceRepository.exists(id)) {
            invoiceRepository.delete(id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id : \\d+}")
    @Consumes(APPLICATION_JSON)
    public Response updateInvoice(@PathParam("id") Long id, InvoiceBody invoiceBody) throws ParseException {
        if (!invoiceRepository.exists(id)) {
            return Response.noContent().build();
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

    @GET
    @Path("/date")
    @Produces(APPLICATION_JSON)
    public Response getInvoicesByDate(
            @QueryParam("from") String stringDateFrom,
            @QueryParam("to") String stringDateTo) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = formatter.parse(stringDateFrom);
        Date dateTo = formatter.parse(stringDateTo);
        if(dateFrom.equals(dateTo)) {
            dateTo = addDay(dateTo);
        }
        List<Invoice> invoices = invoiceRepository.findInvoicesByDate(dateFrom, dateTo);
        if (invoices.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(invoices).build();
    }

    private static Date addDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
