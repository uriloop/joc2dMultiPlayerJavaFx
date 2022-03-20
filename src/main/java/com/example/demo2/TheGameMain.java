package com.example.demo2;

import com.example.demo2.conexio.Client;
import com.example.demo2.model.Bala;
import com.example.demo2.model.Enemic;
import com.example.demo2.model.Player;
import com.example.demo2.view.ViewManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TheGameMain extends Application {

    private double cicles = 0;

    private Client client;


    private float viewPortX = 1200;
    private float viewPortY = 800;

    private final static String CASTTLE_IMAGE = "/resources/deep_blue.png";


    private Stage mainStage;
    private Scene mainScene;
    private String ip;
    private Pane root = new Pane();
    private int margeJugadors = 25;
    private final Sprite player1 = new Sprite("player", Color.DARKOLIVEGREEN, (int) (viewPortX / 2), (int) (viewPortY - 50), 80, 150, Player.Direccio.S, 25);
    private final Sprite castell = new Sprite("castell", Color.BLUE, (int) (viewPortX / 2) - 125, (int) (viewPortY - 150), 250, 180, Player.Direccio.S, 25);
    private List<Sprite> players = new ArrayList<>();
    private List<Sprite> enemics = new ArrayList<>();
    private List<String> input = new ArrayList<>();
    private int id;
    private long idBales = 10;
    private int numSpriteImage = 0;
    private double ciclesSpritesEnemics;
    private int vidaCastell;
    private String rondaLabelID;
    private Sprite rondaSprite = new Sprite("castell", Color.BLUE, (int) (viewPortX / 2) - 125, 150, 250, 180, Player.Direccio.S, 25);


    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public void setRoot(Pane root) {
        this.root = root;
    }

    public Pane getRoot() {
        return root;
    }

    private AudioClip acHit;
    private AudioClip acDead;

    private void createBackground() {
        Image backgroundImage = new Image("background_grass.png", 1200, 800, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        root.setBackground(new Background(background));
    }

    public Parent createContent() {
        root.setPrefSize(viewPortX, viewPortY);
        createBackground();
        String path = getClass().getResource("/small_hit_sound.wav").toString();
        acHit = new AudioClip(path);
        path = getClass().getResource("/enemy_dead_sound.wav").toString();
        acDead = new AudioClip(path);
        root.getChildren().add(player1);
        root.getChildren().add(castell);
        music();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();

            }
        };
        timer.start();

        return root;
    }

    double ciclesMov = cicles;


    private void update() {


        updateEstatJoc();

        if (!player1.isDead()) {
            inputs();
            actualitzaPlayer();
        }

        // Per a que les bales avancin

        sprites().stream()
                .filter(s -> s.getType().equals("atac"))
                .filter(s -> s.getDireccio() == Player.Direccio.S)
                .forEach(Sprite::moveDown);
        sprites().stream()
                .filter(s -> s.getType().equals("atac"))
                .filter(s -> s.getDireccio() == Player.Direccio.N)
                .forEach(Sprite::moveUp);
        sprites().stream()
                .filter(s -> s.getType().equals("atac"))
                .filter(s -> s.getDireccio() == Player.Direccio.W)
                .forEach(Sprite::moveLeft);
        sprites().stream()
                .filter(s -> s.getType().equals("atac"))
                .filter(s -> s.getDireccio() == Player.Direccio.E)
                .forEach(Sprite::moveRight);


        // PLAYERS I DEMÉS SPRITES A GESTIONAR

        // per que morin els players amb les bales menys cada player amb les seves bales           el player principal el gestionem a part
        // primer cal veure si estan morts ja des del servidor
        sprites().stream()
                .filter(s -> s.getType().equals("players"))
                .forEach(player -> {
                    for (int i = 0; i < client.getJoc().getPlayers().size(); i++) {
                        if (player.getIdSprite() == client.getJoc().getPlayers().get(i).getId()) {
                            if (client.getJoc().getPlayers().get(i).isMort()) {
                                player.setDead(true);
                            }
                        }
                    }
                });

        // després els posem a morts els que tinguin una colisió
        sprites().stream()
                .filter(sprite -> sprite.getType().equals("enemic"))
                .forEach(enemic -> {
                    for (Sprite players :
                            sprites()) {
                        if (players.getType().equals("player")) {
                            if (enemic.getBoundsInParent().intersects(players.getBoundsInParent())) {
                                if (enemic.getIdSprite() % 10 != id) {
                                    players.restaVida();
                                    enemic.setDead(true);
                                }

                            }
                        }
                    }
                });

        sprites().stream()
                .filter(sprite -> sprite.getType().equals("atac"))
                .forEach(atac -> {
                    for (Sprite sprite :
                            sprites()) {
                        if (sprite.getType().equals("enemic")) {
                            if (atac.getBoundsInParent().intersects(sprite.getBoundsInParent())) {
                               /* if (atac.getIdSprite() % 10 == player1.getIdSprite()) {
                                    client.getJoc().getPlayers().forEach(p -> {
                                        if (p.getId() == this.id) {
                                            p.sumaKill();
                                                                                                Arreglar. no he d'actuar sobre el client
                                        }
                                    });
                                }*/

                                root.getChildren().add(sprite.setDead(true));
                                atac.setDead(true);
                                acDead.play();

                            }
                        }
                    }
                });


        // actualitzo els estats dead dels enemics

        sprites().stream().filter(s -> s.getType().equals("enemic"))
                .forEach(s -> {

                    for (Enemic e :
                            client.getJoc().getEnemics()) {
                        if (e.getId() == s.getIdSprite()) {
                            if (s.isDead()) {
                                e.setViu(false);
                            }
                        }
                    }

                });
        sprites().stream().filter(s -> s.getType().equals("atac"))
                .forEach(s -> {

                    for (Bala bala :
                            client.getJoc().getBales()) {
                        if (bala.getIdBala() == s.getIdSprite()) {
                            if (bala.isDead()) {
                                s.setDead(true);
                            }
                        }
                    }

                });


        // eliminar atacs que han sortit del joc per cada costat de la pantalla :  només els poso a DEAD=true;

        sprites().stream()
                .filter(s -> s.getType().equals("atac"))
                .filter(s -> s.getTranslateY() + s.getHeight() < 0 || s.getTranslateY() - s.getHeight() > viewPortY
                        || s.getTranslateX() + s.getWidth() < 0 || s.getTranslateX() - s.getWidth() > viewPortX)
                .forEach(s -> s.setDead(true));

        // Esborrar els Sprites que estan DEAD=true         ESTIC ESBORRANT ELS PLAYERS QUE MOREN PERO NO ELS DONO PER MORTS AL JOC, PER TANT ES TORNEN A CREAR QUAN ARRIBEN DEL SERVIDOR   feina a fer quan aixó funcioni

        sprites().forEach(sprite -> {
            if (sprite.isDead()) {
                root.getChildren().remove(sprite);
            }
        });
        cicles++;
    }

    /**
     * S'encarrega de comprovar si estem en espera o ja arriba una olejada d'enemics (fight) i mostra el text corresponent
     */
    private void updateFight() {

        int w = 80, h = 50;
        float posX = viewPortX / 2, posY = 100;
        sprites().forEach(sprite -> {
            if (sprite.getType().equals("fight")) {
                root.getChildren().remove(sprite);
            }
        });
        Sprite sp = null;
        if (client.getJoc().isFight()) {
            sp = new Sprite("fight", Color.AQUA, w, h, (int) posX, (int) posY, Player.Direccio.S, 0);
            sp.setImatgeActualFight("fight");
        } else {
            sp = new Sprite("fight", Color.AQUA, w, h, (int) posX, (int) posY, Player.Direccio.S, 0);
            sp.setImatgeActualFight("wait");

        }
        root.getChildren().add(sp);
    }

    private void actualitzaPlayer() {

        /*sprites().stream().filter(s-> s.getType().equals("vida")).forEach(s-> {
            root.getChildren().remove(s);
        });*/


        client.getJoc().getPlayers().forEach(p -> {
            if (p.getId() == id) {
                p.setDireccio(player1.getDireccio());
                p.setPosX((int) player1.getTranslateX());
                p.setPosY((int) player1.getTranslateY());
                p.setMort(player1.isDead());

            }
        });

    }

    private void inputs() {
        input.forEach(s -> {

            /////  PROVANT DIAGONALS

            List<String> dir = new ArrayList<>();


            if (s.equals("D") || s.equals("RIGHT")) {
                player1.setDireccio(Player.Direccio.E);
                player1.setImatgeActual(Player.Direccio.E);
                if (player1.getTranslateX() + player1.getWidth() + margeJugadors < viewPortX)
                    dir.add("-Dreta");

            }
            if (s.equals("A") || s.equals("LEFT")) {
                player1.setDireccio(Player.Direccio.W);
                player1.setImatgeActual(Player.Direccio.W);
                if (player1.getTranslateX() - margeJugadors > 0)
                    dir.add("-Esquerre");

            }
            if (s.equals("W") || s.equals("UP")) {
                player1.setDireccio(Player.Direccio.N);
                player1.setImatgeActual(Player.Direccio.N);
                if (player1.getTranslateY() - (float) margeJugadors * 0.8 > 0)
                    dir.add("-Dalt");


            }
            if (s.equals("S") || s.equals("DOWN")) {
                player1.setDireccio(Player.Direccio.S);
                player1.setImatgeActual(Player.Direccio.S);
                if (player1.getTranslateY() + player1.getHeight() + margeJugadors < viewPortY)
                    dir.add("-Baix");

            }


            StringBuilder direccioFinal = new StringBuilder();
            dir.forEach(direccioFinal::append);

            switch (direccioFinal.toString()) {
                case "-Dalt-Baix":
                case "-Baix-Dalt":
                case "-Esquerre-Dreta":
                case "Dreta-Esquerre":
                    break;
                case "-Dalt-Dreta":
                case "-Dreta-Dalt":
                    player1.moveRightUp();
                    break;
                case "-Dalt-Esquerre":
                case "-Esquerre-Dalt":
                    player1.moveLeftUp();
                    break;
                case "-Baix-Esquerre":
                case "-Esquerre-Baix":
                    player1.moveLeftDown();
                    break;
                case "-Baix-Dreta":
                case "-Dreta-Baix":
                    player1.moveRightDown();
                    break;
                case "-Baix":
                    player1.moveDown();
                    break;
                case "-Dalt":
                    player1.moveUp();
                    break;
                case "-Esquerre":
                    player1.moveLeft();
                    break;
                case "-Dreta":
                    player1.moveRight();
                    break;
            }

            dir = new ArrayList<>();
            if (s.equals("COMMA")) {
                if (cicles - ciclesDispar > 150) {
                    ciclesDispar = cicles;
                    Sprite sp = player1.atacar(player1, idBales);
                    client.getJoc().getBales().add(new Bala(idBales, (float) sp.getTranslateX(), (float) sp.getTranslateY(), sp.getDireccio()));
                    root.getChildren().add(sp);
                    idBales += 10;
                    acHit.play();
                }

            }


        });


        ciclesMov = cicles;
        input = new ArrayList<>();
    }

    private void updateEstatJoc() {

        // mirar el joc i per cada player del servidor, és a dir tots menys el meu player

        if (client == null) {
            //  client = new Client("localhost", 5555, this);
            client = new Client(ip, 5555, this);

            client.start();
        }
        // akí posem el ready.

        if (client.isReady()) {

            this.id = client.getIdPlayer();
            if (idBales % 10 != id) idBales += id;


            // Aixó m'esborra l'sprite dels altres players per evitar l'estela
            sprites().forEach(sprite -> {
                if (sprite.getType().equals("players")) {
                    root.getChildren().remove(sprite);
                }
            });


            // mostrem els  altres players
            players = new ArrayList<>();
            client.getJoc().getPlayers().stream()
                    .filter(player -> !player.isMort())           // filtrem que no estigui mort i el tornem a crear com sprite
                    .forEach(p -> {
                        if (p.getId() != id) {
                            // actualitzem tots els players menys el nostre   // de moment els tornem a crear
                            Sprite sp = new Sprite(p.getId(), "players", Color.DARKGREEN, (int) p.getPosX(), (int) p.getPosY(), 80, 150, p.getDireccio(), 25);
                            players.add(sp);
                            sp.setImatgeActual(p.getDireccio());

                        }
                    });
            players.forEach(e -> root.getChildren().add(e));


            // actualitzem les bales que arriben del servidor
            int atacW = 16;
            int atacH = 8;
            client.getBalesAcrear().stream().filter(b -> {
                        return !b.isDead();
                    })
                    .forEach(bala -> {
                        Sprite sp = new Sprite(bala.getIdBala(), "atac", Color.RED, (int) (bala.getPosX()),
                                (int) (bala.getPosY()),
                                bala.getDir() == Player.Direccio.S || bala.getDir() == Player.Direccio.N ? atacH : atacW, bala.getDir() == Player.Direccio.S || bala.getDir() == Player.Direccio.N ? atacW : atacH, bala.getDir());
                        root.getChildren().add(sp);

                    });

            if (this.vidaCastell > client.getJoc().getVidaCastell()) {
                client.getJoc().setVidaCastell(this.vidaCastell);
                updateCastell();

            } else if (this.vidaCastell < client.getJoc().getVidaCastell()) {
                this.vidaCastell = client.getJoc().getVidaCastell();
                updateCastell();

            }

            client.balesAcrear = new ArrayList<>();


            // actualitzo l'estat del player al joc per a que ho vegi el servidor.

            for (Player player : client.getJoc().getPlayers()) {
                if (player.getId() == id) {

                    player.setPosX((int) player1.getTranslateX());
                    player.setPosY((int) player1.getTranslateY());
                    player.setDireccio(player1.getDireccio());
                    if (player1.isDead()) player.setMort(true);

                }
            }


            updateFight();

            updateRonda();

            // esborro els enemics primer
            sprites().forEach(sprite -> {
                if (sprite.getType().equals("enemic")) {
                    root.getChildren().remove(sprite);
                }
            });


            // mostrem els enemics
            enemics = new ArrayList<>();

            client.getJoc().getEnemics()          // filtrem que no estigui mort i el tornem a crear com sprite
                    .forEach(e -> {
                        if (e.isViu()){

                            Sprite sp = new Sprite(e.getId(), "enemic", Color.RED, (int) e.getPosX(), (int) e.getPosY(), 64, 64, Player.Direccio.S, 2);
                            enemics.add(sp);
                            sp.setImatgeActual(numSpriteImage, e.getTipus());
                        }

                    });

            enemics.forEach(e -> root.getChildren().add(e));

            // moc els sprites dels enemics cada X cicles

            if (cicles - ciclesSpritesEnemics > 30) {
                numSpriteImage = numSpriteImage == 1 ? 0 : 1;
                ciclesSpritesEnemics = cicles;
            }


            // actualitzo l'estat dels enemics per a que ho vegi el servidor

            enemics.forEach(e -> {
                for (Enemic en :
                        client.getJoc().getEnemics()) {
                    if (e.getIdSprite() == en.getId()) {
                        if (e.isDead() || !en.isViu()) {
                            e.setDead(true);
                            en.setViu(false);
                        }


                    }
                }
            });

            sprites().stream().filter(s -> s.getType().equals("vida")).forEach(s -> {
                root.getChildren().remove(s);
            });

            Sprite[] vides = player1.showVida();

            for (int i = 0; i < vides.length; i++) {
                root.getChildren().add(vides[i]);
            }


        }
    }

    private void updateRonda() {
        rondaSprite.setLabel(client.getJoc().getRonda());
    }

    private void updateCastell() {


    /*    sprites().forEach(sprite -> {
            if (sprite.getType().equals("castell")) {
                root.getChildren().remove(sprite);
            }
        });*/

        castell.setImatgeCastell(client.getJoc().getVidaCastell());


    }


    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n -> (Sprite) n).collect(Collectors.toList());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    double ciclesDispar;

    public Sprite getPlayer1() {
        return player1;
    }

    @Override
    public void start(Stage stage) throws Exception {

        ViewManager viewManager = new ViewManager(this);
        stage = viewManager.getMainStage();


        // posem a escoltar diferents tecles per als inputs

        viewManager.getMainScene().setOnKeyPressed(e -> {
            input.add(e.getCode().toString());
        });
        stage.show();


    }

    MediaPlayer mediaPlayer;

    private void music() {
        String path = getClass().getResource("/background_music.mp3").toString();
        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

    }

    public static void main(String[] args) {


        launch();

    }


}
