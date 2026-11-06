package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
