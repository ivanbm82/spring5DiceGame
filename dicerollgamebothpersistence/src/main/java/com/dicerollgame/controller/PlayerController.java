package com.dicerollgame.controller;




import com.dicerollgame.model.dto.*;
import com.dicerollgame.model.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Tag(name = "Crear jugador")
    @ApiResponse(responseCode = "201", description = "Jugador creado exitosamente")
    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody RegistrationDTO registrationDTO) {
        PlayerDTO playerDTO = playerService.createPlayer(registrationDTO);
        return new ResponseEntity<>(playerDTO, HttpStatus.CREATED);
    }

    @Tag(name = "Actualizar nombre del jugador")
    @ApiResponse(responseCode = "200", description = "Nombre del jugador actualizado exitosamente", content = @Content(schema = @Schema(implementation = PlayerDTO.class)))
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayerName(@PathVariable("id") long playerId, @RequestParam("name") String name) {
        PlayerDTO playerDTO = playerService.updatePlayerName(playerId, name);
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }

    @Tag(name = "Lanzar dado")
    @ApiResponse(responseCode = "200", description = "Dado lanzado exitosamente", content = @Content(schema = @Schema(implementation = DiceRollDTO.class)))
    @PostMapping("/{id}/games")
    public ResponseEntity<DiceRollDTO> rollDice(@PathVariable("id") long playerId) {
        DiceRollDTO diceRollDTO = playerService.rollDice(playerId);
        return new ResponseEntity<>(diceRollDTO, HttpStatus.OK);
    }

    @Tag(name = "Eliminar juegos del jugador")
    @ApiResponse(responseCode = "204", description = "Juegos del jugador eliminados exitosamente")
    @DeleteMapping("/{id}/games")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayerGames(@PathVariable("id") long playerId) {
        playerService.deletePlayerGames(playerId);
    }

    @Tag(name = "Obtener todos los jugadores")
    @ApiResponse(responseCode = "200", description = "Jugadores obtenidos exitosamente", content = @Content(schema = @Schema(implementation = PlayerDTO.class)))
    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        List<PlayerDTO> players = playerService.getAllPlayersWithSuccessPercentage();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @Tag(name = "Obtener juegos del jugador")
    @ApiResponse(responseCode = "200", description = "Juegos del jugador obtenidos exitosamente", content = @Content(schema = @Schema(implementation = GameDTO.class)))
    @GetMapping("/{id}/games")
    public ResponseEntity<List<GameDTO>> getPlayerGames(@PathVariable("id") long playerId) {
        List<GameDTO> games = playerService.getPlayerGames(playerId);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
    @Tag(name = "Obtener promedio del porcentaje de éxito")
    @ApiResponse(responseCode = "200", description = "Promedio del porcentaje de éxito obtenido exitosamente")
    @GetMapping("/average-success-percentage")
    public ResponseEntity<Double> getAverageSuccessPercentage() {
        double averageSuccessPercentage = playerService.getAverageSuccessPercentage();
        return new ResponseEntity<>(averageSuccessPercentage, HttpStatus.OK);
    }



    @Tag(name = "Obtener jugador perdedor")
    @ApiResponse(responseCode = "200", description = "Jugador perdedor obtenido exitosamente", content = @Content(schema = @Schema(implementation = RankingDTO.class)))
    @GetMapping("/ranking/loser")
    public ResponseEntity<RankingDTO> getLoserPlayer() {
        RankingDTO loserPlayerDTO = playerService.getLoserPlayer();
        return new ResponseEntity<>(loserPlayerDTO, HttpStatus.OK);
    }

    @Tag(name = "Obtener jugador ganador")
    @ApiResponse(responseCode =     "200", description = "Jugador ganador obtenido exitosamente", content = @Content(schema = @Schema(implementation = RankingDTO.class)))
    @GetMapping("/ranking/winner")
    public ResponseEntity<RankingDTO> getWinnerPlayer() {
        RankingDTO winnerPlayerDTO = playerService.getWinnerPlayer();
        return new ResponseEntity<>(winnerPlayerDTO, HttpStatus.OK);
    }
}