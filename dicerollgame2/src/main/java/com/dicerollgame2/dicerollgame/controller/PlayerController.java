package com.dicerollgame2.dicerollgame.controller;

import com.dicerollgame2.dicerollgame.model.domain.Player;
import com.dicerollgame2.dicerollgame.model.dto.GameDTO;
import com.dicerollgame2.dicerollgame.model.dto.PlayerDTO;
import com.dicerollgame2.dicerollgame.model.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping()
    public ResponseEntity<Player> createPlayer(@RequestBody PlayerDTO playerDTO) {
        // Creates a new player
        Player player = playerService.createPlayer(playerDTO);
        return new ResponseEntity<>(player, HttpStatus.CREATED);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerDTO> updatePlayerName(@PathVariable Long playerId, @RequestBody PlayerDTO playerDTO) {
        if (playerDTO.getName() == null || playerDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Player name can't be null or empty");
        }

        // Updates the player's name
        PlayerDTO updatedPlayer = playerService.updatePlayerName(playerId, playerDTO.getName());
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @PostMapping("/{playerId}/games")
    public ResponseEntity<GameDTO> rollDice(@PathVariable Long playerId) {
        // Rolls the dice for the player and returns the game details
        GameDTO game = playerService.rollDice(playerId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @DeleteMapping("/{playerId}/games")
    public ResponseEntity<Void> deletePlayerGames(@PathVariable Long playerId) {
        // Deletes all games of the player
        playerService.deletePlayerGames(playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{playerId}/games")
    public ResponseEntity<List<GameDTO>> getPlayerGames(@PathVariable Long playerId) {
        // Gets all games of the player
        List<GameDTO> games = playerService.getPlayerGames(playerId);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        // Gets all players
        List<PlayerDTO> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/ranking")
    public ResponseEntity<Double> getAverageSuccessPercentage() {
        // Gets the average success percentage of all players
        double averagePercentage = playerService.getAverageSuccessPercentage();
        return new ResponseEntity<>(averagePercentage, HttpStatus.OK);
    }

    @GetMapping("/ranking/loser")
    public ResponseEntity<PlayerDTO> getLoserPlayer() {
        // Gets the player with the lowest success percentage
        PlayerDTO loserPlayer = playerService.getLoserPlayer();
        return new ResponseEntity<>(loserPlayer, HttpStatus.OK);
    }

    @GetMapping("/ranking/winner")
    public ResponseEntity<PlayerDTO> getWinnerPlayer() {
        // Gets the player with the highest success percentage
        PlayerDTO winnerPlayer = playerService.getWinnerPlayer();
        return new ResponseEntity<>(winnerPlayer, HttpStatus.OK);
    }
}
