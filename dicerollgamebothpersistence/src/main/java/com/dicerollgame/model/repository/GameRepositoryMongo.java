package com.dicerollgame.model.repository;

import com.dicerollgame.model.domain.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepositoryMongo  extends MongoRepository<Game, ObjectId> {
    List<Game> findAllByPlayerId(String playerId);

}