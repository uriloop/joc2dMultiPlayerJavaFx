package com.example.demo2.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe que recull dades del joc i les enmagatzema per poder consultar i veure si el joc fa el que ha de fer
 */
public class LogPartida {

   private File f;

   private  BufferedWriter bw;

    /**
     * Constructor de la classe
     */
    public LogPartida() {

        f= new File("src/main/resources/logs_partides/info_partida_servidor.txt");
        f.delete();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw= new BufferedWriter(new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Constructor secondari per als clients
     * @param info_partida_client
     * @param idPlayer
     */
    public LogPartida(String info_partida_client, int idPlayer) {

        f= new File("src/main/resources/logs_partides/"+info_partida_client+idPlayer+".txt");
        f.delete();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw= new BufferedWriter(new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** Afegeix a l'arxiu
     * @param s
     */
    public void add(String s) {


        try {
            bw.append(s);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
