package com.avaloq.dice.app.api.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RollDistributionResponse {

    Integer sum;
    BigDecimal percentage;
}
