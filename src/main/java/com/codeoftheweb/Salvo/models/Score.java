package com.codeoftheweb.Salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date finishDate;
    private double score;

    @ManyToOne (fetch= FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    /*Aqui yo lo haria com private string score. meli pone Date finish date.
    Ademas la relacion que yo haria seria un jugador para mcuhos scores y un juego para muchos jugadores.
    */

    //CONSTRUCTORES

    public Score(Game game, Player opponentPlayer, double v, Date date) {
    }

    public Score(Player player, Game game){
        this.player = player;
        this.game = game;
    }


    //SETTERS AND GETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    /////////////////////////////////////

    public Map<String, Object> makeScoreDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("player", this.getPlayer().getId());
        dto.put("score",this.getScore());
        dto.put("finishDate", this.getFinishDate());
        return dto;
    }


}
