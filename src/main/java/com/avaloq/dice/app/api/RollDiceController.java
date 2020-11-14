package com.avaloq.dice.app.api;

import java.util.List;
import java.util.stream.Collectors;

import com.avaloq.dice.app.api.dto.RollDiceResult;
import com.avaloq.dice.app.service.RollMultipleDiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roll")
public class RollDiceController {

    private RollMultipleDiceService rollMultipleDiceService;

    public RollDiceController(RollMultipleDiceService rollMultipleDiceService) {
        this.rollMultipleDiceService = rollMultipleDiceService;
    }

    @PostMapping
    public ResponseEntity<List<RollDiceResult>> rollDice(
            @RequestParam("dice") int numberOfDice,
            @RequestParam("sides") int numberOfDiceSides,
            @RequestParam("rolls") int numberOfRolls) {

        List<RollDiceResult> result = rollMultipleDiceService
                                            .rollMultipleDice(numberOfDice, numberOfDiceSides, numberOfRolls)
                                            .stream()
                                            .map(e -> RollDiceResult.builder()
                                                                    .amount(e.getAmount())
                                                                    .result(e.getResult())
                                                                    .build())
                                            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
