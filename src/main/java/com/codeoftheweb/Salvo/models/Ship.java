package com.codeoftheweb.Salvo.models;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;


@Entity
public class Ship {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ElementCollection
    @Column(name = "shipLocations")
    private List<String> shipLocations = new ArrayList<>();

    private String shipType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship(){}

    public Ship(String shipType) {this.shipType = shipType;}

//SETTER AND GETTER

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public void setShipLocation(List<String> shipLocation) {
        this.shipLocations = shipLocation;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}