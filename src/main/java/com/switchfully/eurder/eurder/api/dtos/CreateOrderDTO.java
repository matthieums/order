package com.switchfully.eurder.eurder.api.dtos;

import com.switchfully.eurder.eurder.domain.ItemGroup;

import java.util.List;
import java.util.Map;

public record CreateOrderDTO(Map<String, Integer> itemIdsToAmounts, String placedBy) {}
