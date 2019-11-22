package com.codeoftheweb.Salvo.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    ///////////////////////////////////////////////

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    private Date joinDate;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships; // = new HashSet<>();

    //CONSTRUCTORES
    public GamePlayer() {
    }

    public GamePlayer(Date joinDate) {
        this.joinDate = new Date();
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.joinDate = new Date();
    }

    //METODOS

    public Map<String, Object> makeGamePlayerDTO() {

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("player", player.makePlayerDTO());

        return dto;
    }

    public void addShip(Ship carrier) {
        this.ships.add(carrier);
    }
//
//    public static void add(GamePlayer gamePlayer) {
//    }

    //SETTER y GETTER

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
    
    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }
}






