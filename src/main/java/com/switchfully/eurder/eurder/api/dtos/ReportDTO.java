package com.switchfully.eurder.eurder.api.dtos;

import java.util.List;

public class ReportDTO {
    List<OrderDetailsDTO> orderDetailsDTOS;
    private final double totalPrice;

    public ReportDTO(List<OrderDetailsDTO> orderDetailsDTOS, double totalPrice) {
        this.orderDetailsDTOS = orderDetailsDTOS;
        this.totalPrice = totalPrice;
    }

    public List<OrderDetailsDTO> getOrderDTOS() {
        return orderDetailsDTOS;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
