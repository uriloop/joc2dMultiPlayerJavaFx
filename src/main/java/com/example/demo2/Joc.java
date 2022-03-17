package com.example.demo2;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Joc implements Serializable {

    private List<Player> players = new ArrayList<>();

    private List<Enemic> enemics= new ArrayList<>();

    private List<Bala> bales = new ArrayList<>();

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
}
