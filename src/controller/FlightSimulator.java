package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Territory;
import view.FlightSimulatorScene;
import view.TerritoryPane;

import java.util.Observable;

/**
 * The main class for starting the flight simulator software
 */
public class FlightSimulator extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private final ExceptionObservable exceptionObservable = new ExceptionObservable();

    @Override
    public void start(Stage primaryStage) {
        Thread.setDefaultUncaughtExceptionHandler(this::handleSimulationException);

        Territory territory = new Territory(10, 14);
        TerritoryPane territoryPane = new TerritoryPane(territory);
        territory.addObserver(territoryPane);

        FlightSimulatorScene scene = new FlightSimulatorScene(1200, 800, territoryPane, territory);

        this.exceptionObservable.addObserver(scene);

        primaryStage.setTitle("Java Flugsimulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSimulationException(Thread t, Throwable e) {
        this.exceptionObservable.notifyAboutException(e);
    }
}
