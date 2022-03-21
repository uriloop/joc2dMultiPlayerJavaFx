package com.example.demo2.model;

/**
 * Classe model per a les bales
 */
public class Bala {

    private float posX, posY;
    private Player.Direccio dir;
    private long idBala;
    private boolean dead=false;

    /**
     * Constructor buit per al jackson
     */
    public Bala() {
    }

    /** Constructor del client
     * @param idBales
     * @param posX
     * @param posY
     * @param direccio
     */
    public Bala(long idBales, float posX, float posY, Player.Direccio direccio) {
        idBala=idBales;
        this.posX = posX;
        this.posY = posY;
        this.dir = direccio;
    }


    public long getIdBala() {
        return idBala;
    }

    public void setIdBala(long idBala) {
        this.idBala = idBala;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public Player.Direccio getDir() {
        return dir;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setDir(Player.Direccio dir) {
        this.dir = dir;
    }
}
