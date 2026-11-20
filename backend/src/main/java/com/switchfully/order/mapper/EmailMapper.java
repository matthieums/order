package com.switchfully.order.mapper;

import com.switchfully.order.domain.Email;
import com.switchfully.order.dto.EmailDto;
import org.springframework.stereotype.Component;

import static com.switchfully.order.domain.Email.EmailBuilder.email;

@Component
public class EmailMapper {

        public EmailDto toDto(Email email) {
        return new EmailDto()
                .withLocalPart(email.getLocalPart())
                .withDomain(email.getDomain())
                .withComplete(email.getComplete());
    }

        public Email toDomain(EmailDto emailDto) {
        return email()
                .withLocalPart(emailDto.getLocalPart())
                .withDomain(emailDto.getDomain())
                .withComplete(emailDto.getComplete())
                .build();
    }
}
