package com.avaloq.dice.app.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RollDiceResult {

    private Integer result;
    private Long amount;
}
