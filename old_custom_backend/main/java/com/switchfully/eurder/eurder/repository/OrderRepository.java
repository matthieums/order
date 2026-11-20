package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Eurder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Eurder, Long> {
    public List<Eurder> findAllByCustomerId(Long customerId);
}
