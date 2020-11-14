package com.avaloq.dice.app.repository;

import java.util.List;

import com.avaloq.dice.app.repository.model.RollDiceResultEntity;
import com.avaloq.dice.app.repository.model.RollDiceResultItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RollDiceRepository extends JpaRepository<RollDiceResultEntity, Long> {
}
