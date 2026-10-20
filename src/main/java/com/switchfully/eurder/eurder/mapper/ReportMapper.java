package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.Eurder;

import java.util.List;

public class ReportMapper {
    public static ReportDTO toDto(List<Eurder> orders) {
        return new ReportDTO(
                orders.stream()
                        .map(OrderMapper::toDTO)
                        .toList(),
                orders.stream().mapToDouble(Eurder::getTotalPrice)
                        .sum()
        );
    }
}
