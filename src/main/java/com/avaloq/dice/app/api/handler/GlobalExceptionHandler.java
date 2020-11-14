package com.avaloq.dice.app.api.handler;

import com.avaloq.dice.app.exception.DiceRollInputDataValidationException;
import com.avaloq.dice.app.exception.DiceSidesAmountException;
import com.avaloq.dice.app.exception.NoSimulationsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ DiceSidesAmountException.class, DiceRollInputDataValidationException.class})
    public void handleRollDiceValidation() { }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler({ NoSimulationsException.class})
    public void handleNoSimulations() { }

}
