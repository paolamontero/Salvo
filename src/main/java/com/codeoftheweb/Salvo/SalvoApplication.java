package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.Repository.*;
import com.codeoftheweb.Salvo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class SalvoApplication<Fecha> {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Autowired
    GameRepository gameRepository;
	@Autowired
    GamePlayerRepository gamePlayerRepository;

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository, SalvoRepository salvoRepository) {
		return (args) -> {

            Player player1 = new Player("paola20@gmail.com");
            Player player2 = new Player("Nicolash@gmail.com");
            playerRepository.save(player1);
            playerRepository.save(player2);

            Game game1 = new Game();
            gameRepository.save(game1);
            Game game2 = new Game();
            gameRepository.save(game2);

            GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
            GamePlayer gamePlayer2 = new GamePlayer(game1, player2);

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);

            Ship ship1 = new Ship("Carrier");
            Ship ship2 = new Ship("Battleship");
            shipRepository.save(ship1);

            gamePlayer1.addShip(ship1);
            gamePlayer2.addShip(ship2);

            List<String> shipLocation1 = new ArrayList<>();
            shipLocation1.add("H3");
            shipLocation1.add("H4");
            shipLocation1.add("H5");
            ship1.setShipLocation(shipLocation1);
            shipRepository.save(ship1);

            List<String> shipLocation2 = new ArrayList<>();
            shipLocation2.add("H6");
            shipLocation2.add("H7");
            shipLocation2.add("H8");
            ship1.setShipLocation(shipLocation1);
            shipRepository.save(ship2);

//SALVO 1
            List<String> salvoLocation1 = new ArrayList<>();
            salvoLocation1.add("A1");
            salvoLocation1.add("A2");
            Salvo salvo1 = new Salvo(gamePlayer1, 1, salvoLocation1);
            salvoRepository.save(salvo1);
//SALVO 2
            List<String> salvoLocation2 = new ArrayList<>();
            salvoLocation2.add("C2");
            salvoLocation2.add("C3");
            Salvo salvo2 = new Salvo(gamePlayer2,2, salvoLocation2);
            salvoRepository.save(salvo2);

		};
    }
//
}
