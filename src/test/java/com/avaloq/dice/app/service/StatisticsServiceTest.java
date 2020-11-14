package com.avaloq.dice.app.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.avaloq.dice.app.exception.NoSimulationsException;
import com.avaloq.dice.app.model.RollDistribution;
import com.avaloq.dice.app.model.TotalNumberSimulation;
import com.avaloq.dice.app.repository.RollDiceItemRepository;
import com.avaloq.dice.app.repository.RollDiceRepository;
import com.avaloq.dice.app.repository.model.RollDiceResultEntity;
import com.avaloq.dice.app.repository.model.RollDiceResultItemEntity;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatisticsServiceTest {

    RollDiceRepository rollDiceRepository;
    RollDiceItemRepository rollDiceItemRepository;
    StatisticsService statisticsService;

    @BeforeAll
    void setUp() {
        rollDiceRepository = mock(RollDiceRepository.class);
        rollDiceItemRepository = mock(RollDiceItemRepository.class);
        statisticsService = new StatisticsService(rollDiceRepository, rollDiceItemRepository);
    }

    @Test
    void Total_Statistics_Empty_Data_OK() {
        when(rollDiceRepository.findAll()).thenReturn(Collections.emptyList());

        final List<TotalNumberSimulation> result = statisticsService.statsticsTotalNumber();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void Total_Statistics_Single_Result_No_Items_Data_OK() {
        when(rollDiceRepository.findAll()).thenReturn(Lists.list(
                new RollDiceResultEntity(1L, 1, 5, Collections.emptySet())));

        final List<TotalNumberSimulation> result = statisticsService.statsticsTotalNumber();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result).extracting("totalSimulations", Long.class).element(0).isEqualTo(1L);
        assertThat(result).extracting("totalRolls", Long.class).element(0).isEqualTo(0L);
        assertThat(result).extracting("diceNo", Integer.class).element(0).isEqualTo(1);
        assertThat(result).extracting("sidesNo", Integer.class).element(0).isEqualTo(5);
    }

    @Test
    void Total_Statistics_Single_Complete_Data_OK() {
        when(rollDiceRepository.findAll()).thenReturn(Lists.list(
                new RollDiceResultEntity(1L, 2, 4, Set.of(new RollDiceResultItemEntity(1L, null, 1, 2L)))));

        final List<TotalNumberSimulation> result = statisticsService.statsticsTotalNumber();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result).extracting("totalSimulations", Long.class).element(0).isEqualTo(1L);
        assertThat(result).extracting("totalRolls", Long.class).element(0).isEqualTo(2L);
        assertThat(result).extracting("diceNo", Integer.class).element(0).isEqualTo(2);
        assertThat(result).extracting("sidesNo", Integer.class).element(0).isEqualTo(4);
    }

    @Test
    void Total_Statistics_Single_Duplicated_Complete_Data_OK() {
        when(rollDiceRepository.findAll()).thenReturn(Lists.list(
                new RollDiceResultEntity(1L, 2, 4, Set.of(new RollDiceResultItemEntity(1L, null, 1, 2L),
                                                          new RollDiceResultItemEntity(2L, null, 2, 5L)))));

        final List<TotalNumberSimulation> result = statisticsService.statsticsTotalNumber();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result).extracting("totalSimulations", Long.class).element(0).isEqualTo(1L);
        assertThat(result).extracting("totalRolls", Long.class).element(0).isEqualTo(7L);
        assertThat(result).extracting("diceNo", Integer.class).element(0).isEqualTo(2);
        assertThat(result).extracting("sidesNo", Integer.class).element(0).isEqualTo(4);
    }

    @Test
    void Total_Statistics_Multi_The_Same_Complete_Data_OK() {
        when(rollDiceRepository.findAll()).thenReturn(Lists.list(
                new RollDiceResultEntity(1L, 2, 4, Set.of(new RollDiceResultItemEntity(1L, null, 1, 2L),
                                                          new RollDiceResultItemEntity(2L, null, 1, 3L)))));

        final List<TotalNumberSimulation> result = statisticsService.statsticsTotalNumber();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result).extracting("totalSimulations", Long.class).element(0).isEqualTo(1L);
        assertThat(result).extracting("totalRolls", Long.class).element(0).isEqualTo(5L);
        assertThat(result).extracting("diceNo", Integer.class).element(0).isEqualTo(2);
        assertThat(result).extracting("sidesNo", Integer.class).element(0).isEqualTo(4);
    }

    @Test
    void Total_Statistics_Multi_Multi_Complete_Data_OK() {
        when(rollDiceRepository.findAll()).thenReturn(Lists.list(
                new RollDiceResultEntity(1L, 2, 4, Set.of(new RollDiceResultItemEntity(1L, null, 1, 2L))),
                new RollDiceResultEntity(2L, 2, 4, Set.of(new RollDiceResultItemEntity(2L, null, 1, 3L))),
                new RollDiceResultEntity(3L, 2, 5, Set.of(new RollDiceResultItemEntity(3L, null, 1, 20L))),
                new RollDiceResultEntity(4L, 1, 4, Set.of(new RollDiceResultItemEntity(4L, null, 1, 30L)))));

        final List<TotalNumberSimulation> result = statisticsService.statsticsTotalNumber();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty().hasSize(3);

        // 1
        assertThat(result).extracting("totalSimulations", Long.class).element(0).isEqualTo(1L);
        assertThat(result).extracting("totalRolls", Long.class).element(0).isEqualTo(30L);
        assertThat(result).extracting("diceNo", Integer.class).element(0).isEqualTo(1);
        assertThat(result).extracting("sidesNo", Integer.class).element(0).isEqualTo(4);
        // 2
        assertThat(result).extracting("totalSimulations", Long.class).element(1).isEqualTo(2L);
        assertThat(result).extracting("totalRolls", Long.class).element(1).isEqualTo(5L);
        assertThat(result).extracting("diceNo", Integer.class).element(1).isEqualTo(2);
        assertThat(result).extracting("sidesNo", Integer.class).element(1).isEqualTo(4);
        // 3
        assertThat(result).extracting("totalSimulations", Long.class).element(2).isEqualTo(1L);
        assertThat(result).extracting("totalRolls", Long.class).element(2).isEqualTo(20L);
        assertThat(result).extracting("diceNo", Integer.class).element(2).isEqualTo(2);
        assertThat(result).extracting("sidesNo", Integer.class).element(2).isEqualTo(5);
    }

    @Test
    void Relative_Distribution_Null_Data_Exception_FAIL() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(null);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt())).thenReturn(Lists.emptyList());

        assertThatThrownBy(() -> statisticsService.relativeDistribution(1, 1)).isInstanceOf(NoSimulationsException.class);
    }

    @Test
    void Relative_Distribution_Empty_Data_Exception_FAIL() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(0L);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt())).thenReturn(Lists.emptyList());

        assertThatThrownBy(() -> statisticsService.relativeDistribution(1, 1)).isInstanceOf(NoSimulationsException.class);
    }

    @Test
    void Relative_Distribution_Empty_Data_OK() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(1L);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt())).thenReturn(Lists.emptyList());

        final List<RollDistribution> distributions = statisticsService.relativeDistribution(1, 1);

        assertThat(distributions).isNotNull();
        assertThat(distributions).isEmpty();
    }

    @Test
    void Relative_Distribution_Single_Sum_Simple_Data_100prc_OK() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(1L);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt()))
                .thenReturn(Lists.list(
                        new RollDiceResultItemEntity(1L, null, 1, 1L)
                                      ));

        final List<RollDistribution> distributions = statisticsService.relativeDistribution(1, 1);

        assertThat(distributions).isNotNull();
        assertThat(distributions).isNotEmpty().hasSize(1);
        assertThat(distributions).extracting("sum", Integer.class).element(0).isEqualTo(1);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(0).isEqualTo(BigDecimal.valueOf(100).setScale(2));
    }

    @Test
    void Relative_Distribution_Single_Sum_Simple_Data_1prc_OK() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(100L);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt()))
                .thenReturn(Lists.list(
                        new RollDiceResultItemEntity(1L, null, 1, 1L)
                                      ));

        final List<RollDistribution> distributions = statisticsService.relativeDistribution(1, 1);

        assertThat(distributions).isNotNull();
        assertThat(distributions).isNotEmpty().hasSize(1);
        assertThat(distributions).extracting("sum", Integer.class).element(0).isEqualTo(1);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(0).isEqualTo(BigDecimal.valueOf(1).setScale(2));
    }

    @Test
    void Relative_Distribution_Single_Sum_Simple_Data_5prc_OK() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(100L);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt()))
                .thenReturn(Lists.list(
                        new RollDiceResultItemEntity(1L, null, 1, 5L)
                                      ));

        final List<RollDistribution> distributions = statisticsService.relativeDistribution(1, 1);

        assertThat(distributions).isNotNull();
        assertThat(distributions).isNotEmpty().hasSize(1);
        assertThat(distributions).extracting("sum", Integer.class).element(0).isEqualTo(1);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(0).isEqualTo(BigDecimal.valueOf(5).setScale(2));
    }

    @Test
    void Relative_Distribution_Single_Sum_Multiple_Data_15prc_OK() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(100L);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt()))
                .thenReturn(Lists.list(
                        new RollDiceResultItemEntity(1L, null, 1, 5L),
                        new RollDiceResultItemEntity(1L, null, 1, 5L),
                        new RollDiceResultItemEntity(1L, null, 1, 5L)
                                      ));

        final List<RollDistribution> distributions = statisticsService.relativeDistribution(1, 1);

        assertThat(distributions).isNotNull();
        assertThat(distributions).isNotEmpty().hasSize(1);
        assertThat(distributions).extracting("sum", Integer.class).element(0).isEqualTo(1);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(0).isEqualTo(BigDecimal.valueOf(15).setScale(2));
    }

    @Test
    void Relative_Distribution_Multiple_Sum_Multiple_Data_15prc_OK() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(100L);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt()))
                .thenReturn(Lists.list(
                        new RollDiceResultItemEntity(1L, null, 1, 5L),
                        new RollDiceResultItemEntity(1L, null, 2, 1L),
                        new RollDiceResultItemEntity(1L, null, 2, 3L),
                        new RollDiceResultItemEntity(1L, null, 1, 5L),
                        new RollDiceResultItemEntity(1L, null, 1, 5L)
                                      ));

        final List<RollDistribution> distributions = statisticsService.relativeDistribution(1, 1);

        assertThat(distributions).isNotNull();
        assertThat(distributions).isNotEmpty().hasSize(2);
        // 1
        assertThat(distributions).extracting("sum", Integer.class).element(0).isEqualTo(1);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(0).isEqualTo(BigDecimal.valueOf(15).setScale(2));

        // 2
        assertThat(distributions).extracting("sum", Integer.class).element(1).isEqualTo(2);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(1).isEqualTo(BigDecimal.valueOf(4).setScale(2));
    }

    @Test
    void Relative_Distribution_Multiple_Sum_Multiple_Data_Variation_OK() {
        when(rollDiceItemRepository.sumAllSimulationRolls()).thenReturn(300L);
        when(rollDiceItemRepository.findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(anyInt(), anyInt()))
                .thenReturn(Lists.list(
                        new RollDiceResultItemEntity(1L, null, 3, 1L),
                        new RollDiceResultItemEntity(1L, null, 3, 3L),
                        new RollDiceResultItemEntity(1L, null, 4, 3L),
                        new RollDiceResultItemEntity(1L, null, 5, 1L),
                        new RollDiceResultItemEntity(1L, null, 5, 3L),
                        new RollDiceResultItemEntity(1L, null, 5, 4L),
                        new RollDiceResultItemEntity(1L, null, 5, 3L)
                                      ));

        final List<RollDistribution> distributions = statisticsService.relativeDistribution(1, 1);

        assertThat(distributions).isNotNull();
        assertThat(distributions).isNotEmpty().hasSize(3);
        // 1
        assertThat(distributions).extracting("sum", Integer.class).element(0).isEqualTo(3);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(0).isEqualTo(BigDecimal.valueOf(1.33));

        // 2
        assertThat(distributions).extracting("sum", Integer.class).element(1).isEqualTo(4);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(1).isEqualTo(BigDecimal.valueOf(1).setScale(2));

        // 3
        assertThat(distributions).extracting("sum", Integer.class).element(2).isEqualTo(5);
        assertThat(distributions).extracting("percentage", BigDecimal.class).element(2).isEqualTo(BigDecimal.valueOf(3.67).setScale(2));
    }
}
