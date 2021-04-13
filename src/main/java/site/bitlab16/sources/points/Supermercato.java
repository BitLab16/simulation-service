package site.bitlab16.sources.points;

import java.util.Calendar;

import site.bitlab16.sources.SimulatedSource;

public class Supermercato extends PuntoAllAperto {

    /* CREATION */

    public Supermercato() {
        baseMultiplier = 0.012f;
    }

    
    /* METHODS */

    static final int[] usableDays = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
    @Override
    protected int[] getUsableDays() {
        return usableDays;
    }

    @Override
    public int getSeed() { return 4; }

    //imposto l'effetto degli eventi
    @Override
    protected int eventiEditValue(int flow, float modifier) {
        return flow; // non ha eventi il supermercato!!
    }

    //imposto l'effetto delle attività
    @Override
    protected int attivitaEditValue(int flow, float modifier) {
        return Math.round((modifier+getIndiceAttivita())*flow/getIndiceAttivita());
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
        return 2;
    }
    @Override
    public float getIndiceAttivita() {
        return .3f;
    }
    @Override
    public float getIndiceEventi() {
        return 1;
    }

}