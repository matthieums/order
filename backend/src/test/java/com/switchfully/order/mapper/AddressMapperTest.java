package com.switchfully.order.mapper;

import com.switchfully.order.domain.Address;
import com.switchfully.order.dto.AddressDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.switchfully.order.domain.Address.AddressBuilder.address;

class AddressMapperTest {

    @Test
    void toDto() {
        AddressDto addressDto = new AddressMapper().toDto(address()
                .withStreetName("Teststraat")
                .withHouseNumber("88B")
                .withPostalCode("3000")
                .withCountry("Belgium")
                .build());

        Assertions.assertThat(addressDto)
                .isEqualToComparingFieldByField(new AddressDto()
                        .withStreetName("Teststraat")
                        .withHouseNumber("88B")
                        .withPostalCode("3000")
                        .withCountry("Belgium"));
    }

    @Test
    void toDomain() {
        Address address = new AddressMapper().toDomain(new AddressDto()
                .withStreetName("Teststraat")
                .withHouseNumber("88B")
                .withPostalCode("3000")
                .withCountry("Belgium"));

        Assertions.assertThat(address)
                .isEqualToComparingFieldByField(address()
                        .withStreetName("Teststraat")
                        .withHouseNumber("88B")
                        .withPostalCode("3000")
                        .withCountry("Belgium")
                        .build());
    }

}
