package com.switchfully.order.domain;

import com.switchfully.order.domain.Customer.CustomerBuilder;

import java.util.UUID;

import static com.switchfully.order.domain.AddressTestBuilder.anAddress;
import static com.switchfully.order.domain.EmailTestBuilder.anEmail;
import static com.switchfully.order.domain.PhoneNumberTestBuilder.aPhoneNumber;

public class CustomerTestBuilder {

    private CustomerBuilder customerBuilder;

    private CustomerTestBuilder(CustomerBuilder customerBuilder) {
        this.customerBuilder = customerBuilder;
    }

    public static CustomerTestBuilder anEmptyCustomer() {
        return new CustomerTestBuilder(CustomerBuilder.customer());
    }

    public static CustomerTestBuilder aCustomer() {
        return new CustomerTestBuilder(CustomerBuilder.customer()
                .withFirstname("Tom")
                .withLastname("Thompson")
                .withAddress(anAddress().build())
                .withEmail(anEmail().build())
                .withPhoneNumber(aPhoneNumber().build())
        );
    }

        public Customer build() {
        return customerBuilder.build();
    }

    public CustomerTestBuilder withId(UUID id) {
        customerBuilder.withId(id);
        return this;
    }

    public CustomerTestBuilder withFirstname(String firstname) {
        customerBuilder.withFirstname(firstname);
        return this;
    }

    public CustomerTestBuilder withLastname(String lastname) {
        customerBuilder.withLastname(lastname);
        return this;
    }

    public CustomerTestBuilder withEmail(Email email) {
        customerBuilder.withEmail(email);
        return this;
    }

    public CustomerTestBuilder withAddress(Address address) {
        customerBuilder.withAddress(address);
        return this;
    }

    public CustomerTestBuilder withPhoneNumber(PhoneNumber phoneNumber) {
        customerBuilder.withPhoneNumber(phoneNumber);
        return this;
    }
}