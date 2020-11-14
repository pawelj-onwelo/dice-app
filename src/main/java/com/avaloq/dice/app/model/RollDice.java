package com.avaloq.dice.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RollDice {

    private Integer result;
    private Long amount;
}
