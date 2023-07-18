package com.dicerollgame.model.dto;


public class PlayerStatisticsDTO {

    private int totalGames;
    private int gamesWon;
    private double successPercentage;

    public PlayerStatisticsDTO() {
    }

    public PlayerStatisticsDTO(int totalGames, int gamesWon, double successPercentage) {
        this.totalGames = totalGames;
        this.gamesWon = gamesWon;
        this.successPercentage = successPercentage;
    }


    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public double getSuccessPercentage() {
        return successPercentage;
    }

    public void setSuccessPercentage(double successPercentage) {
        this.successPercentage = successPercentage;
    }
}