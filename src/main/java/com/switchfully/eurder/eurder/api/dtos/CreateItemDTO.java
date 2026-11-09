package com.switchfully.eurder.eurder.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class CreateItemDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Positive
    private BigDecimal price;
    @PositiveOrZero
    private int amount;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public CreateItemDTO(String name, String description, BigDecimal price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;


    }
}
