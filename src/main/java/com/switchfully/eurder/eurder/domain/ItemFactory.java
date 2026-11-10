package com.switchfully.eurder.eurder.domain;

import java.math.BigDecimal;

public class ItemFactory {

    public static ItemBuilder itemBuilder() {
        return new ItemBuilder();
    }

    public static class ItemBuilder {
        private String name = "Item";
        private String description = "An item";
        private BigDecimal price = new BigDecimal("1.00");
        private int amount = 200;

        public ItemBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ItemBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ItemBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ItemBuilder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public Item build() {
            return new Item(name, description, price, amount);
        }
    }

    public static Item itemWithInfiniteSupply() {
        return itemBuilder().withAmount(999)
                .build();
    }

    public static Item itemWithAmount(int amount) {
        return itemBuilder()
                .withAmount(amount)
                .build();
    }
}
