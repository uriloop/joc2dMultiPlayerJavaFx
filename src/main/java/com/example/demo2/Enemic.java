package com.example.demo2;

public class Enemic {

    float posY, posX;
    boolean viu = true;
    private int width,height;
    private long id;
    private Tipus tipus;

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
            }
            case FLOATING -> {
                width=32;
                height=32;
                setRandomPosition();
            }
            case PUMPKIN -> {
                width=32;
                height=32;
                setRandomPosition();
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
}
