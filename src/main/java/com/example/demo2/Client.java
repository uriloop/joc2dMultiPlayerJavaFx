package com.example.demo2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {

    private String hostname;
    int port;
    private JsonClass json;
    boolean continueConnected;

    private Joc joc;
    private Joc coj;


    private int idPlayer;
    private boolean ready;
    TheGameMain gameMain;
    List<Bala> balesAcrear = new ArrayList<>();

    LogPartida log;


    public Client(String hostname, int port, TheGameMain gameMain) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
        List<Player> playersJoc = new ArrayList<>();
        List<Player> playersCoj = new ArrayList<>();

        this.gameMain = gameMain;
        joc = new Joc(playersJoc);
        coj = new Joc(playersCoj);

        json = new JsonClass();
    }


    public void run() {

        String serverData;
        String request;


        Socket socket;
        BufferedReader in;
        PrintStream out;
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            // Tractem el primer missatge on rebem la id
            serverData = in.readLine();
            this.idPlayer = Integer.parseInt(serverData);
            log = new LogPartida("info_partida_client", idPlayer);
            log.add("i. " + serverData);
            // ~enviem el nick~  enviem resposta simple de moment
            // TODO enviar el nick que hauriem d'haver demanat
            request = "Conectat!";
            ready = true;

            out.println(request);
            out.flush();
            log.add("o. " + request);

            // revem segon missatge amb el json ja. toca iniciar tot:
            // - posar el jugador propi al seu lloc nou
            serverData = in.readLine();
            log.add("i. " + serverData);

            joc = json.getObject(serverData);
            ready = true;
            request = json.getJSON(joc);
            // comença la festa dels Json
            out.println(request);
            out.flush();
            log.add("o. " + request);
            while (continueConnected) {
                serverData = in.readLine();

                //processament de les dades rebudes i obtenció d'una nova petició
                request = getRequest(serverData);
                //enviament el número i els intents
                out.println(request);
                out.flush();
                esborrarEnemicsMorts();
            }
            close(socket);
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix l'host: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        }

    }

    private void esborrarEnemicsMorts() {

        List<Integer> posicionsEnemicsAEliminar = new ArrayList<>();
        for (int i = 0; i < joc.getEnemics().size(); i++) {
            if (!joc.getEnemics().get(i).isViu()) posicionsEnemicsAEliminar.add(i);
        }
        for (int i = 0; i < posicionsEnemicsAEliminar.size(); i++) {
            joc.getEnemics().remove((int) posicionsEnemicsAEliminar.get(i));
        }

    }


    // Tractem la rebuda de dades
    public String getRequest(String recivedDataFromServer) {


        // ens pillem tota la info.            després només actualitzarem als altres players en el joc. pero abans de retornar el json hem d'actualitzar el player

        Joc jocRebut = json.getObject(recivedDataFromServer);

        // actualitzo players
        actualitzaPlayers(jocRebut);

        // comprobar les bales.
        actualitzaBales(jocRebut);

        // actualitzem els enemics al nostre joc
        actualitzaEnemics(jocRebut);

        String resposta = json.getJSON(joc);

        // monitoritzar el que rebem del servidor
        log.add("i.  " + recivedDataFromServer);
        System.out.println("i.  " + recivedDataFromServer);

        // monitoritzem per veure que tot funciona
        log.add("o. " + resposta);
        System.out.println("o. " + resposta);

        return resposta;  // envio el json amb l'objecte joc


    }

    private void actualitzaEnemics(Joc jocRebut) {



        /*       // un cop hagi retornat que està mort l'enemic cap al servidor l'esborro i m'estalvio aixó

        // busco enemics que han estat eliminats per el servidor
        List<Integer> posicionsEnemicsAEliminar=  new ArrayList<>();
        boolean esta = false;
        for (int i = 0; i < joc.getEnemics().size(); i++) {
            for (Enemic e :
                    jocRebut.getEnemics()) {
                if (e.getId()==joc.getEnemics().get(i).getId()) esta= true;
            }
            if (!esta) posicionsEnemicsAEliminar.add(i);
            esta=false;
        }
        // els elimino del joc
        for (int i = posicionsEnemicsAEliminar.size()-1; i >=0 ; i--) {
            joc.getEnemics().remove(posicionsEnemicsAEliminar.get(i));
        }*/
        List<Integer> nousEnemicsPos = new ArrayList<>();
        boolean esta = false;

        // busco enemics nous que vinguin del servidor
        for (int i = 0; i < jocRebut.getEnemics().size(); i++) {
            for (Enemic e : joc.getEnemics()) {
                if (e.getId() == jocRebut.getEnemics().get(i).getId()) esta = true;
            }
            if (!esta && jocRebut.getEnemics().get(i).isViu()) nousEnemicsPos.add(i);
            esta = false;
        }

        try {

            // els afegeixo al joc
            for (int i = 0; i < nousEnemicsPos.size(); i++) {
                if (jocRebut.getEnemics().get(nousEnemicsPos.get(i)).isViu())
                    joc.getEnemics().add(new Enemic(jocRebut.getEnemics().get(nousEnemicsPos.get(i)).getId(), jocRebut.getEnemics().get(nousEnemicsPos.get(i)).getPosY(), jocRebut.getEnemics().get(nousEnemicsPos.get(i)).getPosX(), jocRebut.getEnemics().get(nousEnemicsPos.get(i)).getTipus()));
            }

        } catch (Exception e) {

        }


        // actualitzo l'estat de la posició i l'estat de viu
        for (Enemic e : joc.getEnemics()) {
            for (Enemic e2 : jocRebut.getEnemics()) {
                if (e.getId() != idPlayer) {
                    if (e.getId() == e2.getId()) {
                        e.setPosX(e2.getPosX());
                        e.setPosY(e2.getPosY());
                        if (!e.isViu() || !e2.isViu()) {
                            e.setViu(false);
                            e2.setViu(false);
                        }
                    }
                }
            }
        }


    }

    private void actualitzaPlayers(Joc jocRebut) {

        List<Integer> nousPlayersPos = new ArrayList<>();
        boolean esta = false;

        // busco players nous que vinguin del servidor
        for (int i = 0; i < jocRebut.getPlayers().size(); i++) {
            for (Player p : joc.getPlayers()) {
                if (p.getId() == jocRebut.getPlayers().get(i).getId()) esta = true;
            }
            if (!esta) nousPlayersPos.add(i);
            esta = false;
        }
        // els afegeixo al joc
        for (int i = 0; i < nousPlayersPos.size(); i++) {
            joc.getPlayers().add(new Player(jocRebut.getPlayers().get(nousPlayersPos.get(i)).getId(), jocRebut.getPlayers().get(nousPlayersPos.get(i)).getPosY(), jocRebut.getPlayers().get(nousPlayersPos.get(i)).getPosX(), jocRebut.getPlayers().get(nousPlayersPos.get(i)).getDireccio()));
        }

        // actualitzo l'estat dels que no son l'usuari
        for (Player p : joc.getPlayers()) {
            for (Player p2 : jocRebut.getPlayers()) {
                if (p.getId() != idPlayer) {
                    if (p.getId() == p2.getId()) {
                        p.setPosX(p2.getPosX());
                        p.setPosY(p2.getPosY());
                        p.setDireccio(p2.getDireccio());
                        if (p.isMort() || p2.isMort()) {
                            p.setMort(true);
                            p2.setMort(true);
                        }
                    }
                }
            }
        }


//        joc.getPlayers().forEach(player -> {
//            if (player.getId() != idPlayer) {
//                for (Player p :
//                        jocRebut.getPlayers()) {
//                    if (p.getId() == player.getId()) {
//                        player.setPosX(p.getPosX());
//                        player.setPosY(p.getPosY());
//                        player.setDireccio(p.getDireccio());
//                        player.setId(p.getId());
//                    }
//                }
//
//            }
//        });


//                  nO ACABA DE TIRAR
//        // comprovar usuaris nous al servidor. Els vells els actualitzem
//        List<Integer> posicioDelsNousUsuaris= new ArrayList<>();
//        boolean esta= false;
//        for (int i = 0; i < jocRebut.getPlayers().size(); i++) {
//            for (Player p :
//                    joc.getPlayers()) {
//                if (p.getId()==jocRebut.getPlayers().get(i).getId()&& p.getId()!=idPlayer){   // busquem el que té la mateixa id per actualitazar-lo. descartem el propi usuari
//                    esta=true;
////                    p=jocRebut.getPlayers().get(i);
//                    p.setDireccio(jocRebut.getPlayers().get(i).getDireccio());
//                    p.setPosX(jocRebut.getPlayers().get(i).getPosX());
//                    p.setPosY(jocRebut.getPlayers().get(i).getPosY());
//                }
//            }
//            if (!esta) posicioDelsNousUsuaris.add(i);
//            esta=false;
//        }
//
//        // ara afegeixo els usuaris nous que m'he guardat les les posicions
//        for (int i = 0; i < posicioDelsNousUsuaris.size(); i++) {
//            joc.getPlayers().add(jocRebut.getPlayers().get(posicioDelsNousUsuaris.get(i)));
//        }


    }

    private void actualitzaBales(Joc jocRebut) {

        boolean existeix = false;
        // Per cada bala rebuda
        for (Bala bReb : jocRebut.getBales()) {
            // per cada bala que existeix actualment
            for (Bala bAct : joc.getBales()) {
                // comprovem si existeix per determinar si s'ha de crear o no
                if (bAct.getIdBala() == bReb.getIdBala()) {
                    existeix = true;
                }
            }
            // si no existeix la creem, sino l'actualitzem la posició i direcció        també la poso a una llista de bales a crear per a crear els sprites un sol cop
            if (!existeix) {
                joc.getBales().add(bReb);
                balesAcrear.add(new Bala(bReb.getIdBala(), bReb.getPosX(), bReb.getPosY(), bReb.getDir()));
            }

            existeix = false;
        }


    }

    public List<Bala> getBalesAcrear() {
        return balesAcrear;
    }

    public void setBalesAcrear(List<Bala> balesAcrear) {
        this.balesAcrear = balesAcrear;
    }

    public Joc getJoc() {
        return joc;
    }

    public void setJoc(Joc joc) {
        this.joc = joc;
    }


    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean mustFinish(String dades) {
        if (dades.equals("exit")) return false;
        return true;

    }

    private void close(Socket socket) {
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if (socket != null && !socket.isClosed()) {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public boolean isReady() {
        return ready;
    }

    public int getIdPlayer() {
        return idPlayer;
    }
}
