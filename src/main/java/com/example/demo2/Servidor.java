package com.example.demo2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class Servidor {


    int maxPlayers = 5;
    private final Joc estatJoc;
    private List<Player> players;
    int port;
    // un map per tenir els players en una llista numerada
    private List<ServidorThread> playersConectats;
    private List<Thread> clients;
    private int numplayersConectats;
    private int ids=0;

    public Servidor(int port) {
        this.port = port;
        playersConectats = new ArrayList<>();
        clients = new ArrayList<>();

        // inicio les variables de l'estat del joc
        players= new ArrayList<>();
        numplayersConectats=0;
        estatJoc = new Joc(players);
    }

    public void listen() {

        try {
            // Aquest trocet em diu la ip global adamés de comprobar si tenim conexió a internet. Si troba la ip la mostra. si no te conexió mostra missatge i adamés mostrem link de check ip de amazon
            URL whatismyip = null;
            try {
                whatismyip = new URL("http://checkip.amazonaws.com");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String ip=whatismyip.toString();

            boolean connexionLost=false;
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));
                ip = in.readLine(); //you get the IP as a String
            } catch (IOException e) {
                connexionLost=true;
            }


            System.out.println("*********************************************************");
            System.out.println("*                    SERVIDOR                           *");
            System.out.println("*********************************************************");

            System.out.println("       Nom servidor: "+ InetAddress.getLocalHost().getHostName());
            System.out.println("       IP servidor: "+InetAddress.getLocalHost().getHostAddress());
            System.out.println("       IP externa: "+ip);
            if (connexionLost) {
                System.out.println("*                                                       *");
                System.out.println("*            NO TENS INTERNET - ONLY LOCAL              *");
            }else{
                System.out.println("*                                                       *");
                System.out.println("*               - CONECTAT A INTERNET -                 *");
            }
            System.out.println("*                                                       *");

            System.out.println("*  i = msgFromClient   o = msgToClient   > = StatusMsg  *");
            System.out.println("*********************************************************");
            System.out.println();
            System.out.print("> --Preparant   o==|");
            for (int i = 0; i < 10; i++) {
                System.out.print("=");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                   // no faig res, saltarà i continuarà la conexió
                }
            }
            System.out.println(">        Gooooo!");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                if (numplayersConectats == maxPlayers-1){
                    // TODO he de restar els clients que caiguin per a que torni a escoltar i modificar el joc tmb
                }else{

                    //esperar connexió del client i llançar thread  // si hi ha 2 clients deixa d'esperar conexions
                    // TODO He de mantenir aqest bucle. Quan caigui un client s'ha de reiniciar per a que torni a escoltar.
                    System.out.println("> Estic escoltant peticions ...");
                    System.out.println(">");

                    clientSocket = serverSocket.accept();
                    //Llançar Thread per establir la comunicació

                    playersConectats.add(new ServidorThread(clientSocket, estatJoc,ids));
                    clients.add(new Thread(playersConectats.get(numplayersConectats)));
                    clients.get(numplayersConectats).start();
                    numplayersConectats++;
                    ids++;

                }

            }



        } catch (IOException ex) {
            ex.printStackTrace();

        }
        try {
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public static void main(String[] args) {

        Servidor srv = new Servidor(5555);
        srv.listen();

    }
}
