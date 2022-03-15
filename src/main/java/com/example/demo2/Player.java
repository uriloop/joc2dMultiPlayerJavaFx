package com.example.demo2;

public class Player {

    private float posY, posX;
    private int id;

    public int getId() {
        return id;
    }

    public Player() {

    }

    private Direccio direccio;

    public Player(int id, float posY, float posX, Direccio direccio) {
        this.posY = posY;
        this.posX = posX;
        this.direccio = direccio;
        this.id=id;
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
    public enum Direccio {
        N, S, E, W;

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
