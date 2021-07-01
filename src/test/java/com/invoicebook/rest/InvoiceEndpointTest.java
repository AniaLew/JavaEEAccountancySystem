package com.invoicebook.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoicebook.model.Invoice;
import com.invoicebook.repository.InvoiceProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import org.junit.jupiter.api.*;

import java.lang.reflect.Type;

import static org.hamcrest.Matchers.*;

class InvoiceEndpointTest implements  StatusCodes{
    private static int counter = 0;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/";
        RestAssured.basePath = "invoicebook-1/api/invoices/";
    }

    @BeforeEach
    public void setupBeforeTest() {
        counter++;
        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(InvoiceProvider.getJsonInvoiceBodyForPostMethod())
                .post();
    }

    @Test
    public void shouldUseGlobalConstants() {

        RestAssured
                .given()
                .log().all()
                .when()
                .get()
                .then()
                .body("date[0]", equalTo("2021-06-23"));
    }

    @Test
    public void shouldTestHeadRestMethod() {
        RestAssured
                .given()
                .log().headers()
                .head()
                .then()
                .statusCode(OK)
                .body(emptyOrNullString());

    }

    @Test
    public void shouldTestOptionsRestMethod() {
        RestAssured
                .given()
                .log().headers()
                .options()
                .then()
                .statusCode(OK)
                .header("allow", equalTo("HEAD, POST, GET, OPTIONS"))
                .body(equalTo("HEAD, POST, GET, OPTIONS"));
    }

    @Test
    public void shouldPostInvoice() throws JsonProcessingException {
        counter++;
        RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .body(InvoiceProvider.getJsonInvoiceBodyForPostMethod())
                .when()
                    .post()
                .then()
                    .statusCode(CREATED);

        RestAssured.delete( String.valueOf(counter - 1));
    }

    @Test
    public void shouldDeleteExistingInvoice() {
        RestAssured
                .when()
                .delete( String.valueOf(counter))
                .then()
                .statusCode(OK);
    }

    @Test
    public void shouldDeleteNonExistingInvoice() {
        RestAssured
                .when()
                .delete(  "100")
                .then()
                .statusCode(NO_CONTENT);
    }

     @Test
    void shouldGetInvoice() {
        RestAssured.get(RestAssured.baseURI + RestAssured.basePath + String.valueOf(counter))
                .then()
                .body("id", equalTo(counter))
                .body("date", equalTo("2021-06-23"))
                .rootPath("counterparty")
                    .body("id", notNullValue())
                    .body("nip", equalTo("123456789"))
                    .body("companyName",equalTo("Dummy Company"))
                    .rootPath("counterparty.address")
                        .body("zipCode", equalTo("97-420"))
                        .body("townName", equalTo("Lodz"))
                        .body("streetName", equalTo("Piotrkowska"))
                .rootPath("invoiceItems")
                        .body("id", notNullValue())
                        .body("description[0]", equalTo("milk"))
                        .body("description[1]", equalTo("bread"))
                        .body("numberOfItems[0]", is(2))
                        .body("numberOfItems[1]", is(3))
                        .body("amount[0]", is(18.23F))
                        .body("amount[1]", is(58.23F))
                        .body("vat[0]", endsWith("23"))
                        .body("vat[1]", endsWith("23"));
    }

    @Test
    void getInvoices() {
        counter++;
        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(InvoiceProvider.getJsonInvoiceBodyForPostMethod())
                .post();
        RestAssured
                .when()
                .get()
                .then()
                .body("id", hasItems(counter -1, counter))
                .body("date", hasItem("2021-06-23"))
                .body("counterparty.id",notNullValue())
                .body("invoiceItems.vat[0]", hasItem(startsWith("vat")));

        RestAssured.delete( String.valueOf(counter - 1));
    }

    @Test
    void updateInvoice() {
//        counter = 4;
        System.out.println(counter);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(InvoiceProvider.getJsonInvoiceBodyForPutMethod())
                .when()
                .put(RestAssured.baseURI + RestAssured.basePath + String.valueOf(counter))
                .then()
                .statusCode(OK);
        RestAssured
                .get(RestAssured.baseURI + RestAssured.basePath + String.valueOf(counter))
                .then()
                .assertThat()
                .body("date", equalTo("2021-12-12"))
                    .rootPath("counterparty")
                        .body("id", notNullValue())
                        .body("nip", equalTo("987654321"))
                        .body("companyName",equalTo("ANY Company"))
                        .rootPath("counterparty.address")
                            .body("zipCode", equalTo("97-420"))
                            .body("townName", equalTo("Lodz"))
                            .body("streetName", equalTo("Piotrkowska"))
                        .rootPath("invoiceItems")
                            .body("id", notNullValue())
                            .body("description", containsInAnyOrder("coffee", "butter"))
                            .body("numberOfItems", hasItems(3, 5))
                            .body("amount", hasItem(60.0F))
                            .body("vat", hasItem(endsWith("23")));
    }

    @Test
    public void shouldCountInvoices() {
      RestAssured
               .given()
               .log().all()
               .when()
               .get(RestAssured.baseURI + RestAssured.basePath + "count")
               .then()
               .statusCode(OK)
               .body(equalTo("1"));
    }

    @Test
    public void shouldUnmarshallInvoice() {
        Invoice invoice = RestAssured
                .get(RestAssured.baseURI + RestAssured.basePath + String.valueOf(counter))
                .as(Invoice.class, ObjectMapperType.JACKSON_2);
    }

    private Jackson2Mapper getMapper() {
        return new Jackson2Mapper(new Jackson2ObjectMapperFactory() {
            @Override
            public ObjectMapper create(Type type, String s) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return objectMapper;
            }
        });
    }

    private Jackson2Mapper getMapperLambda() {
        return new Jackson2Mapper((type, string) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper;
        });
    }

    @AfterEach
    public void cleanupAfterTest() {
            RestAssured.delete( String.valueOf(counter));
    }
}