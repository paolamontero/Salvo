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
                                      ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
            return (args) -> {

                    //creacion de jugadores
                    Player player1 = new Player("paola@gmail.com");
                    Player player2 = new Player("mari@gmail.com");
                    Player player3 = new Player("nico@gmail.com");
                    Player player4 = new Player("lau@gmail.com");

                    //creacion de juegos
                    Game game1 = new Game();
                    Game game2 = new Game();
                    Game game3 = new Game();
                    Game game4 = new Game();

                    // creacion gamePlayers
                    GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
                    GamePlayer gamePlayer2 = new GamePlayer(game1, player2);

                    GamePlayer gamePlayer3 = new GamePlayer(game2, player3);
                    GamePlayer gamePlayer4 = new GamePlayer(game2, player4);


                    //horarios
            game1.setCreationDate(Date.from(game1.getCreationDate().toInstant().plusSeconds(3600)));
            game2.setCreationDate(Date.from(game2.getCreationDate().toInstant().plusSeconds(3600)));
            game3.setCreationDate(Date.from(game3.getCreationDate().toInstant().plusSeconds(3600)));
            game4.setCreationDate(Date.from(game4.getCreationDate().toInstant().plusSeconds(3600)));

            List<String> shipLocation1 = new ArrayList<>();
                shipLocation1.add("H3");
                shipLocation1.add("H4");
                shipLocation1.add("H5");

                List<String> shipLocation2 = new ArrayList<>();
                shipLocation2.add("H6");
                shipLocation2.add("H7");
                shipLocation2.add("H8");


                    List<String> shipLocation3 = new ArrayList<>();
                    shipLocation3.add("F3");
                    shipLocation3.add("C4");
                    shipLocation3.add("C6");

                    List<String> shipLocation4 = new ArrayList<>();
                    shipLocation4.add("C9");
                    shipLocation4.add("F3");
                    shipLocation4.add("F8");

                    //ships game 1
                    Ship ship1 = new Ship("Carrier", gamePlayer1, shipLocation1);
                    Ship ship2 = new Ship("Submarine", gamePlayer2, shipLocation2);
                    Ship ship3 = new Ship("Destroyer", gamePlayer3,shipLocation3);
                    Ship ship4 = new Ship("Battleship", gamePlayer4, shipLocation4);

                    // creo LAS UBICACIONES de los disparos o salvos

                    //game1
                    List<String> salvoLocation1 = new ArrayList<>();
                    salvoLocation1.add("A1");
                    salvoLocation1.add("A2");

                    List<String>salvoLocation2 = new ArrayList<>();
                    salvoLocation2.add("C2");
                    salvoLocation2.add("C3");
                    salvoLocation2.add("C4");

                    //game2
                    List<String>salvoLocation3 =  new ArrayList<>();
                    salvoLocation3.add("H7");
                    salvoLocation3.add("H8");

                    List<String> salvoLocation4 =  new ArrayList<>();
                    salvoLocation4.add("C2");
                    salvoLocation4.add("C3");
                    salvoLocation4.add("C4");

                    //game3
                    Set<String> salvoLocation5 = new LinkedHashSet<>();
                    salvoLocation5.add("J6"); salvoLocation5.add("J7");
                    Set<String> salvoLocation6 = new LinkedHashSet<>();
                    salvoLocation1.add("A1");salvoLocation1.add("A2");

                    //game4
                    Set<String> salvoLocation7 = new LinkedHashSet<>();
                    salvoLocation2.add("C2"); salvoLocation2.add("C3"); salvoLocation2.add("C4");
                    Set<String> salvoLocation8 = new LinkedHashSet<>();
                    salvoLocation3.add("H7"); salvoLocation3.add("H8");


                    //disparos-salvos

                    //game1
                    Salvo GamePlayer1_Turn1 = new Salvo (gamePlayer1, 1, salvoLocation1);
                    Salvo GamePlayer1_Turn2 = new Salvo (gamePlayer1, 2, salvoLocation2);
                    Salvo GamePlayer2_Turn1 = new Salvo (gamePlayer2, 1, salvoLocation3);
                    Salvo GamePlayer2_Turn2 = new Salvo (gamePlayer2, 2, salvoLocation4);

                    //points
                    Score score1 = new Score(player1, game1);
                    Score score2 = new Score(player2, game1);
                    Score score3 = new Score(player3, game2);
                    Score score4 = new Score(player4, game2);

                    //scores
                    score1.setScore(1);
                    score2.setScore(1);
                    score3.setScore(0.5);
                    score4.setScore(0);

                    // asigno los barcos a las jugadas
//                    gamePlayer1.addShip(ship1);
//                    gamePlayer2.addShip(ship2);
//                    gamePlayer3.addShip(ship3);
//                    gamePlayer4.addShip(ship4);


                    //info en los repositorios
                    playerRepository.save(player1);
                    playerRepository.save(player2);
                    playerRepository.save(player3);
                    playerRepository.save(player4);

                    gameRepository.save(game1);
                    gameRepository.save(game2);
                    gameRepository.save(game3);
                    gameRepository.save(game4);


                    gamePlayerRepository.save(gamePlayer1);
                    gamePlayerRepository.save(gamePlayer2);
                    gamePlayerRepository.save(gamePlayer3);
                    gamePlayerRepository.save(gamePlayer4);

                    //lista de barcos
                    List<Ship> listOfShips = new LinkedList<>();
                    listOfShips.addAll(new ArrayList<>(Arrays.asList(ship1,ship2, ship3,ship4)));
                    shipRepository.saveAll(listOfShips);

                    List<Salvo> listOfSalvos = new LinkedList<>();
                    listOfSalvos.addAll(new ArrayList<>(Arrays.asList(GamePlayer1_Turn1,GamePlayer1_Turn2,GamePlayer2_Turn2,
                            GamePlayer2_Turn1)));
                    salvoRepository.saveAll(listOfSalvos);


                    List<Score> listOfScores = new LinkedList<>();
                    listOfScores.addAll(new ArrayList<>(Arrays.asList(score1,score2,score3,score4)));
                    scoreRepository.saveAll(listOfScores);

            };

    }
}
