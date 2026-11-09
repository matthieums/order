package com.switchfully.eurder.eurder.api.dtos;

import com.switchfully.eurder.eurder.domain.user.Customer;

import java.util.List;

public record ReportDTO(Long customerId, List<OrderReportDTO> orders) {}
