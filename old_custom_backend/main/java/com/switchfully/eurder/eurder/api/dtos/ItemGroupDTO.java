package com.switchfully.eurder.eurder.api.dtos;

import java.math.BigDecimal;

public record ItemGroupDTO(String itemName, int orderedAmount, BigDecimal totalPrice) {}