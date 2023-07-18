package com.dicerollgame.model.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Data
@Document(collection = "games")
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    private ObjectId id;

    private String playerId;

    private int dice1;

    private int dice2;

    private boolean win;

}