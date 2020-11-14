package com.avaloq.dice.app.api;

import java.math.BigDecimal;
import java.util.List;

import com.avaloq.dice.app.exception.NoSimulationsException;
import com.avaloq.dice.app.model.RollDistribution;
import com.avaloq.dice.app.model.TotalNumberSimulation;
import com.avaloq.dice.app.service.StatisticsService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticsController.class)
public class StatisticsControllerMockedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService service;

    private static final List<TotalNumberSimulation> totalNumberSimulations = Lists.list(
            TotalNumberSimulation.builder().diceNo(2).sidesNo(4).totalRolls(10L).totalSimulations(2L).build(),
            TotalNumberSimulation.builder().diceNo(3).sidesNo(6).totalRolls(50L).totalSimulations(10L).build());

    private static final List<RollDistribution> distribution = Lists.list(
            RollDistribution.builder().sum(5).percentage(BigDecimal.valueOf(5.1)).build(),
            RollDistribution.builder().sum(6).percentage(BigDecimal.valueOf(15.5)).build(),
            RollDistribution.builder().sum(12).percentage(BigDecimal.valueOf(25.6)).build());

    @Test
    void Statistics_Exeption_On_Statistics_Retrival_Result_FAIL() throws Exception {
        when(service.statsticsTotalNumber()).thenThrow(NoSimulationsException.class);

        this.mockMvc.perform(get("/statistics"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    void Statistics_Empty_Statistics_Result_OK() throws Exception {
        when(service.statsticsTotalNumber()).thenReturn(Lists.emptyList());

        this.mockMvc.perform(get("/statistics"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("{ \"total\":[] }"));
    }

    @Test
    void Statistics_Just_Total_When_No_Query_Params_No_Distribution_Data_Result_OK() throws Exception {
        when(service.statsticsTotalNumber()).thenReturn(totalNumberSimulations);
        when(service.relativeDistribution(anyInt(), anyInt())).thenReturn(Lists.list());

        this.mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.total").isArray())
                .andExpect(jsonPath("$.total", hasSize(2)))
                .andExpect(jsonPath("$.total[0].diceNo", is(2)))
                .andExpect(jsonPath("$.total[0].totalSimulations", is(2)))
                .andExpect(jsonPath("$.total[1].sidesNo", is(6)))
                .andExpect(jsonPath("$.total[1].totalRolls", is(50)))
                .andExpect(jsonPath("$.distribution").doesNotExist());
    }

    @Test
    void Statistics_Just_Total_When_No_Query_Params_With_Distribution_Data_Result_OK() throws Exception {
        when(service.statsticsTotalNumber()).thenReturn(totalNumberSimulations);
        when(service.relativeDistribution(anyInt(), anyInt())).thenReturn(distribution);

        this.mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.total").isArray())
                .andExpect(jsonPath("$.total", hasSize(2)))
                .andExpect(jsonPath("$.total[0].diceNo", is(2)))
                .andExpect(jsonPath("$.total[0].totalSimulations", is(2)))
                .andExpect(jsonPath("$.total[1].sidesNo", is(6)))
                .andExpect(jsonPath("$.total[1].totalRolls", is(50)))
                .andExpect(jsonPath("$.distribution").doesNotExist());
    }

    @Test
    void Statistics_Just_Total_When_Query_Params_And_No_Distribution_Data_Result_OK() throws Exception {
        when(service.statsticsTotalNumber()).thenReturn(totalNumberSimulations);
        when(service.relativeDistribution(anyInt(), anyInt())).thenReturn(Lists.emptyList());

        this.mockMvc.perform(get("/statistics").param("dice","2").param("sides","5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.total").isArray())
                .andExpect(jsonPath("$.total", hasSize(2)))
                .andExpect(jsonPath("$.total[0].diceNo", is(2)))
                .andExpect(jsonPath("$.total[0].totalSimulations", is(2)))
                .andExpect(jsonPath("$.total[1].sidesNo", is(6)))
                .andExpect(jsonPath("$.total[1].totalRolls", is(50)))
                .andExpect(jsonPath("$.distribution").doesNotExist());
    }

    @Test
    void Statistics_Just_Total_When_Query_Params_And_Distribution_Exists_Data_Result_OK() throws Exception {
        when(service.statsticsTotalNumber()).thenReturn(totalNumberSimulations);
        when(service.relativeDistribution(anyInt(), anyInt())).thenReturn(distribution);

        this.mockMvc.perform(get("/statistics").param("dice","2").param("sides","5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.total").isArray())
                .andExpect(jsonPath("$.total", hasSize(2)))
                .andExpect(jsonPath("$.total[0].diceNo", is(2)))
                .andExpect(jsonPath("$.total[0].totalSimulations", is(2)))
                .andExpect(jsonPath("$.total[1].sidesNo", is(6)))
                .andExpect(jsonPath("$.total[1].totalRolls", is(50)))
                .andExpect(jsonPath("$.distribution").exists())
                .andExpect(jsonPath("$.distribution").isArray())
                .andExpect(jsonPath("$.distribution", hasSize(3)))
                .andExpect(jsonPath("$.distribution[0].sum", is(5)))
                .andExpect(jsonPath("$.distribution[0].percentage", is(5.1)))
                .andExpect(jsonPath("$.distribution[1].sum", is(6)))
                .andExpect(jsonPath("$.distribution[1].percentage", is(15.5)))
                .andExpect(jsonPath("$.distribution[2].sum", is(12)))
                .andExpect(jsonPath("$.distribution[2].percentage", is(25.6)));
    }
}
