package com.switchfully.eurder.eurder.api.dtos;
import java.util.List;

public record CreateOrderDTO(List<CreateItemGroupDTO> itemGroups, Long customerId) {}
