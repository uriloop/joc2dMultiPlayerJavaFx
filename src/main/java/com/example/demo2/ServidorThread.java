package com.example.demo2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class ServidorThread extends Thread {

// com comunico el serverThread amb el server per compartir les dades dels players???????
    // Que arranca que???? el servidor va apart, només escolta.
    /*   El servidor arranca el client i el client arranca el game     ?????           */

    private LogPartida log= new LogPartida();
    private JsonClass json;
    private  Socket clientSocket = null;
    private  BufferedReader in = null;
    private PrintStream out = null;
    private String msgEntrant, msgSortint;
    boolean acabat;
    private Joc estatJoc;
    private int idPropia;
    private long idsEnemics=0;
    private String nick;
    private TimerRondes tempRondes=new TimerRondes(20000);
    private List<Enemic> enemicsDeLaRonda;
    private int numEnemicsEnArena=3;

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
            estatJoc.getPlayers().add(new Player(idPropia, 100f, 100f , Player.Direccio.S));
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
                // gestionar i crear olejades d'enemics que surtin en moments i llocs diversos
                generaRondes();
                // spawnejar enemics de forma escalonada a la llista d'enemics del joc,  els va esborrant de la llista del thread.    tot plegat no tinc clar que ho hagi de fer el servidor thread. seria un Thread extern? una classe dedicada a aixó...
                spawnEnemics();
                // els enemics els he d'actualitzar desde el servidor també...? Si vull implementar-lis moviments extranys si. si simplement tots van cap a un punt no caldria.  (com la feina és la mateixa exactament, ho implemento akí, que em dona més joc en un futur.)
                actualitzarEnemics();


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

    private void actualitzarEnemics() {



        estatJoc.getEnemics().forEach(enemic -> {

            // he de crear una diagonal fins l'objecte que volen atacar. bottom-center
            // un cop arriben ja no es mouen

            // valors que se:   xActual yActual  xFinal yFinal  distanciaDeCadaMoviment  // angleQueHaDeSeguir
            // valors que vull:  posXdespresDelMoviment   posYdespresDelMoviment
            // Math.atan2(by-ay, bx-ax);     A: enemic    B: objectiu    (tinc el dubte de si tindrà negatiu i positiu... tenir-ho en compte si dona POSSIBLE ERROR BUSCA AKI)

            double angle=Math.atan2(790-enemic.getPosY(), 600-enemic.getPosX());

            // gracies! https://www.profesorenlinea.cl/fisica/Fuerzas_descomposicion.html i  https://codigo--java.blogspot.com/2013/06/java-basico-046-funcion-calculando-seno.html
            // Fx = F• cos α  ,  Fy = F• sen α  o en java    movX = velMoviment * cos angle;

            // desglosso el que avança en cada eix de coordenades
            float movX= (float) (enemic.getVelMoviment() * Math.cos(angle));
            float movY=(float) (enemic.getVelMoviment() * Math.sin(angle));

            // actualitzo la posició de l'enemic   FI
            enemic.setPosX(enemic.getPosX()+movX);
            enemic.setPosX(enemic.getPosX()+movX);




        });

        //TODO next


    }

    private void spawnEnemics() {


        // una recursivitat per a que spawneji de tal forma que sempre hi hagi X enemics minims en Arena o en joc.
        if (estatJoc.getEnemics().size()<numEnemicsEnArena&& enemicsDeLaRonda.size()>0){
            estatJoc.getEnemics().add(enemicsDeLaRonda.get(enemicsDeLaRonda.size()-1));
            enemicsDeLaRonda.remove(enemicsDeLaRonda.size()-1);
            spawnEnemics();
        }


    }

    private void generaRondes() {
        if ( tempRondes.isOn()){
            if (tempRondes.haAcabatLespera()){       /* ho poso en dos ifs ja que el segon mètode modifica el resultat del primer metode i vull limitar conflictes */
                enemicsDeLaRonda=(generaUnaLlistaDEnemics());
            }

        }else{
            if (enemicsDeLaRonda.size()==0){
                tempRondes.startEspera();
            }
        }
    }

    private List<Enemic> generaUnaLlistaDEnemics() {

        List<Enemic> enemicsRandom= new ArrayList<>();
        int numEnemicsRandom=(int)(Math.random()*10)+5;
        for (int i = 0; i < numEnemicsRandom; i++) {
            Enemic e= new Enemic(Enemic.Tipus.PUMPKIN,idsEnemics);
            idsEnemics++;
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

        // creem la resposta amb l'objecte joc que hem modificat i que es va modificant constantment amb el que envien la resta de players
        String resposta = json.getJSON(estatJoc);


        // per monitoritzar el que passa al servidor
        log.add("i. jug_" + (idPropia + 1) + ": " + msgEntrant);
        log.add("o. jug_" + (idPropia + 1) + ": " + resposta);

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
