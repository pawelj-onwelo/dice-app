package com.avaloq.dice.app.repository.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLL_DICE_RESULT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RollDiceResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer diceNo;

    @Column(nullable = false)
    private Integer sidesNo;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "rollDiceResult",
            orphanRemoval = true)
    private Set<RollDiceResultItemEntity> resultItems;
}
