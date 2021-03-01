package site.bitlab16;

import java.util.Random;

/**
 * CLASSE ASTRATTA CHE RAPPRESENTA LA SORGENTE DATI
 * QUESTA CLASSE SI OCCUPA DI APPLICARE LE OPPORTUNE
 * MODIFICHE AI DATI FORNITI DALLE SOTTOCLASSI
 * QUESTE MODIFICHE SONO:
 *  -randomness alla 'montecarlo'
 *  -stagioni, pioggia, eventi, ecc
 *  -randomness extra a ondine
 *  -ecc.
 */
public abstract class SimulatedSource { 

    private Random random;
    SimulatedSource() {
        random = new Random(1);
    }

    final public int getValue(TimeInstant when) {
        int shift = random.nextInt(15);
        return getSourceSpecificExpectedValue(when) + 0*shift;
    }

    // create a pattern for specific source to deliver ideal perfect data
    // that does not consider randomness -> perfect reality simulation
    protected abstract int getSourceSpecificExpectedValue(TimeInstant when);

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
        // nin abstract
    }

}