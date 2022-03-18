package com.example.demo2;

public class Player {

    // Els tinc en protected en comptes de private perque en principi pretenc extendre d'aquesta classe i vull poder modificar aquestes variables

    protected float posY, posX;
    protected int id;
    protected boolean mort=false;
    private int kills= 0;

    public int getId() {
        return id;
    }

    public Player() {

    }

    protected Direccio direccio;

    public Player(int id, float posY, float posX, Direccio direccio) {
        this.posY = posY;
        this.posX = posX;
        this.direccio = direccio;
        this.id=id;
    }

    public boolean isMort() {
        return mort;
    }

    public void setMort(boolean mort) {
        this.mort = mort;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setDireccio(Direccio direccio) {
        this.direccio = direccio;
    }

    public void sumaKill() {
        this.kills+=1;
    }

    public enum Direccio {
        N, S, E, W;

    }

    public int getKills() {
        return kills;
    }

    public float getPosY() {
        return posY;
    }

    public float getPosX() {
        return posX;
    }

    public Direccio getDireccio() {
        return direccio;
    }


}
