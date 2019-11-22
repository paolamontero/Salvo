package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.Repository.GameRepository;
import com.codeoftheweb.Salvo.Repository.PlayerRepository;
import com.codeoftheweb.Salvo.Repository.ShipRepository;
import com.codeoftheweb.Salvo.models.Game;
import com.codeoftheweb.Salvo.models.GamePlayer;
import com.codeoftheweb.Salvo.models.Player;
import com.codeoftheweb.Salvo.models.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
		return (args) -> {

            Player player1 = new Player("Paola");
            Player player2 = new Player("Nico");
            playerRepository.save(player1);
            playerRepository.save(player2);

            Game game1 = new Game();
            gameRepository.save(game1);

            GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
            GamePlayer gamePlayer2 = new GamePlayer(game1, player2);

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);

            Ship ship = new Ship("Carrier");
            Ship ship2 = new Ship("Carrier");
            shipRepository.save(ship);

            gamePlayer1.addShip(ship);

            List<String> shipLocations = new ArrayList<>();
            shipLocations.add("H3");
            shipLocations.add("H4");
            shipLocations.add("H5");
            ship.setShipLocation(shipLocations);
            shipRepository.save(ship);

		};
    }
//
}
