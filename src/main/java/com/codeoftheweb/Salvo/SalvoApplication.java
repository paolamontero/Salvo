package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.Repository.GameRepository;
import com.codeoftheweb.Salvo.Repository.PlayerRepository;
import com.codeoftheweb.Salvo.models.Game;
import com.codeoftheweb.Salvo.models.GamePlayer;
import com.codeoftheweb.Salvo.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

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
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
		return (args) -> {

		    Player player1  = new Player ("Paola");
		    Player player2 = new Player ("Nico");
		    playerRepository.save(player1);
		    playerRepository.save(player2);

		    Game game1 = new Game ();
		    gameRepository.save(game1);

		    GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
		    GamePlayer gamePlayer2 = new GamePlayer(game1, player2);

		    gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);

            //GamePlayer gamePlayer2 = new GamePlayer(player1,game1);
			//repository.save(new Player("Pao"));
			//repository.save(new Player("Nico"));
		};
    }
//
}
