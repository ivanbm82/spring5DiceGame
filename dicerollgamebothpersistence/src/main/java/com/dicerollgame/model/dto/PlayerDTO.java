package com.dicerollgame.model.dto;


public class PlayerDTO {

    private String id;
    private String name;
    private double successPercentage;

    public PlayerDTO() {
    }

    public PlayerDTO(String id, String name, double successPercentage) {
        this.id = id;
        this.name = name;
        this.successPercentage = successPercentage;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSuccessPercentage() {
        return successPercentage;
    }

    public void setSuccessPercentage(double successPercentage) {
        this.successPercentage = successPercentage;
    }
}