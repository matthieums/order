package com.switchfully.eurder.eurder.api.dtos;

public class CreateCustomerDTO {
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String address;
    private final String phoneNumber;
    private final String password;

    public CreateCustomerDTO(String firstname, String lastname, String email, String address, String phoneNumber, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
