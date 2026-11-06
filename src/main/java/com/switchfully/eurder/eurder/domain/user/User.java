package com.switchfully.eurder.eurder.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstname;

    @Column(name = "LAST_NAME")
    private String lastname;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    public User(String firstname, String lastname,
                String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    public String getId() {
        return email;
    }
//
//    public boolean allowedToPerform(Feature feature) {
//        return getUserType().containsFeature(feature);
//    }
//
//    public boolean isCustomer() {
//        return userType.equals(UserType.CUSTOMER);
//    }
//
//    public boolean isAdmin() {
//        return userType.equals(UserType.ADMIN);
//    }

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
