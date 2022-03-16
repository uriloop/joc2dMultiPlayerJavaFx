package com.example.demo2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TheGameMain extends Application {

    private double cicles = 0;

    ///////////7

    private Client client;

    /////////////777

    private float viewPortX = 1200;
    private float viewPortY = 800;

    private Pane root = new Pane();
    private int margeJugadors = 25;
    private boolean carrega;
    private final Sprite player1 = new Sprite("player", Color.DARKOLIVEGREEN, 100, 100, 60, 90, Player.Direccio.S, 25);
    private List<Sprite> enemics = new ArrayList<>();

    private List<String> input = new ArrayList<>();
    private int id;
    private long idBales = 10;


    private Parent createContent() {
        root.setPrefSize(viewPortX, viewPortY);
        root.getChildren().add(player1);


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
                .filter(s -> s.getType().equals("enemic"))
                .forEach(enemic -> {
                    for (int i = 0; i < client.getJoc().getPlayers().size(); i++) {
                        if (enemic.getIdSprite() == client.getJoc().getPlayers().get(i).getId()) {
                            if (client.getJoc().getPlayers().get(i).isMort()) {
                                enemic.setDead(true);
                            }
                        }
                    }
                });

        // després els posem a morts els que tinguin una colisió
        sprites().stream()
                .filter(sprite -> sprite.getType().equals("atac"))
                .forEach(atac -> {
                    for (Sprite players :
                            sprites()) {
                        if (players.getType().equals("enemic")) {
                            if (atac.getBoundsInParent().intersects(players.getBoundsInParent())) {
                                if (atac.getIdSprite() % 10 != players.getIdSprite())
                                    players.setDead(true);
                            }
                        } else if (players.getType().equals("player")) {
                            if (atac.getBoundsInParent().intersects(players.getBoundsInParent())) {
                                if (atac.getIdSprite() % 10 != id)
                                    players.setDead(true);
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

        // Esborrar els Sprites que estan DEAD=true         ESTIC ESBORRANT ELS ENEMICS QUE MOREN PERO NO ELS DONO PER MORTS AL JOC, PER TANT ES TORNEN A CREAR QUAN ARRIBEN DEL SERVIDOR   feina a fer quan aixó funcioni

        sprites().forEach(sprite -> {
            if (sprite.isDead()) {
                root.getChildren().remove(sprite);
            }
        });
        cicles++;
    }

    private void actualitzaPlayer() {

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
                if (player1.getTranslateX() + player1.getWidth() + margeJugadors < viewPortX)
                    dir.add("-Dreta");

            }
            if (s.equals("A") || s.equals("LEFT")) {
                player1.setDireccio(Player.Direccio.W);
                if (player1.getTranslateX() - margeJugadors > 0)
                    dir.add("-Esquerre");

            }
            if (s.equals("W") || s.equals("UP")) {
                player1.setDireccio(Player.Direccio.N);
                if (player1.getTranslateY() - (float) margeJugadors * 0.8 > 0)
                    dir.add("-Dalt");


            }
            if (s.equals("S") || s.equals("DOWN")) {

                player1.setDireccio(Player.Direccio.S);
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
                }

            }


        });


        ciclesMov = cicles;
        input = new ArrayList<>();
    }

    private void updateEstatJoc() {

        // mirar el joc i per cada enemic, és a dir tots menys el meu player

        if (client == null) {
            client = new Client("localhost", 5555, this);
            client.start();
        }
        // akí posem el ready.

        if (client.isReady()) {
            this.id = client.getIdPlayer();
            if (idBales % 10 != id) idBales += id;
            // Aixó m'esborra l'sprite dels altres players per evitar l'estela
            sprites().forEach(sprite -> {
                if (sprite.getType().equals("enemic")) {
                    root.getChildren().remove(sprite);
                }
            });

            // Com l'anterior pero per les bales     LES BALES ES CREEN UN COP, NO S'ACTUALITZEN, NO VAL CREC
//            sprites().forEach(sprite -> {
//                if (sprite.getType().equals("atac")){
//                    root.getChildren().remove(sprite);
//                }
//            });


            // mostrem els enemics o altres players
            enemics = new ArrayList<>();
            client.getJoc().getPlayers().stream()
                    .filter(player -> !player.isMort())           // filtrem que no estigui mort i el tornem a crear com sprite
                    .forEach(p -> {
                        if (p.getId() != id) {
                            // actualitzem tots els players menys el nostre   // de moment els tornem a crear
                            enemics.add(new Sprite(p.getId(), "enemic", Color.DARKGREEN, (int) p.getPosX(), (int) p.getPosY(), 60, 90, p.getDireccio(), 25));
                        }
                    });
            enemics.forEach(e -> root.getChildren().add(e));


            // actualitzem les bales que arriben del servidor
            int atacW = 16;
            int atacH = 8;
            client.getBalesAcrear().forEach(bala -> {
                Sprite sp = new Sprite(bala.getIdBala(), "atac", Color.RED, (int) (bala.getPosX()),
                        (int) (bala.getPosY()),
                        bala.getDir()== Player.Direccio.S||bala.getDir()== Player.Direccio.N ? atacH: atacW, bala.getDir()== Player.Direccio.S||bala.getDir()== Player.Direccio.N ? atacW: atacH, bala.getDir());
                root.getChildren().add(sp);

            });

            // Aixó és una llicencia ràpida que em permeto pero hi ha una clara falla d'encapsulament. les bales a crear hauria de pertanyer a TheGameMain més aviat i que client hi poogués fer modificacions...
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


        }

    }


    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n -> (Sprite) n).collect(Collectors.toList());
    }


    double ciclesDispar;

    public Sprite getPlayer1() {
        return player1;
    }

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(createContent());

        // posem a escoltar diferents tecles per als inputs

        scene.setOnKeyPressed(e -> {
            input.add(e.getCode().toString());
        });


        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {


        launch();

    }


}
