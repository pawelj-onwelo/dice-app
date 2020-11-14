package com.avaloq.dice.app.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalNumberSimulationResponse {

    private Integer diceNo;
    private Integer sidesNo;
    private Long totalSimulations;
    private Long totalRolls;
}
