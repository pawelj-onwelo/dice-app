package com.avaloq.dice.app.service;

import java.util.Random;

import com.avaloq.dice.app.exception.DiceSidesAmountException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RollDiceTest {


    private RollDiceService rollDiceService = new RollDiceService();

    @Test
    public void Roll_Dice_OK() {
        assertThat(rollDiceService.rollDice(6)).isBetween(1,6);
    }

    @Test
    public void Roll_Dice_Random_OK() {
                new Random()
                        .ints(1000, 4, Integer.MAX_VALUE)
                        .forEach(i -> assertThat(rollDiceService.rollDice(i)).isBetween(1, i));
    }

    @Test
    public void Roll_Dice_Sides_Is_3() {
        assertThatThrownBy(() -> rollDiceService.rollDice(3)).isInstanceOf(DiceSidesAmountException.class);

    }

    @Test
    public void Roll_Dice_Sides_Is_0() {
        assertThatThrownBy(() -> rollDiceService.rollDice(0)).isInstanceOf(DiceSidesAmountException.class);

    }

    @Test
    public void Roll_Dice_Sides_Is_Less_Than_0() {
        assertThatThrownBy(() -> rollDiceService.rollDice(-5)).isInstanceOf(DiceSidesAmountException.class);
    }
}
