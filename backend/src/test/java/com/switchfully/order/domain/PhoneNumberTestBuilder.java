package com.switchfully.order.domain;

import com.switchfully.order.domain.PhoneNumber.PhoneNumberBuilder;

public class PhoneNumberTestBuilder {

    private PhoneNumberBuilder phoneNumberBuilder;

    private PhoneNumberTestBuilder(PhoneNumberBuilder phoneNumberBuilder) {
        this.phoneNumberBuilder = phoneNumberBuilder;
    }

    public static PhoneNumberTestBuilder anEmptyPhoneNumber() {
        return new PhoneNumberTestBuilder(PhoneNumberBuilder.phoneNumber());
    }

    public static PhoneNumberTestBuilder aPhoneNumber() {
        return new PhoneNumberTestBuilder(PhoneNumberBuilder.phoneNumber()
                .withNumber("484554433")
                .withCountryCallingCode("+32")
        );
    }

        public PhoneNumber build() {
        return phoneNumberBuilder.build();
    }

    public PhoneNumberTestBuilder withNumber(String number) {
        phoneNumberBuilder.withNumber(number);
        return this;
    }

    public PhoneNumberTestBuilder withCountryCallingCode(String countryCallingCode) {
        phoneNumberBuilder.withCountryCallingCode(countryCallingCode);
        return this;
    }
}