package site.bitlab16.sources.points;

import java.util.Calendar;

import site.bitlab16.sources.SimulatedSource;

public class Fiera extends PuntoAllAperto {

    /* CREATION */

    public Fiera() {
        baseMultiplier = 0.12f;
    }

    
    /* METHODS */

    static final int[] usableDays = {Calendar.SATURDAY, Calendar.SATURDAY};
    @Override
    protected int[] getUsableDays() {
        return usableDays;
    }

    @Override
    public int getSeed() { return 5; }

    //imposto l'effetto degli eventi
    @Override
    protected int eventiEditValue(int flow, float modifier) {
        return Math.round((modifier+getIndiceEventi())*flow/getIndiceEventi());
    }

    //imposto l'effetto delle attività
    @Override
    protected int attivitaEditValue(int flow, float modifier) {
        return flow; // non ha attività la Fiera!!
    }

    
    @Override
    public float getIndiceOrario() {
        return 1000;
    }
    @Override
    public float getIndiceMeteo() {
        return 0.5f;
    }
    @Override
    public float getIndiceStagione() {
        return 1.5f;
    }
    @Override
    public float getIndiceAttivita() {
        return 1;
    }
    @Override
    public float getIndiceEventi() {
        return 1.2f;
    }

}