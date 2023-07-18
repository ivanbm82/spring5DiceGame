package com.dicerollgame.model.dto;


public class GameDTO {

    private int dice1;
    private int dice2;
    private boolean won;

    public GameDTO() {
    }

    public GameDTO(int dice1, int dice2, boolean won) {
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.won = won;
    }


    public int getDice1() {
        return dice1;
    }

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }
}