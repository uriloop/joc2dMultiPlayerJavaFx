package com.example.demo2;

public class Enemic {

    float posY, posX;
    int velMoviment;
    boolean viu = true;
    private int width,height;
    private long id;
    private Tipus tipus;

    // constructor del client
    public Enemic(long id, float posY, float posX, Tipus tipus) {
        this.id=id;
        this.posX=posX;
        this.posY=posY;
        this.tipus=tipus;
        switch (tipus){
            case BOSS ->  {
                width=64;
                height=64;
                velMoviment=5;
            }
            case FLOATING -> {
                width=32;
                height=32;
                velMoviment=10;

            }
            case PUMPKIN -> {
                width=32;
                height=32;
                velMoviment=8;
            }
            default -> throw new IllegalStateException("Unexpected value: " + tipus);
        }

    }

    public enum Tipus{
        PUMPKIN, FLOATING, BOSS;
    }

    public Enemic() {
    }

    public Enemic(Tipus tipus, long id) {
        this.tipus=tipus;
        this.id=id;
        switch (tipus){
            case BOSS ->  {
                width=64;
                height=64;
                posX=600-width;
                posY=0;
                velMoviment=5;
            }
            case FLOATING -> {
                width=32;
                height=32;
                setRandomPosition();
                velMoviment=10;

            }
            case PUMPKIN -> {
                width=32;
                height=32;
                setRandomPosition();
                velMoviment=8;

            }
            default -> throw new IllegalStateException("Unexpected value: " + tipus);
        }
    }
    private void setRandomPosition() {
        //   vull que vinguin desde la part superior per no complicar i que vagin tots en direcció a algo que han de protegir per no liar, més endavant posaria que es dirigeixin a l'enemic més proper o que fixin un objectiu...
        // Random entre 0 i el viewPortX-(int)widthEnemic
        posY=0;
        posX= (float) (Math.random()*(1200-width))+1;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getVelMoviment() {
        return velMoviment;
    }

    public void setVelMoviment(int velMoviment) {   // per si rep un atac congelant
        this.velMoviment = velMoviment;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public boolean isViu() {
        return viu;
    }

    public void setViu(boolean viu) {
        this.viu = viu;
    }

    public long getId() {
        return id;
    }



    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Tipus getTipus() {
        return tipus;
    }

    public void setTipus(Tipus tipus) {
        this.tipus = tipus;
    }
}
