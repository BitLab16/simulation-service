package site.bitlab16;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;

import site.bitlab16.kafka_producer.Consumer;
import site.bitlab16.model.SourceRecord;

public class App {

    enum ApplicationScope {
        DEBUG, // Con grafici, assume server grafico
        RELEASE, // deployabile in un docker, no X11
        TMP
    }
    
    final public static ApplicationScope BUILD_MODE = ApplicationScope.DEBUG;

    
    public static void main( String[] args ) {
        
        
        switch (BUILD_MODE) {
        
            case TMP:



// Prepare input timeseries data.
double[] dataArray = new double[] {2, 1, 2, 5, 2, 1, 2, 5, 2, 1, 2, 5, 2, 1, 2, 5};

// Set ARIMA model parameters.
int p = 3, d = 0, q = 3, P = 1, D = 1, Q = 0, m = 0;
int forecastSize = 1;

// Obtain forecast result. The structure contains forecasted values and performance metric etc.
ArimaParams params = new ArimaParams(p, d, q, P, D, Q, m);
ForecastResult forecastResult = Arima.forecast_arima(dataArray, forecastSize, params);

// Read forecast values
double[] forecastData = forecastResult.getForecast(); // in this example, it will return { 2 }

// You can obtain upper- and lower-bounds of confidence intervals on forecast values.
// By default, it computes at 95%-confidence level. This value can be adjusted in ForecastUtil.java
double[] uppers = forecastResult.getForecastUpperConf();
double[] lowers = forecastResult.getForecastLowerConf();

// You can also obtain the root mean-square error as validation metric.
double rmse = forecastResult.getRMSE();

// It also provides the maximum normalized variance of the forecast values and their confidence interval.
double maxNormalizedVariance = forecastResult.getMaxNormalizedVariance();

// Finally you can read log messages.
String log = forecastResult.getLog();

for (var s : forecastData) System.out.println(s);






                break;

            case RELEASE:
                BlockingDeque<SourceRecord> outQueue = new LinkedBlockingDeque<>();
                String kafkaBootstrapServers = "kafka1:19091";
                Simulator simulator = new Simulator(outQueue);
                Consumer consumer = new Consumer("Simulatore 1", kafkaBootstrapServers, outQueue);
                ExecutorService executor = Executors.newCachedThreadPool();
                executor.execute(simulator);
                executor.execute(consumer);
                break;
            
            case DEBUG:
                SimulatorWithGraphs simulatorWindow = new SimulatorWithGraphs("---nome finestra---");
                SwingUtilities.invokeLater(() -> {
                    simulatorWindow.setSize(800, 400);
                    simulatorWindow.setLocationRelativeTo(null);
                    simulatorWindow.setVisible(true);
                    simulatorWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    simulatorWindow.run();
                });
                break;                
        }
    }
}
