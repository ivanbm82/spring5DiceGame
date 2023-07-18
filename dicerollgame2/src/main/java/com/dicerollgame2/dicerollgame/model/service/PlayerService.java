package com.dicerollgame2.dicerollgame.model.service;



import com.dicerollgame2.dicerollgame.model.domain.Player;
import com.dicerollgame2.dicerollgame.model.dto.GameDTO;
import com.dicerollgame2.dicerollgame.model.dto.PlayerDTO;

import java.util.List;

public interface PlayerService {
    Player createPlayer(PlayerDTO playerDTO);

    PlayerDTO updatePlayerName(Long playerId, String newName);

    GameDTO rollDice(Long playerId);

    void deletePlayerGames(Long playerId);

    List<PlayerDTO> getAllPlayers();
    List<GameDTO> getPlayerGames(Long playerId);

    double getAverageSuccessPercentage();

    PlayerDTO getLoserPlayer();

    PlayerDTO getWinnerPlayer();
}
