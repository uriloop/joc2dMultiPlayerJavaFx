package com.example.demo2;

import com.example.demo2.model.Enemic;
import com.example.demo2.model.Player;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Engloba tots els objectes que apareixen en el joc i carrega la imatge corresponent a cada tipus
 */
public class Sprite extends Rectangle {


    private int velMoviment = 2;
    int altura = 20;
    private boolean dead = false;
    private String type = null;
    private Player.Direccio direccio = Player.Direccio.S;
    private long id;
    private String imatgeActual;
    private int vida = 6;

    /**  Aquest constructor s'utilitza amb les bales
     * @param id
     * @param type
     * @param color
     * @param x
     * @param y
     * @param w
     * @param h
     * @param direccio
     */
    // constructor basicament per les bales
    public Sprite(long id, String type, Color color, int x, int y, int w, int h, Player.Direccio direccio) {
        super(w, h, color);
        this.direccio = direccio;
        this.id = id;
        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }

    /** Constructor específic per al player
     * @param type
     * @param color
     * @param x
     * @param y
     * @param w
     * @param h
     * @param direccio
     * @param velocitat
     */
    // Constructor per especificar la velocitat de moviment  -per al player-
    public Sprite(String type, Color color, int x, int y, int w, int h, Player.Direccio direccio, int velocitat) {
        super(w, h, color);
        this.direccio = direccio;
        this.type = type;
        this.velMoviment = velocitat;
        setTranslateX(x);
        setTranslateY(y);
        this.id = -1;
    }

    /**
     * Constructor per als enemics
     * @param id
     * @param type
     * @param color
     * @param x
     * @param y
     * @param w
     * @param h
     * @param direccio
     * @param velocitat
     */
    // Aquest per als enemics que necessito la seva id de sprite per identificar-los
    public Sprite(long id, String type, Color color, int x, int y, int w, int h, Player.Direccio direccio, int velocitat) {
        super(w, h, color);
        this.direccio = direccio;
        this.id = id;
        this.type = type;
        this.velMoviment = velocitat;
        setTranslateX(x);
        setTranslateY(y);
    }

    /**Serveix per mostrar la vida dels players
     * @return un array d'sprites, un per cada vida que li queda
     */
    public Sprite[] showVida() {
        Sprite[] vides = new Sprite[vida];
        for (int i = 0; i < vida; i++) {
            Sprite sprite = new Sprite("vida", Color.RED, (int) getTranslateX() + (i * 6), (int) getTranslateY()-20, 5, 10, Player.Direccio.S, 0);
            vides[i] = sprite;
        }
        return vides;
    }

    public String getImatgeActual() {
        return imatgeActual;
    }


    /** Posa imatge a l'sprite de tipus player
     * @param direccio la direcció en que mira l'sprite
     */
    public void setImatgeActual(Player.Direccio direccio) {
        Image img = null;
        switch (direccio.toString()) {
            case "N" -> {
                if (this.type.equals("players")) {
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("players_n_quiet.png")));

                } else
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("player_n_quiet.png")));
            }
            case "S" -> {
                if (this.type.equals("players")) {
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("players_s_quiet.png")));

                } else
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("player_s_quiet.png")));
            }
            case "E" -> {
                if (this.type.equals("players")) {
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("players_e_quiet.png")));

                } else
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("player_e_quiet.png")));

            }
            case "W" -> {
                if (this.type.equals("players")) {
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("players_w_quiet.png")));

                } else
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("player_w_quiet.png")));

            }
        }
        this.setFill(new ImagePattern(img));
    }

    /** Per donar per mort un enemic. Dibuixa un basalt de sang un cop mor
     * @param dead
     * @return retorna un Sprite amb la imatge de sang que deixen els enemics al morir
     */
    public Node setDead(boolean dead) {
        this.dead = dead;
        return new Sprite("sang",Color.RED, (int) getTranslateX(), (int) getTranslateY(), (int) getWidth(), (int) getHeight(), Player.Direccio.S,0).setImatgeSang();
    }

    /**getter del boolean dead
     * @return true si està mort o false si esta viu
     */
    public boolean isDead() {
        return dead;
    }


    /**
     * Mou el personatge
     */
    public void moveRight() {
        setTranslateX(getTranslateX() + velMoviment);
    }
    /**
     * Mou el personatge
     */
    public void moveLeft() {
        setTranslateX(getTranslateX() - velMoviment);
    }
    /**
     * Mou el personatge
     */
    public void moveUp() {
        setTranslateY(getTranslateY() - velMoviment);
    }
    /**
     * Mou el personatge
     */
    public void moveDown() {
        setTranslateY(getTranslateY() + velMoviment);
    }

    /**
     * Mou el personatge
     */
    public void moveLeftUp() {
        setTranslateY(getTranslateY() - velMoviment);
        setTranslateX(getTranslateX() - velMoviment);
    }
    /**
     * Mou el personatge
     */
    public void moveLeftDown() {
        setTranslateY(getTranslateY() + velMoviment);
        setTranslateX(getTranslateX() - velMoviment);
    }
    /**
     * Mou el personatge
     */
    public void moveRightUp() {
        setTranslateY(getTranslateY() - velMoviment);
        setTranslateX(getTranslateX() + velMoviment);
    }
    /**
     * Mou el personatge
     */
    public void moveRightDown() {
        setTranslateY(getTranslateY() + velMoviment);
        setTranslateX(getTranslateX() + velMoviment);
    }

    /**genera un sprite de bala
     */
    public Sprite atacar(Sprite sprite, long id) {


        Sprite s = null;
        int atacW = 16;
        int atacH = 8;


        // Comprovem la posicio de qui fa l'atac i referent a aixo fem que l'atac surti en la direccio en que mira qui fa l'atac
        switch (sprite.getDireccio()) {
            case W -> {
                s = new Sprite(id, "atac", Color.RED, (int) (sprite.getTranslateX() - atacW),
                        (int) (sprite.getTranslateY() + atacH + altura),
                        atacW, atacH, sprite.getDireccio());
            }
            case E -> {
                s = new Sprite(id, "atac", Color.RED, (int) (sprite.getTranslateX() + sprite.getWidth()),
                        (int) (sprite.getTranslateY() + atacH + altura), atacW, atacH, sprite.getDireccio());
            }
            case S -> {
                s = new Sprite(id, "atac", Color.RED, (int) (sprite.getTranslateX() + sprite.getWidth() / 2 - atacH / 2),
                        (int) (sprite.getTranslateY() + sprite.getWidth() - atacW + altura), atacH, atacW, sprite.getDireccio());
            }
            case N -> {
                s = new Sprite(id, "atac", Color.RED, (int) (sprite.getTranslateX() + sprite.getWidth() / 2 - atacH / 2),
                        (int) (sprite.getTranslateY() - atacW + altura), atacH, atacW, sprite.getDireccio());
            }

        }
        return s;
    }

    /**
     * @return retorna la direcció actual del sprite
     */
    public Player.Direccio getDireccio() {
        return direccio;
    }

    /** Perr cambiar la direcció en que mira l'sprite
     * @param direccio
     */
    public void setDireccio(Player.Direccio direccio) {
        this.direccio = direccio;
    }

    /**
     * @return retorna el tipus de sprite
     */
    public String getType() {
        return type;
    }


    /**
     * @return retorna la id de l'sprite
     */
    public long getIdSprite() {
        return id;
    }


    /**
     * @return posa la imatge de sang a l'sprite
     */
    public Node setImatgeSang(){
        Image img= new Image(String.valueOf(this.getClass().getClassLoader().getResource("sang_image.png")));
        this.setFill(new ImagePattern(img));
        return this;
    }

    /**
     * Posa la imatge pertinent als enemics tenint en compte la fase de moviment en que es troba i el tipus d'enemic que és
     *
     * @param imatge
     * @param tipus
     */
    public void setImatgeActual(int imatge, Enemic.Tipus tipus) {
        Image img = null;
        if (imatge == 0) {
            switch (tipus) {
                case PUMPKIN -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_pumpkin_1.png")));
                case FLOATING -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_floating_red_1.png")));
                case GHOST -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_ghost_1.png")));
                case BOSS -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_boss_1.png")));
                default -> throw new IllegalStateException("Unexpected value: " + tipus);
            }
        } else if (imatge == 1) {
            switch (tipus) {
                case PUMPKIN -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_pumpkin_2.png")));
                case FLOATING -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_floating_red_1.png")));
                case GHOST -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_ghost_2.png")));
                case BOSS -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_boss_2.png")));
                default -> throw new IllegalStateException("Unexpected value: " + tipus);
            }
        }

        this.setFill(new ImagePattern(img));

    }

    /**
     * Dibuixa la paraula fight o waiting depenent de l'estat de les olejades
     * @param estatOlejades
     */
    public void setImatgeActualFight(String estatOlejades) {
        Image img = null;
        if (estatOlejades.equals("wait")) {
            img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("waiting_2.png")));

        } else {
            img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("fight_1.png")));

        }

        this.setFill(new ImagePattern(img));

    }

    /**Posa imatge a l'sprite castell
     * @param vidaCastell
     */
    public void setImatgeCastell(int vidaCastell) {

        Image img = null;

        if (vidaCastell > 80) {
            img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("puig_castell.png")));

        } else if (vidaCastell > 60) {
            img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("puig_castell.png")));

        } else if (vidaCastell > 40) {
            img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("puig_castell.png")));

        } else if (vidaCastell > 20) {
            img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("puig_castell.png")));

        } else {
            img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("puig_castell.png")));


        }
        this.setFill(new ImagePattern(img));

    }

    public void setLabel(long ronda) {
        Label label = new Label();
        label.setText("Ronda: 1");
        label.setLayoutY(getTranslateY());
        label.setLayoutX(getTranslateX());
        label.setPrefWidth(getWidth());
        label.setPrefHeight(getHeight());
        label.setFont(Font.font("Verdana", 35));
        label.setVisible(true);


    }

    /**
     * Resta vida a l'sprite
     */
    public void restaVida() {
        if (vida > 1) vida--;
        else {
            vida--;
            setDead(true);
        }
    }
}
