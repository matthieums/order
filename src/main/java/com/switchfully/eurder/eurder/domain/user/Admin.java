package com.switchfully.eurder.eurder.domain.user;

public class Admin extends User {


    public Admin(String firstname, String lastname, String email, String password) {
        super(UserType.ADMIN, firstname, lastname, email, password);
    }
}
