package com.avaloq.dice.app.api.dto;

import java.util.List;

import com.avaloq.dice.app.model.RollDistribution;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsResponse {

    List<TotalNumberSimulationResponse> total;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<RollDistributionResponse> distribution;
}
