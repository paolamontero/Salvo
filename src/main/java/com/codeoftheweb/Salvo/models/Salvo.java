package com.codeoftheweb.Salvo.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ElementCollection
    @Column(name = "salvoLocations")
    private List<String> salvoLocations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayerId")
    private GamePlayer gamePlayer;

    private int turn;

    public Salvo() {
    }

    public Salvo(GamePlayer gamePlayer, int turn, List salvoLocations) {
        this.setGamePlayer(gamePlayer);
        this.setTurn(turn);
        this.salvoLocations(salvoLocations);
    }

    //Setter and Getter

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private List<String> getSalvoLocation() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    private void salvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }
//////////////////////////////////////////////

    public Map<String, Object> makeSalvoDto() {
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("turn", this.getTurn());
        dto.put("salvoLocations", this.getSalvoLocation());
        dto.put("player", this.getGamePlayer().getPlayer().getId());

        return dto;
    }
}


/// settter and getters para que sirven: para establecer, pues nos sirve para asignar un valor inicial al atributo.
// seria para establecer y obtener!!