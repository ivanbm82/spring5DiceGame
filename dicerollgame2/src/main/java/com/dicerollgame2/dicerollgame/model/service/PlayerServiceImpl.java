package com.dicerollgame2.dicerollgame.model.service;


import com.dicerollgame2.dicerollgame.model.domain.Game;
import com.dicerollgame2.dicerollgame.model.domain.Player;
import com.dicerollgame2.dicerollgame.model.dto.GameDTO;
import com.dicerollgame2.dicerollgame.model.dto.PlayerDTO;
import com.dicerollgame2.dicerollgame.model.repository.GameRepository;
import com.dicerollgame2.dicerollgame.model.repository.PlayerRepository;
import com.dicerollgame2.dicerollgame.security.exception.ExceptionSecurity;
import com.dicerollgame2.dicerollgame.security.user.Role;
import com.dicerollgame2.dicerollgame.security.user.User;
import com.dicerollgame2.dicerollgame.security.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, GameRepository gameRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Player createPlayer(PlayerDTO playerDTO) {
        if (playerDTO.getName() == null || playerDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Player name can't be null or empty");
        }

        // Verificar el rol del usuario autenticado
        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser) && !isPlayerNameMatchUser(playerDTO.getName(), authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to create a player with a different name.");
        }

        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setUser(authenticatedUser);
        return playerRepository.save(player);
    }

    @Override
    public PlayerDTO updatePlayerName(Long playerId, String newName) {
        Player player = getPlayerById(playerId);

        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to get all players.");
        }

        player.setName(newName);

        Player updatedPlayer = playerRepository.save(player);

        return mapPlayerToDTO(updatedPlayer);
    }

    @Override
    public GameDTO rollDice(Long playerId) {
        Player player = getPlayerById(playerId);

        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser) && !isPlayerNameMatchUser(player.getName(), authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to roll the dice for this player.");
        }

        int dice1 = rollDice();
        int dice2 = rollDice();
        boolean won = (dice1 + dice2) == 7;

        Game game = new Game();
        game.setDice1(dice1);
        game.setDice2(dice2);
        game.setWon(won);
        game.setPlayer(player);

        Game savedGame = gameRepository.save(game);

        return mapGameToDTO(savedGame);
    }

    @Override
    @Transactional
    public void deletePlayerGames(Long playerId) {
        Player player = getPlayerById(playerId);

        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser) && !isPlayerNameMatchUser(player.getName(), authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to delete games for this player.");
        }

        player.getGames().clear();
    }

    @Override
    public List<GameDTO> getPlayerGames(Long playerId) {
        Player player = getPlayerById(playerId);

        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser) && !isPlayerNameMatchUser(player.getName(), authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to get games for this player.");
        }

        return player.getGames().stream()
                .map(game -> mapGameToDTO(game))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerDTO> getAllPlayers() {

        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to get all players.");
        }

        return playerRepository.findAll()
                .stream()
                .map(this::mapPlayerToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageSuccessPercentage() {

        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to get the average success percentage.");
        }

        return playerRepository.findAll()
                .stream()
                .mapToDouble(this::calculatePlayerSuccessPercentage)
                .average()
                .orElse(0.0);
    }

    @Override
    public PlayerDTO getLoserPlayer() {

        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to get the loser player.");
        }

        return playerRepository.findAll()
                .stream()
                .min(Comparator.comparingDouble(this::calculatePlayerSuccessPercentage))
                .map(this::mapPlayerToDTO)
                .orElse(null);
    }

    @Override
    public PlayerDTO getWinnerPlayer() {

        User authenticatedUser = getAuthenticatedUser();
        if (!isUserAdmin(authenticatedUser)) {
            throw new ExceptionSecurity.ForbiddenException("You don't have permission to get the winner player.");
        }

        return playerRepository.findAll()
                .stream()
                .max(Comparator.comparingDouble(this::calculatePlayerSuccessPercentage))
                .map(this::mapPlayerToDTO)
                .orElse(null);
    }

    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new ExceptionSecurity.NotFoundException("Player not found with ID: " + playerId));
    }

    private int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }

    private double calculatePlayerSuccessPercentage(Player player) {
        List<Game> games = player.getGames();

        if (games.isEmpty()) {
            return 0.0;
        }

        long wonGamesCount = games.stream()
                .filter(Game::isWon)
                .count();

        return (double) wonGamesCount / games.size() * 100;
    }

    private PlayerDTO mapPlayerToDTO(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getName());
        playerDTO.setRegistrationDate(player.getRegistrationDate());
        playerDTO.setSuccessPercentage(calculatePlayerSuccessPercentage(player));
        return playerDTO;
    }

    private GameDTO mapGameToDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        gameDTO.setDice1(game.getDice1());
        gameDTO.setDice2(game.getDice2());
        gameDTO.setWon(game.isWon());
        return gameDTO;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();
        return userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new ExceptionSecurity.NotFoundException("User not found."));
    }

    private boolean isUserAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    private boolean isPlayerNameMatchUser(String playerName, User user) {
        return playerName.equals(user.getFirstname());
    }
}
