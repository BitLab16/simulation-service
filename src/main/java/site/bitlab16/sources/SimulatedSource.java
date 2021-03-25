package site.bitlab16.sources;

import java.util.Random;
import site.bitlab16.TimeInstant;

/**
 * CLASSE ASTRATTA CHE RAPPRESENTA LA SORGENTE DATI
 * QUESTA CLASSE SI OCCUPA DI APPLICARE LE OPPORTUNE
 * MODIFICHE AI DATI FORNITI DALLE SOTTOCLASSI
 * QUESTE MODIFICHE SONO:
 *  -stagioni, pioggia, eventi, ecc
 *  -randomness extra a ondine
 *  -ecc.
 */
public abstract class SimulatedSource { 

    protected Random random;

    public SimulatedSource() {
        random = new Random(1);
    }

    final public Integer getValue(TimeInstant when) {
        return getSourceSpecificExpectedValue(when);
    }

    // create a pattern for specific source to deliver ideal perfect data
    // that does not consider randomness -> perfect reality simulation
    protected abstract Integer getSourceSpecificExpectedValue(TimeInstant when);

    protected void applySourceSpecificRandomlyGeneratedFunctions() {
        // abstract
    }

    protected void applySourceIndependentRandomlyGeneratedFunctions() {
        // non abstract
    }

    protected void applySourceIndependentHandWrittenFunctions() {
        // non abstract
    }

    protected void applyMonteCarloRandomnessToDerivative() {
        // non abstract
    }

}