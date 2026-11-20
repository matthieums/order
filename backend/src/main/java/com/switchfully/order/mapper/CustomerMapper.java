package com.switchfully.order.mapper;

import com.switchfully.order.domain.Customer;
import com.switchfully.order.dto.CustomerDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final EmailMapper emailMapper;
    private final PhoneNumberMapper phoneNumberMapper;

    public CustomerMapper(AddressMapper addressMapper, EmailMapper emailMapper, PhoneNumberMapper phoneNumberMapper) {
        this.addressMapper = addressMapper;
        this.emailMapper = emailMapper;
        this.phoneNumberMapper = phoneNumberMapper;
    }

    public Customer toDomain(UUID customerId, CustomerDto customerDto) {
        if(customerDto.getId() == null) {
            return toDomain(customerDto.withId(customerId));
        }
        if(!customerId.toString().equals(customerDto.getId())) {
            throw new IllegalArgumentException("When updating a customer, the provided ID in the path should match the ID in the body: " +
                    "ID in path = " + customerId.toString() + ", ID in body = " + customerDto.getId());
        }
        return toDomain(customerDto);
    }

        public CustomerDto toDto(Customer customer) {
        return new CustomerDto()
                .withId(customer.getId())
                .withFirstname(customer.getFirstname())
                .withLastname(customer.getLastname())
                .withAddress(addressMapper.toDto(customer.getAddress()))
                .withEmail(emailMapper.toDto(customer.getEmail()))
                .withPhoneNumber(phoneNumberMapper.toDto(customer.getPhoneNumber()));
    }

        public Customer toDomain(CustomerDto customerDto) {
        return Customer.CustomerBuilder.customer()
                .withId(customerDto.getId() == null ? null : UUID.fromString(customerDto.getId()))
                .withLastname(customerDto.getLastname())
                .withFirstname(customerDto.getFirstname())
                .withAddress(addressMapper.toDomain(customerDto.getAddress()))
                .withEmail(emailMapper.toDomain(customerDto.getEmail()))
                .withPhoneNumber(phoneNumberMapper.toDomain(customerDto.getPhoneNumber()))
                .build();
    }

}
