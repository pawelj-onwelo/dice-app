package com.avaloq.dice.app.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RollDistribution {

    Integer sum;
    BigDecimal percentage;
}
