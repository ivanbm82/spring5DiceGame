package com.dicerollgame2.dicerollgame.model.repository;

import com.dicerollgame2.dicerollgame.model.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByPlayerId(Long playerId);

}
