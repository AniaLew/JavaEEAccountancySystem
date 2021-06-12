package com.example.javaee_jpa_hibernate.model.counterparty;

import com.example.javaee_jpa_hibernate.model.Invoice;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "COUNTERPARTY")
public class Counterparty implements Serializable {
    @Id
    @Column(name = "COUNTERPARTY_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NIP")
    @NotEmpty(message = "NIP cannot be empty")
    private String nip;

    @Column(name = "COMPANY_NAME")
    @NotEmpty(message = "NIP cannot be empty")
    private String companyName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @NotEmpty(message = "NIP cannot be empty")
    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_NUMBER")
    @NotEmpty(message = "NIP cannot be empty")
    private String bankNumber;

    @Embedded
    @NotNull(message = "Address cannot be NULL")
    private Address address;

    @OneToMany(mappedBy = "counterparty", cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    public Counterparty(String companyName, Address address, String phoneNumber, String nip,
                        String bankName,
                        String bankNumber) {
        this.companyName = companyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.nip = nip;
        this.bankName = bankName;
        this.bankNumber = bankNumber;
    }

    public Counterparty() {
    }

    public int getId() {
        return id;
    }

    public String getNip() {
        return nip;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Address getAddress() {
        return address;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public String toString() {
        return "Counterparty{"
                + "companyName='" + companyName + '\''
                + ", address=" + address
                + ", phoneNumber='" + phoneNumber + '\''
                + ", NIP='" + nip + '\''
                + ", bankName='" + bankName + '\''
                + ", bankNumber='" + bankNumber + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Counterparty)) {
            return false;
        }

        Counterparty that = (Counterparty) o;

        if (nip != that.nip) {
            return false;
        }
        if (!getCompanyName().equals(that.getCompanyName())) {
            return false;
        }
        if (!getPhoneNumber().equals(that.getPhoneNumber())) {
            return false;
        }
        if (!getBankName().equals(that.getBankName())) {
            return false;
        }
        if (!getBankNumber().equals(that.getBankNumber())) {
            return false;
        }
        return getAddress().equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        int result = getNip().hashCode();
        result = 31 * result + getCompanyName().hashCode();
        result = 31 * result + getPhoneNumber().hashCode();
        result = 31 * result + getBankName().hashCode();
        result = 31 * result + getBankNumber().hashCode();
        result = 31 * result + getAddress().hashCode();
        return result;
    }
}
