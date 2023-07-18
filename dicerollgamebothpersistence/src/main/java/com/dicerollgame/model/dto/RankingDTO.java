package com.dicerollgame.model.dto;


public class RankingDTO {

    private String playerName;
    private double successPercentage;

    public RankingDTO() {
    }

    public RankingDTO(String playerName, double successPercentage) {
        this.playerName = playerName;
        this.successPercentage = successPercentage;
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getSuccessPercentage() {
        return successPercentage;
    }

    public void setSuccessPercentage(double successPercentage) {
        this.successPercentage = successPercentage;
    }
}