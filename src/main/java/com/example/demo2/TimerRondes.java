package com.example.demo2;

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

        if (tempsGuardat+tempsEspera<System.currentTimeMillis()) return false;
        on=false;
        return true;


    }

    public boolean isOn() {
        return on;
    }
}
