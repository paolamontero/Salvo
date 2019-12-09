package com.codeoftheweb.Salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvos = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships; // = new HashSet<>();

    private int missed; // esto lo agregamos por la tarea5 (el JSON te pone missed)

    private Date joinDate;

    public GamePlayer() {
    }

    public GamePlayer(Date joinDate) {
        this.joinDate = new Date();
        this.ships = new HashSet<>();
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.joinDate = new Date();
        this.ships = new HashSet<>();
    }

    public Score getScore() {
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

    public List<Map<String, Object>> getAllShips() {
        return this.getShips()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllSalvos() {
        return this.getGame().getGamePlayers()
                .stream()
                .flatMap(_gamePlayer -> _gamePlayer.getSalvos()
                        .stream())
                .map(salvo -> salvo.makeSalvoDto())
                .collect(Collectors.toList());
    }

    public GamePlayer getOpponent() {
        return this.getGame()
                .getGamePlayers()
                .stream()
                .filter(gamePlayers -> gamePlayers.getId() != this.getId())
                .findFirst().orElse(null); // mejor ponerle null porque si le pones game player, le pedira datos a un gp que no esta
    }

    public void addShip(Ship ship1) {
        ships.add(ship1); //no podia poner addShip porque estaba llamando al metdo una y otra vez y se hacia bcle infinito. llamo a la funcion de la lista.
    }


    //*-----------------------------------------------------------* aqui comienzo la tarea 5

    public List<Map<String, Object>> makeHitsDTO() {
        List<Map<String, Object>> dto = new LinkedList<>();
        List<Salvo> salvosInOrder = getOpponent().getSalvos().stream().sorted(Comparator.comparingInt(Salvo::getTurn)).collect(Collectors.toList());
        salvosInOrder.stream().forEach(salvo -> dto.add(makeHits(salvo)));
        return dto;

    }


    public Map<String, Object> makeHits(Salvo salvo) {

        Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("turn", salvo.getTurn());
            dto.put("hitLocations", this.getHitsLocations( salvo));
            dto.put("damages", this.getShipByType(salvo));
            dto.put("missed", missed);
        return dto;
    }

    int carrier = 0; int battleship = 0; int submarine = 0; int destroyer = 0; int patrolboat = 0;

    public List<String> getHitsLocations(Salvo salvoOpponent) {
        return ships
                .stream()
                .flatMap(ship -> ship.getShipLocations()
                        .stream()
                        .flatMap(shipLocation -> salvoOpponent
                                .getSalvoLocations()
                                .stream()
                                .filter(salvoLocation -> shipLocation.contains(salvoLocation))))
                .collect(Collectors.toList());
    }


    public Map<String, Object> getShipByType(Salvo salvo) {
        Map<String, Object> dtoBarcos = new LinkedHashMap<>();


        int carrierHits = this.countHits(ships
                .stream()
                .filter(ship -> ship.getShipType() == "carrier")
                .findFirst()
                .orElse(new Ship()),  salvo);

        int battleshipHits = this.countHits(ships
                .stream()
                .filter(ship -> ship.getShipType() == "battleship")
                .findFirst()
                .orElse(new Ship()), salvo);

        int submarineHits = this.countHits(ships
                .stream()
                .filter(ship -> ship.getShipType() == "submarine")
                .findFirst()
                .orElse(new Ship()), salvo);

        int destroyerHits = this.countHits(ships
                .stream()
                .filter(ship -> ship.getShipType() == "destroyer")
                .findFirst()
                .orElse(new Ship()),  salvo);

        int patrolboatHits = this.countHits(ships
                .stream()
                .filter(ship -> ship.getShipType() == "patrolboat")
                .findFirst()
                .orElse(new Ship()),  salvo);

        int ps = (carrierHits + battleshipHits + submarineHits + destroyerHits + patrolboatHits);
        this.missed = 5;
        this.missed -= ps;

        dtoBarcos.put("carrier", carrier += carrierHits);
        dtoBarcos.put("battleship", battleship += battleshipHits);
        dtoBarcos.put("submarine", submarine += submarineHits);
        dtoBarcos.put("destroyer", destroyer += destroyerHits);
        dtoBarcos.put("patrolboat", patrolboat += patrolboatHits);
        dtoBarcos.put("carrierHits", carrierHits);
        dtoBarcos.put("battleshipHits", battleshipHits);
        dtoBarcos.put("submarineHits", submarineHits);
        dtoBarcos.put("destroyerHits", destroyerHits);
        dtoBarcos.put("patrolboatsHits", patrolboatHits);

        return dtoBarcos;
    }

    public int countHits(Ship ship, Salvo salvo) {

        int totalHits = getHitsLocations( salvo).size();
        int pointCounter = 0;

        if (ship.getShipType() != null && totalHits != 0) {

            for (String shipLocation : ship.getShipLocations()) {
                if (getHitsLocations(salvo).contains(shipLocation)) {
                    pointCounter++;
                }
            }
        }

        return pointCounter;
    }

}


//segun como le mande el game player me va a devolver los datos.

 /*Tanto map como flatMap se pueden aplicar a un Stream<T> y ambos devuelven un Stream<R>.
        La diferencia es que la map produce un valor de salida para cada valor de entrada!!!!
         mientras que flatMap produce un número arbitrario (cero o más) valores para cada valor de entrada.

        OPCIONES DE TAREA 5


    public Map<String, Object> makeHitsDTO(Salvo salvo, GamePlayer gamePlayer) {
        for (Salvo salvo : .getSalvo ) //{
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
//deberia crear algo que acumule los golpes recbids durante turnos
        dto.put("hitsLocation", getHitsLocations); // tengo que hacer la lista de hits location
        dto.put("turn", salvo.getTurn()); //Map<String,Object> damagesPerTurn= new LinkedHashMap<>();
        dto.put("damages", makeDamageDTO()); //y tengo que hacer la lista de damges


       tengo - damage per turn -hitslocation  -y los damages que tinies que poner el find by ship type//esto podria ir  con un condicional.

       podria ser con un List<String> getShipLocationBytype = new ArrayList<>();
            Set<String> carrierLocation = getShipLocationByTyp

            SET PODRIA CAUSAR PROBELAMS Y PODRIA REPETIR DATOS, por eso creamos listaa
 */



//-----------------------------------------------------------------

//    public List<Object> getHitsLocation(GamePlayer gamePlayer){
//        return gamePlayer.getShips()
//                .stream()
//                .flatMap(ship -> ship.getShipLocations()
//                        .stream()
//                        .flatMap(shipLocation -> gamePlayer
//                                .getOpponent()
//                                .getSalvos()
//                                .stream()
//                                .flatMap(salvo -> salvo
//                                        .getSalvoLocations()
//                                        .stream()
//                                        .filter(salvoLocation-> shipLocation.contains(salvoLocation)))))
//                .collect(Collectors.toList());
//    }
//
//}






