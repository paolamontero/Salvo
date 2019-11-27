package com.codeoftheweb.Salvo.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    private String userName;
    private String password;

    public Player() {}

    public Player(String userName, String password) {
        this.password = password;
        this.userName = userName;
    }

    //setter y getter

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //////////////

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("email", userName);

        return dto;
    }

    public Score getScore (long gameId){
        Score score = scores.stream()
            .filter(score1 -> score1.getGame().getId()== gameId)
            .findFirst()
            .orElse(null);
        return score;
    }

    public Map<String, Object> makeCalculoPointsDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();

        double wins, losses, ties, totalScore;
        wins = scores.stream().filter(points -> points.getScore() == 1.0).count();
        losses = scores.stream().filter(points -> points.getScore() == 0).count();
        ties = scores.stream().filter(points -> points.getScore() == 0.5).count();
        totalScore = (wins * 1) + (losses *0) + (ties * 0.5);

        dto.put("player", this.getUserName());
        dto.put("totalScore", totalScore);
        dto.put("wins", wins);
        dto.put("losses", losses);
        dto.put("ties", ties);

        return dto;

    }
}
