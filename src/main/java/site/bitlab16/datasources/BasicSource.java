package site.bitlab16.datasources;

import site.bitlab16.TimeInstant;

public interface BasicSource {
    
    public int getSeed();
    public int getValue(TimeInstant when);
    public float getEventi(TimeInstant when);
    public float getAttivita(TimeInstant when);
    public float getFestivita(TimeInstant when);
    public int getModifierMeteoAsEnum(TimeInstant when);
    public default float getIndiceOrario() { return 1000; };
    public float getIndiceMeteo();
    public float getIndiceEventi();
    public float getIndiceAttivita();
    public float getIndiceStagione();
   
}