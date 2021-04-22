package site.bitlab16.datasources;

public interface SourceValuesEditor {

    /*stabilisce l'impatto dei giorni festivi, spesso li sostituisce proprio!*/
    public int festeEditValue(String date, int flow, int instant, float modifier);
    /*stabilisce l'effetto del meteo*/
    public int meteoEditValue(String date, int flow, int instant, float modifier);
    /*stabilisce l'effetto delle stagioni*/
    public int seasonEditValue(int dayOfYear, int flow, int instant);
    /*stabilisce l'effetto degli eventi*/
    public int eventiEditValue(int flow, float modifier);
    /*stabilisce l'effetto delle attivita*/
    public int attivitaEditValue(int flow, float modifier);
    
}
