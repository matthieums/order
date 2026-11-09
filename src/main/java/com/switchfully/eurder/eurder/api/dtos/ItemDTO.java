package com.switchfully.eurder.eurder.api.dtos;

import java.math.BigDecimal;

public record ItemDTO(Long id, String name, String description, BigDecimal price, int amount) {
}
