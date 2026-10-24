package com.switchfully.eurder.eurder.factory;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.user.Customer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DTOFactory {
    public static CreateCustomerDTO validCreateCustomerDTO() {
        return new CreateCustomerDTO(
                "newUser",
                "user",
                MailFactory.generateRandomMail(),
                "userAddress - 5555",
                "04 555 555 555",
                "pass"
        );
    }

    public static CreateItemDTO validCreateItemDTOWithInfAmount() {
        return new CreateItemDTO(
                "Item",
                "An item",
                1.0d,
                999
        );
    }

    public static CreateItemDTO createItemDTOForItem(Item item, int amount) {
        return new CreateItemDTO(
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                amount
        );
    }

    public static CreateOrderDTO validCreateOrderDTOWithOneItem(Customer customer, Item item) {
        return new CreateOrderDTO(
                Map.of(
                        item.getId(), item.getAmount()
                ),
                customer.getEmail()
        );
    }

    public static CreateOrderDTO validCreateOrderDTOWithMultipleItems(Customer customer, List<Item> items) {
        return new CreateOrderDTO(
                items.stream()
                        .collect(Collectors.toMap(
                                Item::getId,
                                Item::getAmount
                        )),
                customer.getEmail()
        );
    }

    public static CreateOrderDTO validCreateOrderDTOWithRandomItems(Customer customer) {
        List<Item> items = ItemFactory.listOfItemsOfAmountOne(2);

        return new CreateOrderDTO(
                items.stream()
                        .collect(Collectors.toMap(
                                Item::getId,
                                Item::getAmount
                        )),
                customer.getEmail()
        );
    }

    public static CreateOrderDTO validCreateOrderDTOWithGivenItem(Customer customer, Item item, int amount) {

        return new CreateOrderDTO(
                Map.of(item.getId(), amount),
                customer.getEmail()
        );
    }
}
