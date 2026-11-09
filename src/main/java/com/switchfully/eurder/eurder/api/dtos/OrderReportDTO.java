package com.switchfully.eurder.eurder.api.dtos;

import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;

import java.util.List;

public record OrderReportDTO(Customer customer, Eurder order, List<ItemGroup> itemGroups) {}
