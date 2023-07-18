package com.dicerollgame2.dicerollgame.model.repository;

import com.dicerollgame2.dicerollgame.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
