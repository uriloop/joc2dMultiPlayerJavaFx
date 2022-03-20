package com.example.demo2.model;

/**
 * Model per als enemics del joc
 */
public class Enemic {

    float posY, posX;
    float velMoviment;
    boolean viu = true;
    private int width, height;
    private long id;
    private Tipus tipus;

    /** Constructor de la classe
     * @param id
     * @param posY
     * @param posX
     * @param tipus
     */
    // constructor del client
    public Enemic(long id, float posY, float posX, Tipus tipus) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.tipus = tipus;
        switch (tipus) {
            case BOSS -> {
                width = 64;
                height = 64;
                velMoviment = 0.05f;
            }
            case FLOATING -> {
                width = 64;
                height = 64;
                velMoviment = 0.2f;

            }
            case GHOST -> {
                width = 64;
                height = 64;
                velMoviment = 0.15f;
            }
            case PUMPKIN -> {
                width = 64;
                height = 64;
                velMoviment = 0.1f;
            }
            default -> throw new IllegalStateException("Unexpected value: " + tipus);
        }

    }

    /**
     * Enum del tipus d'enemic que és
     */
    public enum Tipus {
        PUMPKIN, FLOATING, GHOST, BOSS;

        /**
         * @param num
         * @return retorna donat un numero
         */
        public Tipus getTipus(int num){
            switch (num){
                case 0 -> {
                    return PUMPKIN;
                }
                case 1 -> {
                    return FLOATING;
                }
                case  2 -> {
                    return GHOST;
                }
                case 3 -> {
                    return BOSS;
                }

            }
            return PUMPKIN;
        }
    }

    /**
     * Constructor buit per al jackson
     */
    public Enemic() {
    }

    /** Constructor secundari
     * @param tipus
     * @param id
     */
    public Enemic(Tipus tipus, long id) {
        this.tipus = tipus;
        this.id = id;
        switch (tipus) {
            case BOSS -> {
                width = 64;
                height = 64;
                posX = 600 - width;
                posY = 0;
                velMoviment = 0.05f;
            }
            case FLOATING -> {
                width = 64;
                height = 64;
                setRandomPosition();
                velMoviment = 0.2f;
            }
            case GHOST -> {
                width = 64;
                height = 64;
                setRandomPosition();
                velMoviment = 0.15f;
            }
            case PUMPKIN -> {
                width = 64;
                height = 64;
                setRandomPosition();
                velMoviment = 0.1f;
            }
            default -> throw new IllegalStateException("Unexpected value: " + tipus);
        }
    }

    private void setRandomPosition() {
        //   vull que vinguin desde la part superior per no complicar i que vagin tots en direcció a algo que han de protegir per no liar, més endavant posaria que es dirigeixin a l'enemic més proper o que fixin un objectiu...
        // Random entre 0 i el viewPortX-(int)widthEnemic
        int posRandom = (int) (Math.random() * (2000));
        if (posRandom < 400) {
            posX = 0;
            posY = posRandom;
        } else if (posRandom > 1600) {
            posX = 1200-width;
            posY = posRandom-1600;
        } else {
            posX= posRandom-400;
            posY= -height;
        }



    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getVelMoviment() {
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

    public Tipus getTipus() {
        return tipus;
    }

}
