package com.dicerollgame2.dicerollgame.controller;


import com.dicerollgame2.dicerollgame.model.domain.Player;
import com.dicerollgame2.dicerollgame.model.dto.GameDTO;
import com.dicerollgame2.dicerollgame.model.dto.PlayerDTO;
import com.dicerollgame2.dicerollgame.model.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlayer_WithValidPlayerDTO_ReturnsResponseWithPlayerAndHttpStatusCreated() {
        // Arrange
        PlayerDTO playerDTO = new PlayerDTO();
        Player player = new Player();
        when(playerService.createPlayer(any(PlayerDTO.class))).thenReturn(player);

        // Act
        ResponseEntity<Player> response = playerController.createPlayer(playerDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(player);
    }

    @Test
    void updatePlayerName_WithValidPlayerIdAndNewName_ReturnsResponseWithUpdatedPlayerAndHttpStatusOk() {
        // Arrange
        Long playerId = 1L;
        String newName = "John Doe";
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setName(newName);
        PlayerDTO updatedPlayer = new PlayerDTO();
        updatedPlayer.setId(playerId);
        updatedPlayer.setName(newName);

        when(playerService.updatePlayerName(anyLong(), anyString())).thenReturn(updatedPlayer);

        // Act
        ResponseEntity<PlayerDTO> response = playerController.updatePlayerName(playerId, playerDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedPlayer);
    }


    @Test
    void rollDice_WithValidPlayerId_ReturnsResponseWithGameDTOAndHttpStatusOk() {
        // Arrange
        Long playerId = 1L;
        GameDTO game = new GameDTO();
        when(playerService.rollDice(anyLong())).thenReturn(game);

        // Act
        ResponseEntity<GameDTO> response = playerController.rollDice(playerId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(game);
    }

    @Test
    void deletePlayerGames_WithValidPlayerId_ReturnsResponseWithHttpStatusNoContent() {
        // Arrange
        Long playerId = 1L;

        // Act
        ResponseEntity<Void> response = playerController.deletePlayerGames(playerId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(playerService).deletePlayerGames(playerId);
    }

    @Test
    void getPlayerGames_WithValidPlayerId_ReturnsResponseWithListGameDTOAndHttpStatusOk() {
        // Arrange
        Long playerId = 1L;
        List<GameDTO> games = new ArrayList<>();
        when(playerService.getPlayerGames(anyLong())).thenReturn(games);

        // Act
        ResponseEntity<List<GameDTO>> response = playerController.getPlayerGames(playerId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(games);
    }

    @Test
    void getAllPlayers_WithAdminRole_ReturnsResponseWithListPlayerDTOAndHttpStatusOk() {
        // Arrange
        List<PlayerDTO> players = new ArrayList<>();
        when(playerService.getAllPlayers()).thenReturn(players);

        // Act
        ResponseEntity<List<PlayerDTO>> response = playerController.getAllPlayers();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(players);
    }

    @Test
    void getAverageSuccessPercentage_WithAdminRole_ReturnsResponseWithAveragePercentageAndHttpStatusOk() {
        // Arrange
        double averagePercentage = 0.75;
        when(playerService.getAverageSuccessPercentage()).thenReturn(averagePercentage);

        // Act
        ResponseEntity<Double> response = playerController.getAverageSuccessPercentage();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(averagePercentage);
    }

    @Test
    void getLoserPlayer_WithAdminRole_ReturnsResponseWithLoserPlayerAndHttpStatusOk() {
        // Arrange
        PlayerDTO loserPlayer = new PlayerDTO();
        when(playerService.getLoserPlayer()).thenReturn(loserPlayer);

        // Act
        ResponseEntity<PlayerDTO> response = playerController.getLoserPlayer();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(loserPlayer);
    }

    @Test
    void getWinnerPlayer_WithAdminRole_ReturnsResponseWithWinnerPlayerAndHttpStatusOk() {
        // Arrange
        PlayerDTO winnerPlayer = new PlayerDTO();
        when(playerService.getWinnerPlayer()).thenReturn(winnerPlayer);

        // Act
        ResponseEntity<PlayerDTO> response = playerController.getWinnerPlayer();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(winnerPlayer);
    }
}
