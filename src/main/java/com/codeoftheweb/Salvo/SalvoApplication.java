package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.Repository.*;
import com.codeoftheweb.Salvo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import java.util.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

      @Bean
        public PasswordEncoder passwordEncoder() {
         return PasswordEncoderFactories.createDelegatingPasswordEncoder();
      }


	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Autowired
    PasswordEncoder passwordEncoder;

	@Autowired
    GameRepository gameRepository;
	@Autowired
    GamePlayerRepository gamePlayerRepository;

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
            return (args) -> {

                    //creacion de jugadores
                    Player /*1*/ player1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
                    Player /*2*/ player2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42"));
                    Player /*3*/ player3= new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
                    Player /*4*/ player4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("moles"));

                    //creacion de juegos
                    Game game1 = new Game(new Game());
                    Game game2 = new Game(new Game());
                    Game game3 = new Game(new Game());
                    Game game4 = new Game(new Game());
                    Game game5 = new Game (new Game());
                    Game game6 = new Game (new Game());


                    // creacion gamePlayers
                    GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
                    GamePlayer gamePlayer2 = new GamePlayer(game1, player2);

                    GamePlayer gamePlayer3 = new GamePlayer(game2, player1);
                    GamePlayer gamePlayer4 = new GamePlayer(game2, player2);

                    GamePlayer gamePlayer5 = new GamePlayer(game3, player2);
                    GamePlayer gamePlayer6 = new GamePlayer(game3, player4);

                    GamePlayer gamePlayer7 = new GamePlayer(game4, player2);
                    GamePlayer gamePlayer8 = new GamePlayer(game4, player1);

                    GamePlayer gamePlayer9 = new GamePlayer(game5, player4);
                    GamePlayer gamePlayer10 = new GamePlayer(game5, player1);

                    GamePlayer gamePlayer11 = new GamePlayer(game6, player3);
                    GamePlayer gamePlayer12 = new GamePlayer(game6, player4);


                List<String> shipLocation1 = new ArrayList<>();
                shipLocation1.add("H4");
                shipLocation1.add("H5");
                shipLocation1.add("H6");

                List<String> shipLocation2 = new ArrayList<>();
                shipLocation2.add("H9");
                shipLocation2.add("H10");
                shipLocation2.add("H11");

                List<String> shipLocation3 = new ArrayList<>();
                shipLocation3.add("C2");
                shipLocation3.add("C3");
                shipLocation3.add("C4");

                List<String> shipLocation4 = new ArrayList<>();
                shipLocation4.add("F1");
                shipLocation4.add("F2");
                shipLocation4.add("F3");

                List<String> shipLocation5 = new ArrayList<>();
                shipLocation5.add("H9");
                shipLocation5.add("H10");
                shipLocation5.add("H11");

                List<String> shipLocation6 = new ArrayList<>();
                shipLocation1.add("K1");
                shipLocation1.add("K2");
                shipLocation1.add("K3");

                List<String> shipLocation7 = new ArrayList<>();
                shipLocation1.add("E1");
                shipLocation1.add("H3");
                shipLocation1.add("A2");

                List<String> shipLocation8 = new ArrayList<>();
                shipLocation1.add("G6");
                shipLocation1.add("H6");
                shipLocation1.add("A4");

                List<String> shipLocation9 = new ArrayList<>();
                shipLocation1.add("A2");
                shipLocation1.add("A3");
                shipLocation1.add("D8");

                List<String> shipLocation10 = new ArrayList<>();
                shipLocation1.add("E1");
                shipLocation1.add("F2");
                shipLocation1.add("G3");

                List<String> shipLocation11 = new ArrayList<>();
                shipLocation1.add("H4");
                shipLocation1.add("H5");
                shipLocation1.add("H6");

                List<String> shipLocation12 = new ArrayList<>();
                shipLocation1.add("H1");
                shipLocation1.add("H8");

                List<String> shipLocation13 = new ArrayList<>();
                shipLocation1.add("C5");
                shipLocation1.add("C6");

                List<String> shipLocation14 = new ArrayList<>();
                shipLocation1.add("C6");
                shipLocation1.add("D6");
                shipLocation1.add("E6");

                List<String> shipLocation15 = new ArrayList<>();
                shipLocation1.add("A1");
                shipLocation1.add("A2");
                shipLocation1.add("A3");

                List<String> shipLocation16 = new ArrayList<>();
                shipLocation1.add("E1");
                shipLocation1.add("F2");
                shipLocation1.add("G3");
                shipLocation1.add("G4");

                List<String> shipLocation17 = new ArrayList<>();
                shipLocation1.add("A4");
                shipLocation1.add("A5");
                shipLocation1.add("A6");
                shipLocation1.add("A7");

                List<String> shipLocation18 = new ArrayList<>();
                shipLocation1.add("H8");
                shipLocation1.add("H9");
                shipLocation1.add("H10");
                shipLocation1.add("H11");

                List<String> shipLocation19 = new ArrayList<>();
                shipLocation1.add("A2");
                shipLocation1.add("A3");
                shipLocation1.add("A4");
                shipLocation1.add("D8");



                //horarios
            game1.setCreationDate(Date.from(game1.getCreationDate().toInstant().plusSeconds(3600)));
            game2.setCreationDate(Date.from(game2.getCreationDate().toInstant().plusSeconds(3600)));
            game3.setCreationDate(Date.from(game3.getCreationDate().toInstant().plusSeconds(3600)));
            game4.setCreationDate(Date.from(game4.getCreationDate().toInstant().plusSeconds(3600)));


                //ships game 1
                Ship ship1 = new Ship("Carrier", gamePlayer1, shipLocation1);
                Ship ship2 = new Ship("Destroyer", gamePlayer2, shipLocation2);
                Ship ship3 = new Ship("Submarine", gamePlayer1, shipLocation3);
                Ship ship4 = new Ship("Battleship", gamePlayer3, shipLocation11);
                Ship ship5 = new Ship("Patrol Boat", gamePlayer3, shipLocation5);
                Ship ship6 = new Ship("Patrol Boat", gamePlayer3, shipLocation8);

                //ships game2
                Ship ship7 = new Ship("Patrol Boat", gamePlayer3, shipLocation15);
                Ship ship8 = new Ship("Patrol Boat", gamePlayer3, shipLocation6);
                Ship ship9 = new Ship("Battleship", gamePlayer3, shipLocation9);
                Ship ship10 = new Ship("Battleship", gamePlayer3, shipLocation17);


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
                    List<String> salvoLocation5 = new ArrayList<>();
                    salvoLocation5.add("J6");
                    salvoLocation5.add("J7");

                    List<String> salvoLocation6 = new ArrayList<>();
                    salvoLocation1.add("A1");
                    salvoLocation1.add("A2");

                    //game4
                    List<String> salvoLocation7 = new ArrayList<>();
                    salvoLocation2.add("C2");
                    salvoLocation2.add("C3");
                    salvoLocation2.add("C4");

                    List<String> salvoLocation8 = new ArrayList<>();
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
                    gameRepository.save(game5);
                    gameRepository.save(game6);


                    gamePlayerRepository.save(gamePlayer1);
                    gamePlayerRepository.save(gamePlayer2);
                    gamePlayerRepository.save(gamePlayer3);
                    gamePlayerRepository.save(gamePlayer4);
                    gamePlayerRepository.save(gamePlayer6);
                    gamePlayerRepository.save(gamePlayer7);
                    gamePlayerRepository.save(gamePlayer8);
                    gamePlayerRepository.save(gamePlayer9);
                    gamePlayerRepository.save(gamePlayer10);
                    gamePlayerRepository.save(gamePlayer11);
                    gamePlayerRepository.save(gamePlayer12);

                    //lista de barcos

                    List<Ship> listOfShips = new LinkedList<>();
                    listOfShips.addAll(new ArrayList<>(Arrays.asList(ship1,ship2, ship3,ship4)));
                    shipRepository.saveAll(listOfShips);

                    List<Salvo> listOfSalvos = new ArrayList<>();
                    listOfSalvos.addAll(new ArrayList<>(Arrays.asList(GamePlayer1_Turn1,GamePlayer1_Turn2,GamePlayer2_Turn2,
                            GamePlayer2_Turn1)));
                    salvoRepository.saveAll(listOfSalvos);


                    List<Score> listOfScores = new LinkedList<>();
                    listOfScores.addAll(new ArrayList<>(Arrays.asList(score1,score2,score3,score4)));
                    scoreRepository.saveAll(listOfScores);

            };
    }

}

//AUTENTICACION
@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            Player player = playerRepository.findByUserName(inputName);
            if (player != null) {
                return new User(player.getUserName(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }

    @SpringBootApplication
    public class Application extends SpringBootServletInitializer {

    }

    @Configuration
    @EnableWebSecurity
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/web/**").permitAll()
                    .antMatchers("/api/games").permitAll()
                    .antMatchers("/api/players", "/api/login", "/api/logout").permitAll()
                    .antMatchers("/rest").denyAll()
                    .antMatchers("/web/games.html").permitAll()
                    .antMatchers("/api/users").permitAll()
                    .antMatchers("/web/game.html?gp=*", "/api/game_view/*").hasAuthority("USER")
                    .anyRequest().denyAll()
//                .antMatchers("/*").permitAll()
            ;
//-------------------------------------------------------------------------------------------------------
            http.formLogin()
                    .usernameParameter("name")
                    .passwordParameter("pwd")
                    .loginPage("/api/login");
            http.logout().logoutUrl("/api/logout");
//--------------------------------------------------------------------------------------------------------
            // turn off checking for CSRF tokens
            http.csrf().disable();
            // if user is not authenticated, just send an authentication failure response
            http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
            // if login is successful, just clear the flags asking for authentication
            http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
            // if login fails, just send an authentication failure response
            http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
            // if logout is successful, just send a success response
            http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        }

        private void clearAuthenticationAttributes(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }
    }
}











