package com.example.javaee_jpa_hibernate.counterparty;

import com.example.javaee_jpa_hibernate.model.counterparty.Address;
import com.example.javaee_jpa_hibernate.model.counterparty.Counterparty;
import com.example.javaee_jpa_hibernate.test_helper.InvoiceGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CounterpartyTest {
        @Test
        public void shouldReturnDefaultCounterparty() throws Exception {
            //when
            Counterparty counterparty = new Counterparty();
            //then
            assertNotNull(counterparty);
        }

        @Test
        public void shouldReturnCounterparty() throws Exception {
            //given
           String companyName = "Dummy Company";
           Address address = InvoiceGenerator.generateAddress();
           String phoneNumber = "696 34 67 21";
           String nip = "5932603633";
           String bankName = "PKO SA";
           String bankNumber = "11114015601081110181488249";
            //when
            Counterparty counterparty = new Counterparty(companyName, address, phoneNumber, nip, bankName, bankNumber);
            //then
            assertNotNull(counterparty);
            assertEquals(companyName, counterparty.getCompanyName());
            assertEquals(address, counterparty.getAddress());
            assertEquals(nip, counterparty.getNip());
            assertEquals(bankName, counterparty.getBankName());
            assertEquals(bankNumber, counterparty.getBankNumber());
        }
}