package com.switchfully.order.config;

import com.switchfully.order.domain.Customer;
import com.switchfully.order.domain.Item;
import com.switchfully.order.domain.Price;
import com.switchfully.order.service.CustomerService;
import com.switchfully.order.service.ItemService;
import com.switchfully.order.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

import static com.switchfully.order.domain.Address.AddressBuilder.address;
import static com.switchfully.order.domain.Customer.CustomerBuilder.customer;
import static com.switchfully.order.domain.Email.EmailBuilder.email;
import static com.switchfully.order.domain.Item.ItemBuilder.item;
import static com.switchfully.order.domain.Order.OrderBuilder.order;
import static com.switchfully.order.domain.OrderItem.OrderItemBuilder.orderItem;
import static com.switchfully.order.domain.PhoneNumber.PhoneNumberBuilder.phoneNumber;
import static java.util.Arrays.asList;

@Configuration
public class SampleDataConfiguration {

    /**
     * Generates some sample / dummy data
     */
    @Bean
    @Profile(value = "dev")
    public CommandLineRunner commandLineRunner(CustomerService customerService, ItemService itemService, OrderService orderService) {
        return (args) -> {
            Customer customerFiona = customerService.createCustomer(customer()
                    .withFirstname("Fiona")
                    .withLastname("Arizonia")
                    .withEmail(email().withLocalPart("fio.ari").withDomain("swampmail.com").withComplete("fio.ari@swampmail.com").build())
                    .withPhoneNumber(phoneNumber().withNumber("488532541").withCountryCallingCode("32").build())
                    .withAddress(address().withCountry("Belgium").withHouseNumber("16A").withPostalCode("3000").withStreetName("Tertiensemarkt").build())
                    .build()
            );
            customerService.createCustomer(customer()
                    .withFirstname("Derek")
                    .withLastname("Bravius")
                    .withEmail(email().withLocalPart("d.bravius").withDomain("gotgot.com").withComplete("d.bravius@gotgot.com").build())
                    .withPhoneNumber(phoneNumber().withNumber("481665584").withCountryCallingCode("32").build())
                    .withAddress(address().withCountry("France").withHouseNumber("88").withPostalCode("58884").withStreetName("Rue Parilyon").build())
                    .build()
            );

            itemService.createItem(item()
                    .withName("iPowd")
                    .withPrice(Price.create(new BigDecimal(495.95)))
                    .withAmountOfStock(50)
                    .withDescription("The iPod is a range of portable music players designed by the company Apple Inc. " +
                            "in California, but made mostly by the Chinese.")
                    .build()
            );
            itemService.createItem(item()
                    .withName("iPhony")
                    .withPrice(Price.create(new BigDecimal(799.99)))
                    .withAmountOfStock(25)
                    .withDescription("The iPhone is a series of smartphones made by Apple Inc since 2007. It does many " +
                            "things that a computer can do, but is small enough to fit in someone's hand.")
                    .build()
            );
            Item itemMacBukPro = itemService.createItem(item()
                    .withName("MacBuk Pro 13-inch")
                    .withPrice(Price.create(new BigDecimal(1999.90)))
                    .withAmountOfStock(15)
                    .withDescription("1.4 GHz quad-core Intel Core i5 Coffee Lake (8257U), up to 3.9 GHz, 6 MB L3 cache, 128 eDRAM")
                    .build()
            );
            itemService.createItem(item()
                    .withName("MacBuk Pro Delux 13-inch")
                    .withPrice(Price.create(new BigDecimal(2999.90)))
                    .withAmountOfStock(10)
                    .withDescription("2.4 GHz quad-core Intel Core i5 Coffee Lake (8279U), up to 4.1 GHz, 6 MB L3 cache, 128 MB eDRAM")
                    .build()
            );
            itemService.createItem(item()
                    .withName("MacBuk Pro 15-inch")
                    .withPrice(Price.create(new BigDecimal(3599)))
                    .withAmountOfStock(2)
                    .withDescription("2.6 GHz six-core Intel Core i7 (9750H) Coffee Lake (9th Gen), up to 4.5 GHz, 12 MB " +
                            "L3 cache Optional 2.4 GHz eight-core Intel Core i9 (9980HK) Coffee Lake (9th Gen), up to 5 GHz, 16 MB L3 cache")
                    .build()
            );
            Item itemMacBukAir = itemService.createItem(item()
                    .withName("MacBuk Air")
                    .withPrice(Price.create(new BigDecimal(3179.99)))
                    .withAmountOfStock(4)
                    .withDescription("1.6 GHz (i5-8210Y) dual‑core Intel Core i5, Turbo Boost 3.6 GHz, with 4 MB L3‑cache")
                    .build()
            );

            orderService.createOrder(order()
                    .withCustomerId(customerFiona.getId())
                    .withOrderItems(
                            asList(
                                    orderItem()
                                            .withItemId(itemMacBukPro.getId())
                                            .withItemPrice(itemMacBukPro.getPrice())
                                            .withOrderedAmount(2)
                                            .withShippingDateBasedOnAvailableItemStock(itemMacBukPro.getAmountOfStock())
                                            .build(),
                                    orderItem()
                                            .withItemId(itemMacBukAir.getId())
                                            .withItemPrice(itemMacBukAir.getPrice())
                                            .withOrderedAmount(1)
                                            .withShippingDateBasedOnAvailableItemStock(itemMacBukAir.getAmountOfStock())
                                            .build()
                            ))
                    .build());
        };
    }
}
