package site.bitlab16;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /*
        SimulatedSource s1 = new Source1();
        TimeInstant when = new TimeInstant(new GregorianCalendar(2020, Calendar.FEBRUARY, 26), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2020, Calendar.MARCH, 1), 1);

        while ( ! when.equals(end) ) {


            System.out.println(when + " " + s1.getValue(when));
            when.advance();
        }

*/
        SwingUtilities.invokeLater(() -> {
            Simulator example = new Simulator("---nome finestra---");
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setVisible(true);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}
