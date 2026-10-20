package com.switchfully.eurder.eurder.api.dtos;

import com.switchfully.eurder.eurder.domain.ItemGroup;

import java.util.List;

public record OrderDTO(String id, List<ItemGroup> items, String placedBy) {
}