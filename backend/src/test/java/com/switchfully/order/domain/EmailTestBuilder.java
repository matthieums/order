package com.switchfully.order.domain;

import com.switchfully.order.domain.Email.EmailBuilder;

public class EmailTestBuilder {

    private EmailBuilder emailBuilder;

    private EmailTestBuilder(EmailBuilder emailBuilder) {
        this.emailBuilder = emailBuilder;
    }

    public static EmailTestBuilder anEmptyEmail() {
        return new EmailTestBuilder(EmailBuilder.email());
    }

    public static EmailTestBuilder anEmail() {
        return new EmailTestBuilder(EmailBuilder.email()
                .withLocalPart("niels")
                .withDomain("mymail.be")
                .withComplete("niels@mymail.be"));
    }

        public Email build() {
        return emailBuilder.build();
    }

    public EmailTestBuilder withLocalPart(String localPart) {
        emailBuilder.withLocalPart(localPart);
        return this;
    }

    public EmailTestBuilder withDomain(String domain) {
        emailBuilder.withDomain(domain);
        return this;
    }

    public EmailTestBuilder withComplete(String complete) {
        emailBuilder.withComplete(complete);
        return this;
    }


}