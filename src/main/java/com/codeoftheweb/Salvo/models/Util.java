package com.codeoftheweb.Salvo.models;

import java.util.*;
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
                                        .flatMap(shipLocations -> salvo.getSalvoLocations()
                                                .stream().filter(salvoLocations -> shipLocations.contains(salvoLocations))))
                                .collect(Collectors.toList()))
                ;

            }
        }

        return hitsLocations;
    }

}
