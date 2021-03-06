package model;

import controller.ExceptionObservable;
import controller.PlacingState;
import controller.ProgramController;
import controller.simulation.SimulationManager;
import javafx.stage.Stage;

import java.io.File;

/**
 * Basic program which contains the stage, placing state and the simulation manager
 */
public class Program {

    private final String name;
    private final Stage stage;
    private final PlacingState placingState;
    private final SimulationManager simulationManager;

    public Program(String name, Stage stage, Territory territory, ExceptionObservable exceptionObservable) {
        this.name = name;
        this.stage = stage;
        this.placingState = new PlacingState();
        this.simulationManager = new SimulationManager(territory, exceptionObservable);
    }

    public String getName() {
        return name;
    }

    public Stage getStage() {
        return stage;
    }

    public String getFileName() {
        return name + ".java";
    }

    public String getFullFileName() {
        return ProgramController.DIRECTORY_NAME + File.separator + getFileName();
    }

    public PlacingState getPlacingState() {
        return placingState;
    }

    public SimulationManager getSimulationManager() {
        return this.simulationManager;
    }

    public void setPlacingState(PlacingState.State state) {
        this.placingState.setSelected(state);
    }
}
