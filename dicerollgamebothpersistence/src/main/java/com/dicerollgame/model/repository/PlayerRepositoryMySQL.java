package com.dicerollgame.model.repository;


import com.dicerollgame.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepositoryMySQL extends JpaRepository<Player, Long> {

}