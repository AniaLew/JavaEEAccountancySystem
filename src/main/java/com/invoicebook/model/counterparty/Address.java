package com.invoicebook.model.counterparty;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {
    private String zipCode;
    private String townName;
    private String streetName;
    private String houseNumber;

    public Address(String zipCode, String townName, String streetName, String houseNumber) {
        this.zipCode = zipCode;
        this.townName = townName;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
    }

    public Address() {
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getTownName() {
        return townName;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "Address{"
                + "zipCode='" + zipCode + '\''
                + ", townName='" + townName + '\''
                + ", streetName='" + streetName + '\''
                + ", houseNumber='" + houseNumber + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        Address address = (Address) o;

        if (!getZipCode().equals(address.getZipCode())) {
            return false;
        }
        if (!getTownName().equals(address.getTownName())) {
            return false;
        }
        if (!getStreetName().equals(address.getStreetName())) {
            return false;
        }
        return getHouseNumber().equals(address.getHouseNumber());
    }

    @Override
    public int hashCode() {
        int result = getZipCode().hashCode();
        result = 31 * result + getTownName().hashCode();
        result = 31 * result + getStreetName().hashCode();
        result = 31 * result + getHouseNumber().hashCode();
        return result;
    }
}
