package com.example.javaee_jpa_hibernate.factory;

import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;

import java.util.Random;

public class CounterpartyElement implements InvoiceObject<Counterparty> {
    private Counterparty counterparty;

    public CounterpartyElement() {
    }

    @Override
    public Counterparty getObject() {
        Random random = new Random();
        int number = random.nextInt(10) + 1;
        return new Counterparty("Compan_name_" + number,
                new Address("zip-code_" + number,
                        "City_" + number, "Street_" + number, "No_" + number),
                "111111" + number, "0123456089" + number,
                "Bank_name_", "bank_number_" + number);
    }
}
