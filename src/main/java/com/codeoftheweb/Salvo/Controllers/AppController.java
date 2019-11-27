package com.codeoftheweb.Salvo.Controllers;

import com.codeoftheweb.Salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.Repository.GameRepository;
import com.codeoftheweb.Salvo.Repository.PlayerRepository;
import com.codeoftheweb.Salvo.Repository.ShipRepository;
import com.codeoftheweb.Salvo.models.Game;
import com.codeoftheweb.Salvo.models.GamePlayer;
import com.codeoftheweb.Salvo.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


//    @RequestMapping("players")
//    public List<Players> getAll(Authentication authentication) {
//        return repo.findByUserName(authentication.getUserName());
//    }

    @RequestMapping("/games")
    public List<Object> getGamesAll() {

        return gameRepository.findAll()
                .stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList())
                ;
    }

    @RequestMapping("/player")
    public List<Object> getPlayersAll() {

        return playerRepository.findAll()
                .stream()
                .map(player -> player.makePlayerDTO())
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameViewAll(@PathVariable long nn) {

        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).get();
        Game game = gamePlayer.getGame();

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
        return dto;
    }

    @RequestMapping("leaderBoard")
    public List<Object> getAllScores() {
        List<Object> allPlayers = playerRepository.findAll()
                .stream()
                .map(player -> player.makeCalculoPointsDTO())
                .collect(Collectors.toList());

        return allPlayers;
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register (
            @RequestParam String userName, @RequestParam String password)  {

        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}