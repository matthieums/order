package com.switchfully.eurder.eurder.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    public Customer(String firstname, String lastname,
                    String email, String password,
                    String address, String phoneNumber) {

        super(firstname, lastname, email, password);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Customer() {
        super();
    }

    public String getId() { return getEmail(); }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
