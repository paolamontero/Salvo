
package com.codeoftheweb.Salvo.Controllers;

import com.codeoftheweb.Salvo.Repository.*;
import com.codeoftheweb.Salvo.models.*;
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
public class AppController<CREATED> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    ShipRepository shipRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    SalvoRepository salvoRepository;

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
    public Map<String, Object> getGamesAll(Authentication authentication) {
        Map<String,Object> map = new LinkedHashMap<>();
        if (isGuest(authentication))
            map.put("player", "Guest");
        else map.put("player", playerRepository.findByUserName(authentication.getName()).makePlayerDTO());
        map.put("games",gameRepository.findAll()
                .stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList()));
        return map;
    } //antes tenia solo games, y ahora le agrego player como gest o el usuario logged.

    @RequestMapping("/players")
    public List<Object> getPlayersAll() {

        return playerRepository.findAll()
                .stream()
                .map(player -> player.makePlayerDTO())
                .collect(Collectors.toList());
    }
//--- --------      esto es para los players!

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty())  {
            return new ResponseEntity<>(makeMap("error", "Faltan datos"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByUserName(email);
        if (player != null) {
            return new ResponseEntity<>((Map<String, Object>) makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
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
    // en la tarea 5 solo modificamos en game view supuestamente.

    @RequestMapping("/game_view/{gpid}")
    //public Map<String, Object> getGameViewAll(@PathVariable long nn) {
    public ResponseEntity<Map<String, Object>> getGame(@PathVariable long gpid, Authentication authentication) {

        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);
        Game game = gamePlayer.getGame();
        Player playerLogged = playerRepository.findByUserName(authentication.getName());

        if(gamePlayer   ==  null){
            return new ResponseEntity<>(makeMap("error", "No gamePlayer given"), HttpStatus.UNAUTHORIZED);
        }

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No name given"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getPlayer().getId() != playerLogged.getId()) {
            return new ResponseEntity<>(makeMap("error", "wrong player id"), HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> hits = new LinkedHashMap<>();
        Map<String, Object> dto = new LinkedHashMap<>();

        if (gamePlayer.getOpponent() == null) {
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());

        } else {
            hits.put("self", gamePlayer.makeHitsDTO());
            hits.put("opponent", gamePlayer.getOpponent().makeHitsDTO());
        }

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
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(_gamePlayer -> _gamePlayer.getSalvos()
                        .stream())
                .map(salvo -> salvo.makeSalvoDto())
                .collect(Collectors.toList()));
        dto.put("hits", hits);
        dto.put("gameState", getState(gamePlayer, gamePlayer.getOpponent()));

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
//            return dto;
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

    public String getState(GamePlayer gamePlayerSelf, GamePlayer gamePlayerOpponent) { /// aqui empieza la tarea6
        //------------------esto ya lo tenia

        if(gamePlayerSelf.getShips().isEmpty()){
            return "PLACESHIPS";
        }

        if(gamePlayerSelf.getGame().getGamePlayers().size() == 1){
            return "WAITINGFOROPP";
        }

        if(gamePlayerSelf.getOpponent().getShips().isEmpty()){
            return "WAITINGFOROPP";
        }


        if(gamePlayerSelf.getSalvos().size() == gamePlayerOpponent.getSalvos().size()) {

            Player selfPlayer = gamePlayerSelf.getPlayer();
            Player opponentPlayer = gamePlayerOpponent.getPlayer();
            Game game = gamePlayerSelf.getGame();
            if (Util.allShipsDown(gamePlayerSelf) == true && Util.allShipsDown(gamePlayerOpponent) == true) {
                Score scoreSelf = new Score  (game, selfPlayer, 0.5, new Date()); //(game, selfPlayer, 0.5, new Date());
                Score scoreOpponent = new Score(game, opponentPlayer, 0.5, new Date());
                if(!existScore(scoreSelf, game)){
                    scoreRepository.save(scoreSelf);
                    scoreRepository.save(scoreOpponent);
                }
                return "TIE";
            }

            if( (Util.allShipsDown(gamePlayerSelf) == true) &&
                    Util.allShipsDown(gamePlayerOpponent) == false){
                Score scoreSelf = new Score(game, selfPlayer, 0.0, new Date());
                Score scoreOpponent = new Score(game, opponentPlayer, 1.0, new Date());
                if(!existScore(scoreSelf, game)){
                    scoreRepository.save(scoreSelf);
                    scoreRepository.save(scoreOpponent);
                }
                return "LOST";

            } if( (Util.allShipsDown(gamePlayerSelf) == false) &&
                    Util.allShipsDown(gamePlayerOpponent) == true){
                Score scoreSelf = new Score(game, selfPlayer, 1.0, new Date());
                Score scoreOpponent = new Score(game, opponentPlayer, 0.0, new Date());
                if(!existScore(scoreSelf, game)) {
                    scoreRepository.save(scoreSelf);
                    scoreRepository.save(scoreOpponent);
                }
                return "WON";
            }
        }

        if (gamePlayerSelf.getSalvos().size() > gamePlayerOpponent.getSalvos().size()) {
            return "WAIT";
        }

        return "PLAY";
    }

    public boolean existScore (Score score, Game game){
        boolean existScore = false;
        if(!game.getScores().isEmpty()){
            existScore = true;
        }
        return existScore;
    }



    //Tarea 4: Implemente un metodo controlador para almacenar salvos (igual a la 3,) esto es la parte uno, aun falta la parte 2
    @RequestMapping (path = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity <Map<String,Object>> saveSalvos(@PathVariable long gamePlayerId, @RequestBody Salvo shot, Authentication authentication){

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        Player playerLogged = playerRepository.findByUserName(authentication.getName());

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "You cannot join the game while you are not logged in"),
                    HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayerRepository.findById(gamePlayerId)  == null){
            return new ResponseEntity<>(makeMap("error", "you dont have a valid gamePlayer id"), HttpStatus.UNAUTHORIZED);
        }


        if(gamePlayer.getPlayer().getId() != playerLogged.getId() ){
            return new ResponseEntity<>(makeMap("error", "your userName is not added to the gamePlayer listed"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getSalvos().stream().count() > gamePlayer.getOpponent().getSalvos().stream().count()) {
            return new ResponseEntity<>(makeMap("error", "Salvos already shot"), HttpStatus.FORBIDDEN);
        }

        if(gamePlayer.getSalvos().stream().filter(salvo1 -> salvo1.getTurn() == shot.getTurn()).count() > 0){
            new ResponseEntity<>(makeMap("error", "you already have fired in this turn"), HttpStatus.FORBIDDEN);
        }

        if(shot.getSalvoLocations().size() != 5){
            return new ResponseEntity<>(makeMap("error", "debes disparar 5 veces"), HttpStatus.CONFLICT);
        }

        int turn = gamePlayer.getSalvos().size();
        if(gamePlayer.getSalvos().isEmpty()){
            shot.setTurn(1);
            shot.setGamePlayer(gamePlayer);
            salvoRepository.save(shot);
            turn++;
        }
        else {
            shot.setTurn(turn+1);
            shot.setGamePlayer(gamePlayer);
            salvoRepository.save(shot);
        }

//        shot.setGamePlayer(gamePlayer);
//        gamePlayerRepository.save(gamePlayer);

        return new ResponseEntity<>(makeMap("OK","disparos lanzados"), HttpStatus.CREATED);
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

}