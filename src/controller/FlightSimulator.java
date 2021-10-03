package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.exception.InvalidProgramNameException;
import model.exception.ProgramAlreadyOpenException;
import utils.FileUtils;

/**
 * The main class for starting the flight simulator software
 */
public class FlightSimulator extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FileUtils.createDirectory(ProgramController.DIRECTORY_NAME);
        try {
            ProgramController.createNewProgram();
        } catch (ProgramAlreadyOpenException | InvalidProgramNameException e) {
            // Should never happen
            e.printStackTrace();
            Platform.exit();
        }
    }
}
