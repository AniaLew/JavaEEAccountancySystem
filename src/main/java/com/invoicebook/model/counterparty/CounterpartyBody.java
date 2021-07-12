package com.invoicebook.model.counterparty;

import java.io.Serializable;

public class CounterpartyBody implements Serializable {
    private String nip;
    private String companyName;
    private String phoneNumber;
    private String bankName;
    private String bankNumber;
    private Address address;

    public CounterpartyBody(String companyName, Address address, String phoneNumber, String nip,
                            String bankName, String bankNumber) {
        this.nip = nip;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.address = address;
    }

    public CounterpartyBody() {
    }

    public String getNip() {
        return nip;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "CounterpartyBody{" +
                "nip='" + nip + '\'' +
                ", companyName='" + companyName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankNumber='" + bankNumber + '\'' +
                ", address=" + address +
                '}';
    }
}
