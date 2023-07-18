package com.dicerollgame.model.dto;


public class RegistrationDTO{

    private String name;



    public RegistrationDTO() {
    }

    public RegistrationDTO(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}