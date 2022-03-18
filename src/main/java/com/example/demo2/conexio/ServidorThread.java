package com.example.demo2.conexio;

import com.example.demo2.utils.JsonClass;
import com.example.demo2.utils.LogPartida;
import com.example.demo2.utils.TimerRondes;
import com.example.demo2.model.Bala;
import com.example.demo2.model.Enemic;
import com.example.demo2.model.Joc;
import com.example.demo2.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServidorThread extends Thread {

// com comunico el serverThread amb el server per compartir les dades dels players???????


    private LogPartida log = new LogPartida();
    private JsonClass json;
    private Socket clientSocket = null;
    private BufferedReader in = null;
    private PrintStream out = null;
    private String msgEntrant, msgSortint;
    boolean acabat;
    private Joc estatJoc;
    private int idPropia;
    private long idsEnemics = 0;
    private String nick;
    private TimerRondes tempRondes = new TimerRondes(5000);
    private List<Enemic> enemicsDeLaRonda;
    private final int numEnemicsEnArena = 3;

    public ServidorThread(Socket clientSocket, Joc estatJoc, int idPropia) {
        this.idPropia = idPropia;
        this.estatJoc = estatJoc;
        this.clientSocket = clientSocket;
        acabat = false;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        json = new JsonClass();

    }

    @Override
    public void run() {
        log.add("i. jug_" + (idPropia) + " Conexió establerta");
        System.out.println("i. jug_" + (idPropia) + " Conexió establerta");
        tempRondes.startEspera();
        try {
            // primer missatge on li passem el num de player per determinar la posició inicial i el color del usuari
            estatJoc.getPlayers().add(new Player(idPropia, 100f, 100f, Player.Direccio.S));
            msgSortint = String.valueOf(idPropia);
            out.println(msgSortint);
            out.flush();
            log.add("o. jug_" + (idPropia) + ": " + msgSortint);
            //rebem la confirmació del client i
            // enviem el primer json amb tots els players, l'usuari ja sap la seva id així que ja podrà discernir que ha de actualitzar i que no.
            msgEntrant = in.readLine();
            log.add("i. jug_" + (idPropia) + ": " + msgEntrant);

            if (msgEntrant.equals("Conectat!")) {
                // akí podriem rebre el nick del jugador per a que tots veiessin el nom dels altres ara tiro milles
                // TODO posar nick al client per a que el servidor tingui el nom dels clients i poder-los mostrar en un futur
            }

            msgSortint = json.getJSON(estatJoc);
            out.println(msgSortint);
            out.flush();


            // Akí hem de rebre el primer json del client

            msgEntrant = in.readLine();
            // akí comença la festa dels json
            while (!acabat) {


                msgSortint = generarResposta(msgEntrant);
                out.println(msgSortint);
                out.flush();
                msgEntrant = in.readLine();
                // bucle de missatges
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Salta akí ");
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualitzarEnemics(Joc jocRebut) {

        // busco els enemics morts per a posarlos a mort
        jocRebut.getEnemics().stream().filter(en -> !en.isViu())
                .forEach(enemic -> {
                    for (Enemic e :
                            estatJoc.getEnemics()) {
                        if (enemic.getId() == e.getId()) {
                            e.setViu(false);
                        }
                    }
                });



        // Actualitzo moviment
        estatJoc.getEnemics().forEach(enemic -> {

            // he de crear una diagonal fins l'objecte que volen atacar. bottom-center
            // un cop arriben ja no es mouen

            // valors que se:   xActual yActual  xFinal yFinal  distanciaDeCadaMoviment  // angleQueHaDeSeguir
            // valors que vull:  posXdespresDelMoviment   posYdespresDelMoviment
            // Math.atan2(by-ay, bx-ax);     A: enemic    B: objectiu    (tinc el dubte de si tindrà negatiu i positiu... tenir-ho en compte si dona POSSIBLE ERROR BUSCA AKI)


            double angle = Math.atan2(700 - enemic.getPosY(), 600 - enemic.getPosX());

            // gracies! https://www.profesorenlinea.cl/fisica/Fuerzas_descomposicion.html i  https://codigo--java.blogspot.com/2013/06/java-basico-046-funcion-calculando-seno.html
            // Fx = F• cos α  ,  Fy = F• sen α  o en java    movX = velMoviment * cos angle;

            // desglosso el que avança en cada eix de coordenades
            float movX = (float) (enemic.getVelMoviment() * Math.cos(angle));
            float movY = (float) (enemic.getVelMoviment() * Math.sin(angle));

            // actualitzo la posició de l'enemic   FI
            enemic.setPosX(enemic.getPosX() + movX);
            enemic.setPosY(enemic.getPosY() + movY);



        });


    }

    private void spawnEnemics() {

        /*for (int i = estatJoc.getEnemics().size()-1; i > 0 ; i--) {
            if (!estatJoc.getEnemics().get(i).isViu()) estatJoc.getEnemics().remove(i);
        }*/
// esborro enemics
        List<Integer> posAesborrar= new ArrayList<>();
        for (int i = 0; i < estatJoc.getEnemics().size(); i++) {
            if (!estatJoc.getEnemics().get(i).isViu()) posAesborrar.add(i);
        }

        for (int i =  posAesborrar.size()-1; i>=0; i--) {
            estatJoc.getEnemics().remove((int) posAesborrar.get(i));
        }
        if (enemicsDeLaRonda != null) {

            // que spawneji de tal forma que sempre hi hagi X enemics minims en Arena o en joc.
            while (estatJoc.getEnemics().size() < numEnemicsEnArena && enemicsDeLaRonda.size() > 0) {
                estatJoc.getEnemics().add(enemicsDeLaRonda.get(enemicsDeLaRonda.size() - 1));
                enemicsDeLaRonda.remove(enemicsDeLaRonda.size() - 1);
            }
        }

    }

    int ronda=0;
    private void generaRondes() {

        if (tempRondes.isOn()) {
            if (tempRondes.haAcabatLespera()) {       /* ho poso en dos ifs ja que el segon mètode modifica el resultat del primer metode i vull limitar conflictes */
                enemicsDeLaRonda = (generaUnaLlistaDEnemics());
            }
        }



        if (enemicsDeLaRonda!=null  && !tempRondes.isOn()){

            if (enemicsDeLaRonda.size() == 0 && estatJoc.getEnemics().size() < 2 && ronda==0) {

                estatJoc.getEnemics().removeAll(estatJoc.getEnemics());
                tempRondes.startEspera();
                ronda++;
            }
            if (enemicsDeLaRonda.size() == 0 && estatJoc.getEnemics().size() < 1 && ronda>0) {

                estatJoc.getEnemics().removeAll(estatJoc.getEnemics());
                tempRondes.startEspera();

            }
        }
    }

    private List<Enemic> generaUnaLlistaDEnemics() {

        int randomTipus;
        int numEnemicsRandom = (int) (Math.random() * 10) + 5;
        List<Enemic> enemicsRandom = new ArrayList<>();
        for (int i = 0; i < numEnemicsRandom; i++) {
            randomTipus = (int) (Math.random() * 3);
            Enemic e = new Enemic(randomTipus == 0 ? Enemic.Tipus.PUMPKIN : randomTipus == 1 ? Enemic.Tipus.FLOATING : Enemic.Tipus.BOSS, idsEnemics++);
            enemicsRandom.add(e);
        }
        return enemicsRandom;

    }


    // juntar tots els msgEntrants dels diferents jugadors, posar en comu i retornar el json amb les posicions
    private String generarResposta(String msgEntrant) {

        // mapejo a un objecte provisional d'on agafar les dades que realment m'interessa actualitzar
        JsonClass json = new JsonClass();
        Joc jocRebut = json.getObject(msgEntrant);

        // actualitzem el joc:      player   i   bales

        actualitzaPlayer(jocRebut);
        actualitzaBales(jocRebut);
        // els enemics els he d'actualitzar desde el servidor també...? Si vull implementar-lis moviments extranys si. si simplement tots van cap a un punt no caldria.  (com la feina és la mateixa exactament, ho implemento akí, que em dona més joc en un futur.)
        generaRondes();
        spawnEnemics();
        actualitzarEnemics(jocRebut);
        // gestionar i crear olejades d'enemics que surtin en moments i llocs diversos
        // spawnejar enemics de forma escalonada a la llista d'enemics del joc,  els va esborrant de la llista del thread.    tot plegat no tinc clar que ho hagi de fer el servidor thread. seria un Thread extern? una classe dedicada a aixó...

        // creem la resposta amb l'objecte joc que hem modificat i que es va modificant constantment amb el que envien la resta de players
        String resposta = json.getJSON(estatJoc);


        // per monitoritzar el que passa al servidor
        log.add("i. jug_" + (idPropia + 1) + ": " + msgEntrant);
        log.add("o. jug_" + (idPropia + 1) + ": " + resposta);
        /*System.out.println("i. jug_" + (idPropia + 1) + ": " + msgEntrant);
        System.out.println("o. jug_" + (idPropia + 1) + ": " + resposta);
*/
        return resposta;    // retornem el json de l'estat del joc
    }


    private void actualitzaBales(Joc jocRebut) {

        boolean existeix = false;
        // Per cada bala rebuda
        for (Bala bReb : jocRebut.getBales()) {
            // per cada bala que existeix actualment
            for (Bala bAct :
                    estatJoc.getBales()) {
                // comprovem si existeix per determinar si s'ha de crear o no
                if (bAct.getIdBala() == bReb.getIdBala()) {
                    existeix = true;
                }
            }
            // si no existeix la creem, sino l'actualitzem la posició i dir
            if (!existeix) estatJoc.getBales().add(bReb);
            /*else {
                estatJoc.getBales().forEach(b -> {
                    // busquem la bala corresponent i l'actualitzem
                    if (b.getIdBala() == bReb.getIdBala()) {
                        b.setDir(bReb.getDir());
                        b.setPosX(bReb.getPosX());
                        b.setPosY(bReb.getPosY());

                    }
                });
            }*/
            existeix = false;
        }

    }

    private void actualitzaPlayer(Joc jocRebut) {

        estatJoc.getPlayers().forEach(player -> {
            if (player.getId() == idPropia) {
                for (Player p :
                        jocRebut.getPlayers()) {
                    if (p.getId() == idPropia) {
                        player.setPosX(p.getPosX());
                        player.setPosY(p.getPosY());
                        player.setDireccio(p.getDireccio());
                        player.setMort(p.isMort() || player.isMort());       // si esta mort en qualsevol lloc el mantenim a mort o cambiem a mort segons toqui
                    }
                }

            }
        });

    }
}
