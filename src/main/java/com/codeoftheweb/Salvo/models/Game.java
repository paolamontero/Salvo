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

    //CONSTRUCTORES

    public Game() {
        this.creationDate = new Date();
    }

    //metodos

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        GamePlayer.add(gamePlayer);
    }


    public Map<String, Object> makeGameDTO() {

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("created", creationDate);
        dto.put("gamePlayers", gamePlayers
                .stream()
                .map(unGamePlayer -> unGamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList())
                );
        return dto;
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


}



