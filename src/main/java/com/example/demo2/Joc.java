package com.example.demo2;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Joc implements Serializable {

    private List<Player> players = new ArrayList<>();

    private List<Enemic> enemics= new ArrayList<>();

    private List<Bala> bales = new ArrayList<>();




    // private boolean fightOn;

    public Joc() {
    }



    public Joc(List<Player> players) {
        this.players = players;
    }

    public  List<Player> getPlayers() {
        return players;
    }

    public  void setPlayers(List<Player> players) {
        this.players = players;
    }

    public  List<Bala> getBales() {
        return bales;
    }

    public  void setBales(List<Bala> bales) {
        this.bales = bales;
    }

    public List<Enemic> getEnemics(){
        return this.enemics;
    }

    public void setEnemics(List<Enemic> enemics) {
        this.enemics = enemics;
    }

    /*
        Amb el fightOn sabem si estem al temps de descans o enmig d'una oleada:
        El servidor inicia el temps de descans quan li arriba que ja no queden enemics.
        Quan el temps finalitza el servidor posa el fightOn a true i envia una nova ronda d'enemics
        Quan el fightOn és true el client mostra missatge de fight! i numero d'enemics restants
        Quan el fightOn és false el client mostra missatge de waiting next round
        Quan el client rep enemics els afegeix al joc.

    public boolean isFightOn() {
        return fightOn;
    }

    public void setFightOn(boolean fightOn) {
        this.fightOn = fightOn;
    }*/
}
