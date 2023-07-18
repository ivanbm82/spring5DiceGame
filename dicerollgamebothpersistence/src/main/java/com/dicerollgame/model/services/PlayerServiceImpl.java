package com.dicerollgame.model.services;



import com.dicerollgame.model.domain.Game;
import com.dicerollgame.model.domain.Player;
import com.dicerollgame.model.dto.*;
import com.dicerollgame.model.repository.GameRepositoryMongo;
import com.dicerollgame.model.repository.PlayerRepositoryMySQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepositoryMySQL playerRepository;
    private final GameRepositoryMongo gameRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepositoryMySQL playerRepository, GameRepositoryMongo gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public PlayerDTO createPlayer(RegistrationDTO registrationDTO) {
        Player player = new Player();
        player.setName(registrationDTO.getName());
        Player savedPlayer = playerRepository.save(player);
        return convertToPlayerDTO(savedPlayer);
    }

    @Override
    public PlayerDTO updatePlayerName(Long playerId, String newName) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            player.setName(newName);
            Player updatedPlayer = playerRepository.save(player);
            return convertToPlayerDTO(updatedPlayer);
        }
        return null;
    }

    @Override
    public DiceRollDTO rollDice(Long playerId) {
        int dice1 = (int) (Math.random() * 6) + 1;
        int dice2 = (int) (Math.random() * 6) + 1;
        boolean won = (dice1 + dice2) == 7;

        Game game = new Game();
        game.setPlayerId(playerId.toString());
        game.setDice1(dice1);
        game.setDice2(dice2);
        game.setWin(won);
        gameRepository.save(game);

        return new DiceRollDTO(dice1, dice2, won);
    }

    @Override
    public void deletePlayerGames(Long playerId) {
        List<Game> games = gameRepository.findAllByPlayerId(playerId.toString());
        gameRepository.deleteAll(games);
    }

    @Override
    public List<PlayerDTO> getAllPlayersWithSuccessPercentage() {
        List<PlayerDTO> playerDTOs = new ArrayList<>();
        List<Player> players = playerRepository.findAll();

        for (Player player : players) {
            PlayerStatisticsDTO statisticsDTO = calculatePlayerStatistics(player);
            PlayerDTO playerDTO = convertToPlayerDTO(player);
            playerDTO.setSuccessPercentage(statisticsDTO.getSuccessPercentage());
            playerDTOs.add(playerDTO);
        }

        return playerDTOs;
    }

    @Override
    public List<GameDTO> getPlayerGames(Long playerId) {
        List<Game> games = gameRepository.findAllByPlayerId(playerId.toString());
        List<GameDTO> gameDTOs = new ArrayList<>();

        for (Game game : games) {
            gameDTOs.add(convertToGameDTO(game));
        }

        return gameDTOs;
    }

    @Override
    public double getAverageSuccessPercentage() {
        List<Player> players = playerRepository.findAll();
        double totalSuccessPercentage = 0.0;

        for (Player player : players) {
            PlayerStatisticsDTO statisticsDTO = calculatePlayerStatistics(player);
            totalSuccessPercentage += statisticsDTO.getSuccessPercentage();
        }

        return totalSuccessPercentage / players.size();
    }

    @Override
    public RankingDTO getLoserPlayer() {
        List<Player> players = playerRepository.findAll();
        double worstSuccessPercentage = Double.MAX_VALUE;
        Player worstPlayer = null;

        for (Player player : players) {
            PlayerStatisticsDTO statisticsDTO = calculatePlayerStatistics(player);
            if (statisticsDTO.getSuccessPercentage() < worstSuccessPercentage) {
                worstSuccessPercentage = statisticsDTO.getSuccessPercentage();
                worstPlayer = player;
            }
        }

        if (worstPlayer != null) {
            return new RankingDTO(worstPlayer.getName(), worstSuccessPercentage);
        }

        return null;
    }

    @Override
    public RankingDTO getWinnerPlayer() {
        List<Player> players = playerRepository.findAll();
        double bestSuccessPercentage = 0.0;
        Player bestPlayer = null;

        for (Player player : players) {
            PlayerStatisticsDTO statisticsDTO = calculatePlayerStatistics(player);
            if (statisticsDTO.getSuccessPercentage() > bestSuccessPercentage) {
                bestSuccessPercentage = statisticsDTO.getSuccessPercentage();
                bestPlayer = player;
            }
        }

        if (bestPlayer != null) {
            return new RankingDTO(bestPlayer.getName(), bestSuccessPercentage);
        }

        return null;
    }

    private PlayerDTO convertToPlayerDTO(Player player) {
        return new PlayerDTO(
                player.getId().toString(),
                player.getName(),
                0.0
        );
    }

    private GameDTO convertToGameDTO(Game game) {
        return new GameDTO(
                game.getDice1(),
                game.getDice2(),
                game.isWin()
        );
    }

    private PlayerStatisticsDTO calculatePlayerStatistics(Player player) {
        List<Game> games = gameRepository.findAllByPlayerId(player.getId().toString());
        int totalGames = games.size();
        int gamesWon = 0;

        for (Game game : games) {
            if (game.isWin()) {
                gamesWon++;
            }
        }

        double successPercentage = (totalGames > 0) ? ((double) gamesWon / totalGames) * 100 : 0.0;

        return new PlayerStatisticsDTO(totalGames, gamesWon, successPercentage);
    }
}