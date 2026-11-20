package com.switchfully.order.mapper;

import com.switchfully.order.domain.Customer;
import com.switchfully.order.dto.CustomerOverviewDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerOverviewMapper {

        public CustomerOverviewDto toDto(Customer customer) {
        return new CustomerOverviewDto()
                .withId(customer.getId())
                .withFirstname(customer.getFirstname())
                .withLastname(customer.getLastname());
    }

        public Customer toDomain(CustomerOverviewDto customerOverviewDto) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
