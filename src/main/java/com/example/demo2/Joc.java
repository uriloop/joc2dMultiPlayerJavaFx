package com.example.demo2;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Joc implements Serializable {


    public Joc() {
    }

    private List<Player> players = new ArrayList<>();


    public Joc(List<Player> players) {
        this.players = players;
    }
    public List<Player> getPlayers() {
        return players;
    }

    /*public void actualitzaClient(int idClient, String recivedDataFromServer) {


        Joc estatJocRebut = new JsonClass().getObject(recivedDataFromServer);

        try{


        }catch (Exception e) {
            System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEeiiiiii "+e);
        }




    }*/
    public void actualitzaServidor(int idDeQuiEnvia, String json) {

        Joc estatJocRebut;

        // Aixó està malament, a la que es desconecti un jugador fallarà, xq la id != posició    //  de moment tiro milles com amb tot ...
        // TODO solucionar el tema id i posició que afecta a més llocs.
        try{
            estatJocRebut = new JsonClass().getObject(json);
            this.players.get(idDeQuiEnvia).setPosX(estatJocRebut.players.get(idDeQuiEnvia).getPosX());
            this.players.get(idDeQuiEnvia).setPosY(estatJocRebut.players.get(idDeQuiEnvia).getPosY());
            this.players.get(idDeQuiEnvia).setDireccio(estatJocRebut.players.get(idDeQuiEnvia).getDireccio());
        }catch(Exception e){

        }

        // akí tria de dades entre el estatjocRebut i this tenint en compte qui ha enviat en quant al player

        // actualitzem les dades del jugador que ens ha enviat el json.
        // comprovem la llista de bales en busca de bales noves

        // he de fer un stream o varios:   quedar-me amb les noves i generar-les al joc...   afegir-les a la llista propia de bales
    }
}
