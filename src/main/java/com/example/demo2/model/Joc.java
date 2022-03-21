package com.example.demo2.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe model que guarda l'estat del joc en els diferents clients i el servidor
 */
public class Joc implements Serializable {

    private List<Player> players = new ArrayList<>();

    private List<Enemic> enemics= new ArrayList<>();

    private List<Bala> bales = new ArrayList<>();

    private boolean fight=false;
    private long ronda=0;
    private int vidaCastell=100;


    // private boolean fightOn;

    /**
     * Constructor buit per al jackson
     */
    public Joc() {
    }


    /** Constructor secondari
     * @param players
     */
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

    public boolean isFight() {
        return fight;
    }

    public void setFight(boolean fight) {
        this.fight = fight;
    }

    /**
     * Suma una ronda al joc
     */
    public void sumaRonda() {
        this.ronda++;
    }

    public int getVidaCastell() {
        return this.vidaCastell;
    }

    public void setVidaCastell(int vidaCastell) {
        this.vidaCastell=vidaCastell;
    }

    public long getRonda() {
        return ronda;
    }

    public void setRonda(long ronda) {
        this.ronda = ronda;
    }
}
