package com.avaloq.dice.app.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StatisticsControllerTest {

    @Autowired
    private StatisticsController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
