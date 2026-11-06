package com.switchfully.eurder.eurder.domain;

import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "EURDER")
public class Eurder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "ARCHIVED")
    private boolean archived;

    public Eurder(Customer customer) {
        this.customer = customer;
    }

    public Eurder() {
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isArchived() {
        return archived;
    }

    //    private double calculatePrice() {
//        return getItemGroups().stream()
//                .mapToDouble(group -> group.getAmount() * group.getUnitPrice())
//                .sum();
//    }
//
//    public boolean hasShippingDateOf(LocalDate shippingDate) {
//        return getItemGroups().stream()
//                .anyMatch(itemGroup -> itemGroup.hasShippingDateOf(shippingDate));
//    }
}
