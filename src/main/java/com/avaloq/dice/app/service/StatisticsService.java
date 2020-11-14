package com.avaloq.dice.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

import com.avaloq.dice.app.exception.NoSimulationsException;
import com.avaloq.dice.app.model.RollDistribution;
import com.avaloq.dice.app.model.TotalNumberSimulation;
import com.avaloq.dice.app.repository.RollDiceItemRepository;
import com.avaloq.dice.app.repository.RollDiceRepository;
import com.avaloq.dice.app.repository.model.RollDiceResultItemEntity;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    RollDiceRepository rollDiceRepository;
    RollDiceItemRepository rollDiceItemRepository;

    public StatisticsService(RollDiceRepository rollDiceRepository,
                             RollDiceItemRepository rollDiceItemRepository) {
        this.rollDiceRepository = rollDiceRepository;
        this.rollDiceItemRepository = rollDiceItemRepository;
    }

    /**
     * Method prepares total statistics data
     * First it prepares for further aggregations
     * Total rolls number are aggregated independently from total simulations number
     * and than combined into a single result by matching aggregated data
     * @return Aggregated data
     */
    public List<TotalNumberSimulation> statsticsTotalNumber() {

        final List<TotalNumberSimulation> simulations =
                rollDiceRepository.findAll().stream()
                                    .map(n -> TotalNumberSimulation.builder()
                                            .diceNo(n.getDiceNo())
                                            .sidesNo(n.getSidesNo())
                                            .totalRolls(n.getResultItems().stream().map(e -> e.getAmount()).reduce(0L, Long::sum))
                                            .totalSimulations(1L)
                                            .build())
                                    .collect(Collectors.toList());

        final Map<Integer, Map<Integer, Long>> rollsAggregation = simulations.stream().collect(
                Collectors.groupingBy(TotalNumberSimulation::getDiceNo,
                                      Collectors.groupingBy(TotalNumberSimulation::getSidesNo,
                                                            Collectors.summingLong(TotalNumberSimulation::getTotalRolls))));

        final Map<Integer, Map<Integer, Long>> countAggregation = simulations.stream().collect(
                Collectors.groupingBy(TotalNumberSimulation::getDiceNo,
                                      Collectors.groupingBy(TotalNumberSimulation::getSidesNo,
                                                            Collectors.counting())));

        List<TotalNumberSimulation> aggregatedResults = new ArrayList<>();
        rollsAggregation.forEach((diceNo, sidesMap) ->
                                         sidesMap.forEach((sideNo, totalRolls) ->
                                                      aggregatedResults.add(TotalNumberSimulation.builder()
                                                                                    .sidesNo(sideNo)
                                                                                    .diceNo(diceNo)
                                                                                    .totalSimulations(countAggregation.get(diceNo).get(sideNo))
                                                                                    .totalRolls(totalRolls)
                                                                                    .build())));

        return aggregatedResults;
    }


    /**
     * Method provides relative distribution of a given pair dice number and sides number
     * Relative distribution is counted based on total rolls in all simulations, not only for given pair
     * @param diceNo
     * @param sidesNo
     * @return
     */
    public List<RollDistribution> relativeDistribution(int diceNo, int sidesNo) {
        final Long simulationRolls = rollDiceItemRepository.sumAllSimulationRolls();
        if (null == simulationRolls || 0L == simulationRolls) {
            throw new NoSimulationsException();
        }

        final BigDecimal allSimulationRolls = BigDecimal.valueOf(simulationRolls);
        return rollDiceItemRepository
                        .findAllByRollDiceResult_DiceNoAndRollDiceResult_SidesNo(diceNo, sidesNo).stream()
                        .collect(Collectors.groupingBy(RollDiceResultItemEntity::getResult,
                                                       Collectors.summingLong(RollDiceResultItemEntity::getAmount)))
                        .entrySet().stream()
                        .map(e -> RollDistribution.builder()
                                .sum(e.getKey())
                                .percentage(BigDecimal.valueOf(e.getValue()).multiply(BigDecimal.valueOf(100)).divide(allSimulationRolls, 2, RoundingMode.HALF_EVEN))
                                .build())
                        .collect(Collectors.toList());
    }
}
