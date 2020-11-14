package com.avaloq.dice.app.service;

import java.util.List;
import java.util.Random;

import com.avaloq.dice.app.exception.DiceRollInputDataValidationException;
import com.avaloq.dice.app.model.RollDice;
import com.avaloq.dice.app.repository.RollDiceRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RollMultipleDiceTest {

    RollDiceRepository rollDiceRepository;
    RollMultipleDiceService rollDiceService;
    Random random = new Random();

    @BeforeAll
    void setUp() {
        rollDiceRepository = mock(RollDiceRepository.class);
        rollDiceService = new RollMultipleDiceService(new RollDiceService(), rollDiceRepository);
    }

    @Test
    public void Roll_Single_Dice_Once_OK() {
        assertThat(rollDiceService.singleRollDice(1, 6)).isBetween(1, 6);
    }

    @Test
    public void Roll_Multiple_Dice_Once_OK() {
        final int numberOfDice = 4;
        final int numberOfSides = 6;
        assertThat(rollDiceService.singleRollDice(numberOfDice, numberOfSides)).isBetween(1, numberOfDice * numberOfSides);
    }

    @Test
    public void Roll_Multiple_Dice_Sum_OK() {
        RollMultipleDiceService rollDiceService = new RollMultipleDiceService(new RollDiceService() {
            @Override public int rollDice(int sides) {
                return sides;
            }
        }, rollDiceRepository);
        assertThat(rollDiceService.singleRollDice(4, 6)).isEqualTo(4*6);
    }

    @Test
    public void Roll_Dice_Invalid_No_Dice_FAIL() {
        assertThatThrownBy(() -> rollDiceService.rollMultipleDice(0, 6, 1))
            .isInstanceOf(DiceRollInputDataValidationException.class);
    }

    @Test
    public void Roll_Dice_Invalid_No_Sides_FAIL() {
        assertThatThrownBy(() -> rollDiceService.rollMultipleDice(1, 0, 1))
                .isInstanceOf(DiceRollInputDataValidationException.class);
    }

    @Test
    public void Roll_Dice_Invalid_No_Rolls_FAIL() {
        assertThatThrownBy(() -> rollDiceService.rollMultipleDice(1, 6, 0))
                .isInstanceOf(DiceRollInputDataValidationException.class);
    }

    @Test
    public void Roll_Dice_Invalid_3_Sides_FAIL() {
        assertThatThrownBy(() -> rollDiceService.rollMultipleDice(1, 3, 1))
                .isInstanceOf(DiceRollInputDataValidationException.class);
    }

    @Test
    public void Roll_Dice_Invalid_4_Sides_OK() {
        assertThat(rollDiceService.rollMultipleDice(1, 4, 1)).isInstanceOf(List.class);
    }

    @Test
    public void Roll_Multiple_Dice_Multiple_Times_Simple_OK() {
        final int numberOfDice = 3;
        final int numberOfSides = 6;
        final int numberOfTimes = random.nextInt(1000);

        final List<RollDice> rollResults = rollDiceService.rollMultipleDice(numberOfDice, numberOfSides, numberOfTimes);

        assertThat(rollResults).isNotEmpty();
    }

    @Test
    public void Roll_Multiple_Dice_Multiple_Times_Single_Result_OK() {
        final int numberOfDice = 3;
        final int numberOfSides = 6;
        final int numberOfTimes = random.nextInt(1000);

        final int MAX_RESULT = numberOfDice * numberOfSides;

        RollMultipleDiceService mockedRollDiceService = new RollMultipleDiceService(new RollDiceService() {
            @Override public int rollDice(int sides) {
                return sides;
            }
        }, rollDiceRepository);

        final List<RollDice> rollResults = mockedRollDiceService.rollMultipleDice(numberOfDice, numberOfSides, numberOfTimes);

        assertThat(rollResults).isNotEmpty().hasSize(1);
        assertThat(rollResults).extracting("result", Integer.class).element(0).isEqualTo(Integer.valueOf(MAX_RESULT));
        assertThat(rollResults).extracting("amount", Long.class).element(0).isEqualTo(Long.valueOf(numberOfTimes));
    }

    @Test
    public void Roll_Multiple_Dice_Multiple_Times_Two_Results_Elements_OK() {
        final int numberOfDice = 3;
        final int numberOfSides = 6;
        final int numberOfTimes = random.nextInt(1000);

        final int MAX_RESULT = numberOfDice * numberOfSides;

        RollMultipleDiceService mockedRollDiceService = new RollMultipleDiceService(new RollDiceService() {
            private int counter = 0;
            @Override public int rollDice(int sides) {
                return ++counter % 2;
            }
        }, rollDiceRepository);

        final List<RollDice> rollResults = mockedRollDiceService.rollMultipleDice(numberOfDice, numberOfSides, numberOfTimes);

        assertThat(rollResults).isNotEmpty();
        assertThat(rollResults).hasSize(2);
        assertThat(rollResults).extracting("result", Integer.class).contains(1, 2);
        assertThat(rollResults).element(0).isNotNull();
        assertThat(rollResults.get(0).getAmount()).isCloseTo(numberOfTimes/2, within(1l));
        assertThat(rollResults).element(1).isNotNull();
        assertThat(rollResults.get(1).getAmount()).isCloseTo(numberOfTimes/2, within(1l));
    }
}
