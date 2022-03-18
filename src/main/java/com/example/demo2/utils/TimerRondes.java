package com.example.demo2.utils;

public class TimerRondes {


    private boolean on;

    public TimerRondes(long tempsEspera) {
        this.tempsEspera = tempsEspera;
        on=false;
    }

    long tempsEspera, tempsGuardat;


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

    public boolean haAcabatLespera() {
        System.out.println();
        if (System.currentTimeMillis()-tempsGuardat<tempsEspera) return false;
        on=false;
        return true;


    }

    public boolean isOn() {
        return on;
    }
}
