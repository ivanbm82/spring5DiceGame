package com.dicerollgame2.dicerollgame.service;


import com.dicerollgame2.dicerollgame.model.domain.Game;
import com.dicerollgame2.dicerollgame.model.domain.Player;
import com.dicerollgame2.dicerollgame.model.dto.GameDTO;
import com.dicerollgame2.dicerollgame.model.dto.PlayerDTO;
import com.dicerollgame2.dicerollgame.model.repository.GameRepository;
import com.dicerollgame2.dicerollgame.model.repository.PlayerRepository;
import com.dicerollgame2.dicerollgame.model.service.PlayerServiceImpl;
import com.dicerollgame2.dicerollgame.security.exception.ExceptionSecurity;
import com.dicerollgame2.dicerollgame.security.user.Role;
import com.dicerollgame2.dicerollgame.security.user.User;
import com.dicerollgame2.dicerollgame.security.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAuthenticatedUser();
    }

    private void mockAuthenticatedUser() {
        User user = new User();
        user.setRole(Role.ADMIN);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin@example.com");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
    }

    @Test
    void createPlayer_ValidPlayerDTO_ReturnsCreatedPlayer() {
        // Arrange
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setName("John Doe");

        Player savedPlayer = new Player();
        savedPlayer.setId(1L);
        savedPlayer.setName("John Doe");

        when(playerRepository.save(ArgumentMatchers.any(Player.class))).thenReturn(savedPlayer);

        // Act
        Player result = playerService.createPlayer(playerDTO);

        // Assert
        assertThat(result).isEqualTo(savedPlayer);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");

        verify(playerRepository).save(ArgumentMatchers.any(Player.class));
    }

    @Test
    void createPlayer_PlayerNameNull_ThrowsIllegalArgumentException() {
        // Arrange
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setName(null);

        // Act & Assert
        assertThatThrownBy(() -> playerService.createPlayer(playerDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Player name can't be null or empty");

        verifyNoInteractions(playerRepository);
    }

    @Test
    void updatePlayerName_ValidPlayerIdAndNewName_ReturnsUpdatedPlayerDTO() {
        // Arrange
        Player existingPlayer = new Player();
        existingPlayer.setId(1L);
        existingPlayer.setName("John Doe");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.save(ArgumentMatchers.any(Player.class))).thenReturn(existingPlayer);

        // Act
        PlayerDTO result = playerService.updatePlayerName(1L, "Jane Doe");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Jane Doe");

        verify(playerRepository).findById(1L);
        verify(playerRepository).save(ArgumentMatchers.any(Player.class));
    }

    @Test
    void updatePlayerName_PlayerNotFound_ThrowsNotFoundException() {
        // Arrange
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> playerService.updatePlayerName(1L, "Jane Doe"))
                .isInstanceOf(ExceptionSecurity.NotFoundException.class)
                .hasMessage("Player not found with ID: 1");

        verify(playerRepository).findById(1L);
        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    void rollDice_ValidPlayerId_ReturnsGameDTO() {
        // Arrange
        Player existingPlayer = new Player();
        existingPlayer.setId(1L);
        existingPlayer.setName("John Doe");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existingPlayer));

        when(gameRepository.save(ArgumentMatchers.any(Game.class))).thenAnswer(invocation -> {
            Game game = invocation.getArgument(0);
            game.setId(1L);
            return game;
        });

        // Act
        GameDTO result = playerService.rollDice(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDice1()).isBetween(1, 6);
        assertThat(result.getDice2()).isBetween(1, 6);

        verify(playerRepository).findById(1L);
        verify(gameRepository).save(ArgumentMatchers.any(Game.class));
    }

    @Test
    void rollDice_PlayerNotFound_ThrowsNotFoundException() {
        // Arrange
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> playerService.rollDice(1L))
                .isInstanceOf(ExceptionSecurity.NotFoundException.class)
                .hasMessage("Player not found with ID: 1");

        verify(playerRepository).findById(1L);
        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    void deletePlayerGames_PlayerNotFound_ThrowsNotFoundException() {
        // Arrange
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> playerService.deletePlayerGames(1L))
                .isInstanceOf(ExceptionSecurity.NotFoundException.class)
                .hasMessage("Player not found with ID: 1");

        verify(playerRepository).findById(1L);
        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    public void getAverageSuccessPercentage_ReturnsAveragePercentage() {
        // Arrange
        Player player1 = new Player();
        player1.setGames(Arrays.asList(createGame(true), createGame(true), createGame(true)));

        Player player2 = new Player();
        player2.setGames(Arrays.asList(createGame(false), createGame(true)));

        when(playerRepository.findAll()).thenReturn(Arrays.asList(player1, player2));

        // Act
        double averagePercentage = playerService.getAverageSuccessPercentage();

        // Assert
        assertThat(averagePercentage).isEqualTo(75);
    }

    @Test
    public void getLoserPlayer_ReturnsLoserPlayer() {
        // Arrange
        Player loserPlayer = new Player();
        loserPlayer.setGames(Arrays.asList(createGame(false), createGame(false), createGame(false)));

        Player winnerPlayer = new Player();
        winnerPlayer.setGames(Arrays.asList(createGame(true), createGame(true)));

        when(playerRepository.findAll()).thenReturn(Arrays.asList(loserPlayer, winnerPlayer));

        // Act
        PlayerDTO loserPlayerDTO = playerService.getLoserPlayer();

        // Assert
        assertThat(loserPlayerDTO).isNotNull();
        assertThat(loserPlayerDTO.getId()).isEqualTo(loserPlayer.getId());
    }

    @Test
    public void getWinnerPlayer_ReturnsWinnerPlayer() {
        // Arrange
        Player loserPlayer = new Player();
        loserPlayer.setGames(Arrays.asList(createGame(false), createGame(false), createGame(false)));

        Player winnerPlayer = new Player();
        winnerPlayer.setGames(Arrays.asList(createGame(true), createGame(true), createGame(true)));

        when(playerRepository.findAll()).thenReturn(Arrays.asList(loserPlayer, winnerPlayer));

        // Act
        PlayerDTO winnerPlayerDTO = playerService.getWinnerPlayer();

        // Assert
        assertThat(winnerPlayerDTO).isNotNull();
        assertThat(winnerPlayerDTO.getId()).isEqualTo(winnerPlayer.getId());
    }

    private Game createGame(boolean won) {
        Game game = new Game();
        game.setWon(won);
        return game;
    }
}