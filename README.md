# CLASSI IMPORTANTI

## Simulator
Simulator possiede il main e gestisce cosa fa.
esempio:

>SimulatedSource source1 = new Source1(...);
>// 04/01/2021, istante 14 (14 è l'una e dieci - 01:10 );
>
>TimeInstant when = new TimeInstant(new GregorianCalendar(2021, Calendar.JANUARY, 4), 14));
>
>System.out.println( source1.getValue(when) );

## TimeInstant
Un giorno è compreso da 288 istanti spaziati di 5 minuti
(60*24/5=288). Un istanza di TimeInstant rappresenta una data
con un Calendar e un istante con un int. Puo essere visualizzato
come orario, es. 7 = 00:35, 14 = 01:10.
Serve per accedere ad un dato specifico di quel momento.

Per avanzare di 5 minuti, (new TimeInstant(...)).advance()

## SimulatedSource
SimulatedSource rappresenta le varie sorgenti dati, è astratta
e si estende per creare una nuova sorgente dati.

Un istanza encapsula un istanza di Random come generatore random.

La funzione principale della classe base è di aggiungere e unire
i vari metodi di introduzione di variazioni ai dati di baseline,
sia di random che influenzati da meteo o altro.

Le classi derivate si occupano solo di avere
- la struttura dati per i vari giorni della settimana o festivi
- metodi overloadati per ottenere i dati giusti da tale struttura dati


# TESTING

C'è JUnit nel progetto, vanno ancora messi i vari test.

# Avvio applicativo
> mvn clean install exec:java