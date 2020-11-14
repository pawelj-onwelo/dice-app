package com.avaloq.dice.app.api;

import java.util.List;
import java.util.stream.Collectors;

import com.avaloq.dice.app.api.dto.RollDistributionResponse;
import com.avaloq.dice.app.api.dto.StatisticsResponse;
import com.avaloq.dice.app.api.dto.TotalNumberSimulationResponse;
import com.avaloq.dice.app.model.RollDistribution;
import com.avaloq.dice.app.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<StatisticsResponse> statistics(
            @RequestParam(value = "dice", required = false) Integer diceNo,
            @RequestParam(value = "sides", required = false) Integer sidesNo) {
        final List<TotalNumberSimulationResponse> totalStatistics =
                statisticsService.statsticsTotalNumber()
                                    .stream().map(e -> TotalNumberSimulationResponse.builder()
                                            .diceNo(e.getDiceNo())
                                            .sidesNo(e.getSidesNo())
                                            .totalRolls(e.getTotalRolls())
                                            .totalSimulations(e.getTotalSimulations())
                                            .build())
                                    .collect(Collectors.toList());

        List<RollDistributionResponse> distribution = null;
        
        if (null != diceNo && null != sidesNo) {
            distribution = statisticsService.relativeDistribution(diceNo, sidesNo).stream()
                                            .map(e -> RollDistributionResponse.builder()
                                                                    .sum(e.getSum())
                                                                    .percentage(e.getPercentage())
                                                                    .build())
                                            .collect(Collectors.toList());
        }

        return ResponseEntity.ok(StatisticsResponse.builder().total(totalStatistics).distribution(distribution).build());
    }
}
