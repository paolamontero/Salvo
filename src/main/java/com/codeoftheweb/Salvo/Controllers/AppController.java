package com.codeoftheweb.Salvo.Controllers;

import com.codeoftheweb.Salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.Repository.GameRepository;
import com.codeoftheweb.Salvo.Repository.PlayerRepository;
import com.codeoftheweb.Salvo.Repository.ShipRepository;
import com.codeoftheweb.Salvo.models.Game;
import com.codeoftheweb.Salvo.models.GamePlayer;
import com.codeoftheweb.Salvo.models.Player;
import com.codeoftheweb.Salvo.models.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    ShipRepository shipRepository;

    @Configuration
    class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {


        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(inputUserName -> {
                Player player = playerRepository.findByUserName(inputUserName);
                if (player != null) {
                    return new User(player.getUserName(), player.getPassword(),  //User es un metodo que tengo que usar, porque es la forma correcta de hacerlo dentro del frame de spring
                            AuthorityUtils.createAuthorityList("USER")); // continuacion: es la manera de comunicar a spring, no hay un mmotivo especial dentro del codigo.
                } else {
                    throw new UsernameNotFoundException("Unknown user: " + inputUserName);
                }
            });
        }
    }

//-------------
    @RequestMapping("/games")
    public List<Object> getGamesAll() {

        return gameRepository.findAll()
                .stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList());
    }

    @RequestMapping("/player")
    public List<Object> getPlayersAll() {

        return playerRepository.findAll()
                .stream()
                .map(player -> player.makePlayerDTO())
                .collect(Collectors.toList());
    }
//--- --------      esto es para los players!

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName, @RequestParam String password) {

        if (userName.isEmpty() || password.isEmpty())  {
            return new ResponseEntity<>(makeMap("error", "Faltan datos"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>((Map<String, Object>) makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }

        playerRepository.save(new Player(userName, password));
        return new ResponseEntity<>(makeMap("OK","player created succesfully"),  HttpStatus.CREATED);
    }

    public Map<String,Object> makeMap(String key, Object value) {

        Map<String, Object> dto =new LinkedHashMap<>();
        dto.put(key, value);
        return dto;
    }

    //----- ESTO ES JOIN GAMEEE

    @RequestMapping(path = "/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity <Map<String, Object>> joinGame(@PathVariable long nn, Authentication authentication) {

        //GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player));
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Unable to join new game while not logged in"),
                    HttpStatus.UNAUTHORIZED);
        }

        Game game = gameRepository.findById(nn).get();
        if (game == null) {
            return new ResponseEntity<>(makeMap("error", "game does not exist"), HttpStatus.FORBIDDEN);
        }

        Player playerLoged = playerRepository.findByUserName(authentication.getName());
        if (game.getGamePlayers().size() < 2) {
            GamePlayer newGamePlayer = new GamePlayer(game, playerLoged);
            gamePlayerRepository.save(newGamePlayer);

            return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(makeMap("error", "Already two players in this game"), HttpStatus.FORBIDDEN);
        }
    }

        @RequestMapping(path = "/games", method = RequestMethod.POST)
        public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {

            if (isGuest(authentication)) {
                return new ResponseEntity<>(makeMap("error", "Unable to create new game while not logged in"),
                        HttpStatus.UNAUTHORIZED);
            }

            Player playerLogged = playerRepository.findByUserName(authentication.getName());

            if (playerLogged != null) {
                Game newGame = new Game();
                GamePlayer newGamePlayer = new GamePlayer(newGame, playerLogged);
                gameRepository.save(newGame);
                gamePlayerRepository.save(newGamePlayer);

                return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(makeMap("error", "Unable to find your username"), HttpStatus.UNAUTHORIZED);
            }
        }

//----------------
    @RequestMapping("/game_view/{gpid}")
    //public Map<String, Object> getGameViewAll(@PathVariable long nn) {
    public ResponseEntity<Map<String, Object>> getGame(@PathVariable long gpid, Authentication authentication) {

        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).get();
        Game game = gamePlayer.getGame();
        Player playerLogged = playerRepository.findByUserName(authentication.getName());

        if (isGuest(authentication)) {// verifico si es invitado, en ese caso no tiene permisos para ver game view
            return new ResponseEntity<>(makeMap("error", "No name given"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getPlayer().getId() != playerLogged.getId()) {
            return new ResponseEntity<>(makeMap("error", "wrong player id"), HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> hits = new LinkedHashMap<>();
        hits.put("self", new ArrayList<>());
        hits.put("opponent", new ArrayList<>());

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO())
                .collect(Collectors.toList()));

        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvos", gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(_gamePlayer -> _gamePlayer.getSalvos()
                        .stream())
                .map(salvo -> salvo.makeSalvoDto())
                .collect(Collectors.toList()));
        dto.put("hits", hits);
        dto.put("gameState", getState(gamePlayer, gamePlayer.getOpponent()));

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }
//--------------
    @RequestMapping("leaderBoard")
    public List<Object> getAllScores () {
        List<Object> allPlayers = playerRepository.findAll()
                .stream()
                .map(player -> player.makeCalculoPointsDTO())
                .collect(Collectors.toList());

        return (List<Object>) allPlayers;
    }
    public boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    //-- tarea 3: Implemente un metodo controlador para una lista de barcos colocados :

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable long gamePlayerId, @RequestBody List<Ship> ships, Authentication authentication) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Unable to join new game while not logged in"),
                    HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayerRepository.findById(gamePlayerId)  == null){
            return new ResponseEntity<>(makeMap("error", "not a valid gamePlayer id"), HttpStatus.UNAUTHORIZED);
        }

        Player playerLogged = playerRepository.findByUserName(authentication.getName()); //
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();

        if(gamePlayer.getPlayer().getId() != playerLogged.getId() ){
            return new ResponseEntity<>(makeMap("error", "your userName is not added to the gameplayer listed"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getShips().size()!= 0){
            return new ResponseEntity<>(makeMap("error", "your userName is not added to the gameplayer listed"), HttpStatus.FORBIDDEN);
        }

        if(gamePlayer.getShips().stream().count() != 0){
            return new ResponseEntity<>(makeMap("error", "the gamePlayer has ships"), HttpStatus.FORBIDDEN);
        }

        ships.stream().forEach(ship -> ship.setGamePlayer(gamePlayer));
        shipRepository.saveAll(ships);
        gamePlayerRepository.save(gamePlayer);

        return new ResponseEntity<>(makeMap("OK","ships added"), HttpStatus.CREATED);
    }


    public String getState(GamePlayer gamePlayerSelf, GamePlayer gamePlayerOpponent){
        if(gamePlayerSelf.getShips().isEmpty()){
            return "PLACESHIPS";
        }

        if(gamePlayerSelf.getGame().getGamePlayers().size() == 1){
            return "WAITINGFOROPP";
        }

        if(gamePlayerSelf.getId() < gamePlayerOpponent.getId()){
            return "PLAY";
        }

        if (gamePlayerSelf.getId() > gamePlayerOpponent.getId()){
            return "WAIT";
        }

        return "LOST";
    }
}


/* para games
public Map<String, Object> getGameAll(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (Util.isGuest(authentication)) {
            dto.put("player", "Guest");
        } else {
            Player playerAutenticado = playerRepository.findByUserName((authentication.getName()));
            dto.put("player", playerAutenticado.makePlayerDTO());
        }*/