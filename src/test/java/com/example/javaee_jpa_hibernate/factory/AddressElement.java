package com.example.javaee_jpa_hibernate.factory;

import com.example.javaee_jpa_hibernate.model.counterparty.Address;

import java.util.Random;

public class AddressElement implements InvoiceObject {
    private Address address;

    public AddressElement() {
    }

    @Override
    public Object getObject() {
        Random random = new Random();
        int number = random.nextInt(20) + 1;
        return new Address("zip-code_" + number, "City_" + number,
                "street_name_" + number, "No_" + number);
    }
}
