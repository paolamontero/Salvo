package com.codeoftheweb.Salvo.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date creationDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    //
    public Game() {
        this.creationDate = new Date();
    }
    public Game(Game game) { // podria hacer una copia de otro  game (nunca pasar new Game por parametros) by Mati.e
        this.setScores(game.getScores());
        this.creationDate = new Date();
    }

    public Game(Date asdfdafds) {
        this.creationDate = asdfdafds;
    }
    //SETTERS AND GETTERS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    /////////////////////////////////////

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        addGamePlayer(gamePlayer);
    }


    public Map<String, Object> makeGameDTO() {

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("created", creationDate);

        dto.put("gamePlayers", this.getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList()));

        dto.put("scores", this.getScores()
                .stream()
                .map(score -> score.makeScoreDTO())
                .collect(Collectors.toList()));
        return dto;
    }

    public List<Map<String, Object>> getAllSalvosFromGamePlayers(){
        return gamePlayers.
                stream()
                .flatMap(gamePlayer -> gamePlayer.getSalvos().stream())
                .map(salvo -> salvo.makeSalvoDto())
                .collect(Collectors.toList());
    }

}