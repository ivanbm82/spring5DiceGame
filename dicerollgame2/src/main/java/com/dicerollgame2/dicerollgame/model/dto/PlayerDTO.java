package com.dicerollgame2.dicerollgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    private Long id;
    private String name;
    private Date registrationDate;
    private double successPercentage;
}
