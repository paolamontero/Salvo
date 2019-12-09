package com.codeoftheweb.Salvo.models;


import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {

    public static boolean allShipsDown (GamePlayer gamePlayer){
        boolean allShipsDown= false;

        if(hitsLocations(gamePlayer).size() == (allShipsLocations(gamePlayer)).size()){
            allShipsDown = true;
        }
        return allShipsDown;
    }

    public static Set<String> allShipsLocations (GamePlayer gamePlayer){
        Set<String> allShipsLocations = new LinkedHashSet<>();

        for(Ship ship : gamePlayer.getShips())
            allShipsLocations.addAll(ship.getShipLocations());

        return allShipsLocations;
    }
//

    public static List<String> hitsLocations (GamePlayer gamePlayer){
        List<String> hitsLocations = new LinkedList<>();
        GamePlayer opponent = gamePlayer.getOpponent();

        if(gamePlayer.getId() != 0 && gamePlayer.getOpponent().getSalvos() != null ) {
            for (Salvo salvo : opponent.getSalvos()) {

                hitsLocations.addAll(
                        gamePlayer.getShips()
                                .stream()
                                .flatMap(ship -> ship.getShipLocations()
                                        .stream()
                                        .flatMap(shipsLocations -> salvo.getSalvoLocations()
                                                .stream().filter(salvoLocations -> shipsLocations.contains(salvoLocations))))
                                .collect(Collectors.toList()));

            }
        }

        return hitsLocations;
    }

}