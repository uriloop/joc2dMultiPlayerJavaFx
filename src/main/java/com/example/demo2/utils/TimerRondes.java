package com.example.demo2.utils;

/**
 * Classe temporitzador
 */
public class TimerRondes {


    private boolean on;

    public TimerRondes(long tempsEspera) {
        this.tempsEspera = tempsEspera;
        on=false;
    }

    long tempsEspera, tempsGuardat;


    /**
     * Inicia el temporitzador
     */
    public void startEspera() {
        tempsGuardat=System.currentTimeMillis();
        on=true;
    }

    public long getTempsEspera() {
        return tempsEspera;
    }

    public void setTempsEspera(long tempsEspera) {
        this.tempsEspera = tempsEspera;
    }

    /**
     * @return retorna true si ha acabat l'espera i apaga el temporitzador
     */
    public boolean haAcabatLespera() {
        System.out.println();
        if (System.currentTimeMillis()-tempsGuardat<tempsEspera) return false;
        on=false;
        return true;


    }

    /**
     * @return getter per saber si el temporitzador esta actiu
     */
    public boolean isOn() {
        return on;
    }
}
