package com.avaloq.dice.app.api;

import java.util.Collections;

import com.avaloq.dice.app.model.RollDice;
import com.avaloq.dice.app.service.RollMultipleDiceService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RollDiceController.class)
public class RollDiceControllerMockedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RollMultipleDiceService service;

    @Test
    void Roll_Dice_Empty_Result_OK() throws Exception {
        when(service.rollMultipleDice(anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        this.mockMvc.perform(post("/roll").param("dice", "1").param("sides","1").param("rolls", "1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("[]"));
    }

    @Test
    void Roll_Dice_Full_Result_OK() throws Exception {
        when(service.rollMultipleDice(anyInt(), anyInt(), anyInt())).thenReturn(Lists.list(new RollDice(10, 1L), new RollDice(4, 4L)));

        this.mockMvc.perform(post("/roll").param("dice", "1").param("sides","1").param("rolls", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"result\":10,\"amount\":1},{\"result\":4,\"amount\":4}]"));
    }

    @Test
    void Roll_Dice__Missing_dice_query_param_FAIL() throws Exception {
        when(service.rollMultipleDice(anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        this.mockMvc.perform(post("/roll").param("sides","1").param("rolls", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Roll_Dice__Missing_sides_query_param_FAIL() throws Exception {
        when(service.rollMultipleDice(anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        this.mockMvc.perform(post("/roll").param("dice","1").param("rolls", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Roll_Dice__Missing_rolls_query_param_FAIL() throws Exception {
        when(service.rollMultipleDice(anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        this.mockMvc.perform(post("/roll").param("dice","1").param("sides", "1"))
                .andExpect(status().isBadRequest());
    }
}
