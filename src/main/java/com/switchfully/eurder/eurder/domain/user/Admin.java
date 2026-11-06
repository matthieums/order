package com.switchfully.eurder.eurder.domain.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    protected Admin() {
    }

    public Admin(String firstname, String lastname,
                 String email, String password) {
        super(firstname, lastname, email, password);
    }
}
