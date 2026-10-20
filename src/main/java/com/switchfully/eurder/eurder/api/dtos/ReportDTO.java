package com.switchfully.eurder.eurder.api.dtos;

import com.switchfully.eurder.eurder.domain.ItemGroup;

import java.util.List;

public class ReportDTO {
    List<OrderDTO> orderDTOS;
    private final double totalPrice;

    public ReportDTO(List<OrderDTO> orderDTOS,double totalPrice) {
        this.orderDTOS = orderDTOS;
        this.totalPrice = totalPrice;
    }

    public List<OrderDTO> getOrderDTOS() {
        return orderDTOS;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
