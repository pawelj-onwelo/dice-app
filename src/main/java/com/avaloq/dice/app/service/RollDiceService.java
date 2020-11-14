package com.avaloq.dice.app.service;

import java.util.Random;

import com.avaloq.dice.app.exception.DiceSidesAmountException;
import org.springframework.stereotype.Service;

@Service
public class RollDiceService {

    private Random random = new Random();

    /**
     * Roll a single dice
     * @param sides Amount of dice sides (at least 4)
     * @return Roll result
     */
    public int rollDice(int sides) {
        if (sides <= 3) {
            throw new DiceSidesAmountException();
        }
        int result = random.nextInt(sides);

        while (result == 0) {
            result = random.nextInt(sides);
        }
        return result;
    }
}
