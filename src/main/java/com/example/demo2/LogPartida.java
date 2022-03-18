package com.example.demo2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogPartida {

   private File f;

   private  BufferedWriter bw;

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

    public void add(String s) {


        try {
            bw.append(s);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
