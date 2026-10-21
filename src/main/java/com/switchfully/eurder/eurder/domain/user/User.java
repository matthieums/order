package com.switchfully.eurder.eurder.domain.user;

import jakarta.validation.constraints.NotBlank;

public class User {
    private final UserType userType;
    @NotBlank
    String firstname;
    @NotBlank
    String lastname;
    @NotBlank
    String email;
    @NotBlank
    String password;

    public User(UserType userType, String firstname, String lastname, String email, String password) {
        this.userType = userType;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public boolean allowedToPerform(Feature feature) {
        return userType.containsFeature(feature);
    }

    public UserType getUserType() {
        return userType;
    }

    public boolean isCustomer() {
        return userType.equals(UserType.CUSTOMER);
    }

    public boolean isAdmin() {
        return userType.equals(UserType.ADMIN);
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

    public String getPassword() {
        return password;
    }
}
