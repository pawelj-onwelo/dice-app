package com.avaloq.dice.app.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalNumberSimulation {

    private Integer diceNo;
    private Integer sidesNo;
    private Long totalSimulations;
    private Long totalRolls;
}
