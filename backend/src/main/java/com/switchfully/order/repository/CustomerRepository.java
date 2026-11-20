package com.switchfully.order.repository;

import com.switchfully.order.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CustomerRepository extends BaseRepository<Customer>{

    @Override
    public List<Customer> getAll() {
        return getEntityManager()
                .createQuery("FROM Customer", Customer.class)
                .getResultList();
    }

    @Override
    public Customer get(UUID entityId) {
        return getEntityManager()
                .createQuery("FROM Customer WHERE id = :id", Customer.class)
                    .setParameter("id", entityId.toString())
                .getSingleResult();
    }
}
