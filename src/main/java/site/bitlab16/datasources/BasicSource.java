package site.bitlab16.datasources;

import site.bitlab16.TimeInstant;

public interface BasicSource {
    
    int getSeed();
    int getValue(TimeInstant when);
    float getEventi(TimeInstant when);
    float getAttivita(TimeInstant when);
    float getFestivita(TimeInstant when);
    int getModifierMeteoAsEnum(TimeInstant when);
    default float getIndiceOrario() { return 1000; }
    float getIndiceMeteo();
    float getIndiceEventi();
    float getIndiceAttivita();
    float getIndiceStagione();
   
}