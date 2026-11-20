package com.switchfully.order.mapper;

import com.switchfully.order.domain.Address.AddressBuilder;
import com.switchfully.order.domain.Address;
import com.switchfully.order.dto.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

        public AddressDto toDto(Address address) {
        return new AddressDto()
                .withStreetName(address.getStreetName())
                .withHouseNumber(address.getHouseNumber())
                .withPostalCode(address.getPostalCode())
                .withCountry(address.getCountry());
    }

        public Address toDomain(AddressDto addressDto) {
        return AddressBuilder.address()
                .withStreetName(addressDto.getStreetName())
                .withHouseNumber(addressDto.getHouseNumber())
                .withPostalCode(addressDto.getPostalCode())
                .withCountry(addressDto.getCountry())
                .build();
    }
}
