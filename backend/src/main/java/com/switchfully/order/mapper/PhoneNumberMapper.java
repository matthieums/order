package com.switchfully.order.mapper;

import com.switchfully.order.domain.PhoneNumber;
import com.switchfully.order.dto.PhoneNumberDto;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMapper {

        public PhoneNumberDto toDto(PhoneNumber phoneNumber) {
        return new PhoneNumberDto()
                .withNumber(phoneNumber.getNumber())
                .withCountryCallingCode(phoneNumber.getCountryCallingCode());
    }

        public PhoneNumber toDomain(PhoneNumberDto phoneNumberDto) {
        return PhoneNumber.PhoneNumberBuilder.phoneNumber()
                .withNumber(phoneNumberDto.getNumber())
                .withCountryCallingCode(phoneNumberDto.getCountryCallingCode())
                .build();
    }

}
