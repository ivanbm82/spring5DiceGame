package com.dicerollgame2.dicerollgame.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dice1;

    private int dice2;

    private boolean won;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
}

