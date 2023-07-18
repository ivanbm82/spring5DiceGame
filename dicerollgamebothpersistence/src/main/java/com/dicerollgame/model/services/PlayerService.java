package com.dicerollgame.model.services;


import com.dicerollgame.model.dto.*;

import java.util.List;

public interface PlayerService {


    PlayerDTO createPlayer(RegistrationDTO registrationDTO);

    PlayerDTO updatePlayerName(Long playerId, String newName);

    DiceRollDTO rollDice(Long playerId);

    void deletePlayerGames(Long playerId);

    List<PlayerDTO> getAllPlayersWithSuccessPercentage();

    List<GameDTO> getPlayerGames(Long playerId);

    double getAverageSuccessPercentage();

    RankingDTO getLoserPlayer();

    RankingDTO getWinnerPlayer();

}
