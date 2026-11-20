package com.switchfully.order.repository;

import com.switchfully.order.IntegrationTest;
import com.switchfully.order.domain.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.switchfully.order.domain.CustomerTestBuilder.aCustomer;

class CustomerRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    void save() {
        Customer customerToSave = aCustomer().build();

        Customer savedCustomer = repository.save(customerToSave);

        Assertions.assertThat(savedCustomer.getId()).isNotNull();
        Assertions.assertThat(repository.get(savedCustomer.getId()))
                .isEqualToComparingFieldByField(savedCustomer);
    }

    @Test
    void get() {
        Customer savedCustomer = repository.save(aCustomer().build());

        Customer actualCustomer = repository.get(savedCustomer.getId());

        Assertions.assertThat(actualCustomer)
                .isEqualToComparingFieldByField(savedCustomer);
    }

    @Test
    void getAll() {
        Customer customerOne = repository.save(aCustomer().build());
        Customer customerTwo = repository.save(aCustomer().build());

        List<Customer> allCustomers = repository.getAll();

        Assertions.assertThat(allCustomers)
                .containsExactlyInAnyOrder(customerOne, customerTwo);
    }

}
