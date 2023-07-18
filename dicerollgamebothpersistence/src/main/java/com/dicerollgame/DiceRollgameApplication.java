package com.dicerollgame;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@OpenAPIDefinition
public class DiceRollgameApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiceRollgameApplication.class, args);
	}


	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Dice Game API")
						.description("API documentation for the Dice Game")
						.contact(new Contact()
								.name("Iván Bueno")
								.email("iv13an15@gmail.com")));
	}

}