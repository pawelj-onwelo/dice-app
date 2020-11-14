package com.avaloq.dice.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.avaloq.dice.app.exception.DiceRollInputDataValidationException;
import com.avaloq.dice.app.model.RollDice;
import com.avaloq.dice.app.repository.RollDiceRepository;
import com.avaloq.dice.app.repository.model.RollDiceResultEntity;
import com.avaloq.dice.app.repository.model.RollDiceResultItemEntity;
import org.springframework.stereotype.Service;

@Service
public class RollMultipleDiceService {

    RollDiceService rollDiceService;

    RollDiceRepository rollDiceRepository;

    public RollMultipleDiceService(RollDiceService rollDiceService,
                                   RollDiceRepository rollDiceRepository) {
        this.rollDiceService = rollDiceService;
        this.rollDiceRepository = rollDiceRepository;
    }

    /**
     * Simulation of roll dice which has sides defined amount of times
     * @param numberOfDice dice amount
     * @param numberOfDiceSides single dice amount
     * @param numberOfRolls Simulations amount - how many times multiple dice will be rolled
     * @return
     */
    public List<RollDice> rollMultipleDice(int numberOfDice, int numberOfDiceSides, int numberOfRolls) {
        validateRollDice(numberOfDice, numberOfDiceSides, numberOfRolls);

        List<RollDice> result = IntStream.range(0, numberOfRolls)
                                            .boxed()
                                            .map(i -> singleRollDice(numberOfDice, numberOfDiceSides))
                                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                            .entrySet().stream()
                                            .map(e -> new RollDice(e.getKey(), e.getValue()))
                                            .collect(Collectors.toList());

        saveRollDiceData(numberOfDice, numberOfDiceSides, result);

        return result;
    }

    /**
     * Single roll of amount of dice provided in input parameter
     * @param numberOfDice dice number
     * @param numberOfDiceSides dice sides number
     * @return
     */
    int singleRollDice(int numberOfDice, int numberOfDiceSides) {
        return IntStream.range(0, numberOfDice)
                .map(i -> rollDiceService.rollDice(numberOfDiceSides))
                .sum();
    }

    /**
     * Save Simulation Data to DB
     * @param numberOfDice dice number
     * @param numberOfDiceSides sides number
     * @param rollDiceResults Simulations result
     */
    void saveRollDiceData(int numberOfDice, int numberOfDiceSides, List<RollDice> rollDiceResults) {
        RollDiceResultEntity sample = new RollDiceResultEntity();

        sample.setDiceNo(numberOfDice);
        sample.setSidesNo(numberOfDiceSides);

        sample.setResultItems(rollDiceResults.stream()
                        .map(r -> new RollDiceResultItemEntity(null, sample, r.getResult(), r.getAmount()))
                        .collect(Collectors.toCollection(() -> new HashSet<RollDiceResultItemEntity>())));


        rollDiceRepository.save(sample);
    }

    private void validateRollDice(int numberOfDice, int numberOfDiceSides, int numberOfRolls) {
        if (numberOfRolls <= 0 || numberOfDice <= 0 || numberOfDiceSides < 4) {
            throw new DiceRollInputDataValidationException();
        }
    }
}
