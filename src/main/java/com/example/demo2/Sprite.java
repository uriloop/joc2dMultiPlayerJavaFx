package com.example.demo2;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle {


    private int velMoviment = 2;
    int altura = 20;
    private boolean dead = false;
    private String type = null;
    private Player.Direccio direccio = Player.Direccio.S;
    private long id;
    private String imatgeActual;

/*
    public Sprite(String type, Color color, int x, int y, int w, int h) {
        super(w,h,color);
        // per carregar els missatges
        this.type=type;
        setTranslateX(x);
        setTranslateY(y);

    }*/

    public String getImatgeActual() {
        return imatgeActual;
    }


    public void setImatgeActual(Player.Direccio direccio) {
        Image img = null;
        switch (direccio.toString()) {
            case  "N"-> {
                if (this.type.equals("players")){
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("players_n_quiet.png")));

                }else
                img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("player_n_quiet.png")));
            }
            case "S" -> {
                if (this.type.equals("players")){
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("players_s_quiet.png")));

                }else
                img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("player_s_quiet.png")));
            }
            case "E" -> {
                if (this.type.equals("players")){
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("players_e_quiet.png")));

                }else
                img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("player_e_quiet.png")));

            }
            case "W" -> {
                if (this.type.equals("players")){
                    img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("players_w_quiet.png")));

                }else
                img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("player_w_quiet.png")));

            }
        }
        this.setFill(new ImagePattern(img));
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDead() {
        return dead;
    }

    // constructor basicament per les bales
    public Sprite(long id, String type, Color color, int x, int y, int w, int h, Player.Direccio direccio) {
        super(w, h, color);
        this.direccio = direccio;
        this.id = id;
        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }

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

    public void moveRight() {
        setTranslateX(getTranslateX() + velMoviment);
    }

    public void moveLeft() {
        setTranslateX(getTranslateX() - velMoviment);
    }

    public void moveUp() {
        setTranslateY(getTranslateY() - velMoviment);
    }

    public void moveDown() {
        setTranslateY(getTranslateY() + velMoviment);
    }


    public void moveLeftUp() {
        setTranslateY(getTranslateY() - velMoviment);
        setTranslateX(getTranslateX() - velMoviment);
    }

    public void moveLeftDown() {
        setTranslateY(getTranslateY() + velMoviment);
        setTranslateX(getTranslateX() - velMoviment);
    }

    public void moveRightUp() {
        setTranslateY(getTranslateY() - velMoviment);
        setTranslateX(getTranslateX() + velMoviment);
    }

    public void moveRightDown() {
        setTranslateY(getTranslateY() + velMoviment);
        setTranslateX(getTranslateX() + velMoviment);
    }


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

    public Player.Direccio getDireccio() {
        return direccio;
    }

    public void setDireccio(Player.Direccio direccio) {
        this.direccio = direccio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getIdSprite() {
        return id;
    }

    public void setIdSprite(long id) {
        this.id = id;
    }

    public void setImatgeActual(int imatge, Enemic.Tipus tipus) {
        Image img = null;
        if (imatge==0){
            switch (tipus){
                case PUMPKIN -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_pumpkin_1.png")));
                case FLOATING -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_floating_red_1.png")));
                case BOSS -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_boss_1.png")));
                default -> throw new IllegalStateException("Unexpected value: " + tipus);
            }
        }else if (imatge==1){
            switch (tipus){
                case PUMPKIN -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_pumpkin_2.png")));
                case FLOATING -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_floating_red_1.png")));
                case BOSS -> img = new Image(String.valueOf(this.getClass().getClassLoader().getResource("enemy_boss_2.png")));
                default -> throw new IllegalStateException("Unexpected value: " + tipus);
            }
        }

        this.setFill(new ImagePattern(img));

    }
}
