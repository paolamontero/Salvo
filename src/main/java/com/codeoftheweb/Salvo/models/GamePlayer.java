package com.codeoftheweb.Salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships; // = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvos = new HashSet<>();

    private Date joinDate;

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

    public Score getScore (){
        Score score = this.player.getScore(this.game.getId());
        return score;
    }

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

 //AQUI HABIA UN PUBLIC STATIC VOID PERO MATI ME DIJO QUE LO QUITARA, SI ESTOY EN GAMEPLAYER COMO ME VOY A ENVIAR OtRO GAMEPLAYER.
    //lo ideal (que es como lo tengo) seria agregarlo pero en el constructor de shp.

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    ///////////////////

    public Map<String, Object> makeGamePlayerDTO() {

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("player", player.makePlayerDTO());

        return dto;
    }

    public List<Map<String, Object>> getAllShips(){
        return this.getShips()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList());
    }

    public List<Map<String,Object>> getAllSalvos() {
        return this.getGame().getGamePlayers()
                .stream()
                .flatMap(_gamePlayer -> _gamePlayer.getSalvos()
                        .stream())
                .map(salvo -> salvo.makeSalvoDto())
                .collect(Collectors.toList());
    }

    public GamePlayer getOpponent(){
        return this.getGame().getGamePlayers().stream()
                .filter(gamePlayers -> gamePlayers.getId() != this.getId())
                .findFirst().orElse(new GamePlayer());

//    public void addShip(Ship ship1) {
//        ships.add(ship1); //no podia poner addShip porque estaba llamando al metdo una y otra vez y se hacia bcle infinito. llamo a la funcion de la lista.
//    }

}
}





