package controller;

import javafx.stage.Stage;
import model.Program;
import model.Territory;
import model.exception.InvalidProgramNameException;
import model.exception.ProgramAlreadyOpenException;
import utils.FileUtils;
import view.FlightSimulatorScene;
import view.TerritoryPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls all of the programs in the simulator.
 * Acts as a singleton class with exclusively static fields and methods.
 */
public class ProgramController {

    /**
     * The directory name where all programs get saved
     */
    public static final String DIRECTORY_NAME = "programme";
    /**
     * The default name of a program which gets launched on starting the simulator
     */
    private static final String DEFAULT_PROGRAM_NAME = "DefaultFlightSimulator";
    /**
     * The prefix with which every file gets saved to disk
     */
    private static final String PREFIX = "public class %s extends model.Plane { public" + System.lineSeparator();
    /**
     * The default content of a file
     */
    private static final String DEFAULT_CONTENT = "void main() {" + System.lineSeparator() + "\t" + System.lineSeparator() + "}";
    /**
     * The postfix with which every file gets saved to disk
     */
    private static final String POSTFIX = System.lineSeparator() + "}";

    /**
     * A list of all the currently open programs in the simulator
     */
    private static final List<Program> programs = new ArrayList<>();

    /**
     * Creates a new program with the default program name.
     *
     * @throws ProgramAlreadyOpenException if a program with the same name is already open
     * @throws InvalidProgramNameException if the given name is not a valid Java identifier
     */
    public static void createNewProgram() throws ProgramAlreadyOpenException, InvalidProgramNameException {
        createNewProgram(DEFAULT_PROGRAM_NAME);
    }

    /**
     * Creates a new window and program with the given name.
     *
     * @param name the given name
     * @throws ProgramAlreadyOpenException if a program with the same name is already open
     * @throws InvalidProgramNameException if the given name is not a valid Java identifier
     */
    public static void createNewProgram(String name) throws ProgramAlreadyOpenException, InvalidProgramNameException {
        if (programs.stream().map(Program::getName).anyMatch(programName -> programName.equals(name))) {
            throw new ProgramAlreadyOpenException(name);
        }

        if (!FileUtils.isValidProgramName(name)) {
            throw new InvalidProgramNameException(name);
        }

        Stage stage = new Stage();
        final ExceptionObservable exceptionObservable = new ExceptionObservable();

        Territory territory = new Territory(10, 14);

        Program program = new Program(name, stage, territory, exceptionObservable);
        programs.add(program);

        TerritoryPane territoryPane = new TerritoryPane(territory, exceptionObservable);
        territory.addObserver(territoryPane);

        String content = getContentFromFile(program);

        CompileController.initialCompileAndReload(territory, program);

        FlightSimulatorScene scene = new FlightSimulatorScene(1200, 800, territoryPane, territory, content, program.getSimulationManager());

        exceptionObservable.addObserver(scene);

        stage.setOnCloseRequest(e -> ProgramController.closeProgram());
        stage.setTitle(program.getName() + " - Java Flugsimulator");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gets the content of the given program from the disk while removing the pre- and postfix.
     * On an empty file returns the default content.
     *
     * @param program the given program
     * @return the content without pre- postfix
     */
    private static String getContentFromFile(Program program) {
        String allContent = FileUtils.getContentFromFile(program.getFullFileName());

        if (allContent.equals("")) {
            return DEFAULT_CONTENT;
        }

        return allContent.substring(String.format(PREFIX, program.getName()).length(), allContent.length() - POSTFIX.length());
    }

    /**
     * Returns the currently focused program
     *
     * @return the currently focused program
     */
    public static Program getFocusedProgram() {
        return programs.stream()
                .filter(program -> program.getStage().isFocused())
                .findAny()
                .orElseThrow(() -> new RuntimeException("No focused window found"));
    }

    /**
     * Saves the currently focused program to disk
     *
     * @param content the content which should be saved
     */
    public static void saveProgram(String content) {
        Program program = getFocusedProgram();
        String fileName = program.getFullFileName();

        FileUtils.writeToFile(fileName, String.format(PREFIX, program.getName()), content, POSTFIX);
    }

    /**
     * Saves the currently focused program and closes it.
     */
    public static void closeProgram() {
        Program program = getFocusedProgram();

        String content = getWrittenContent(program);
        saveProgram(content);

        programs.remove(program);

        program.getStage().close();

        if (programs.size() == 0) System.exit(0);
    }

    /**
     * Gets the written content of a program
     *
     * @param program the given program
     * @return the content written in the textbox
     */
    public static String getWrittenContent(Program program) {
        return ((FlightSimulatorScene) program.getStage().getScene()).getWrittenContent();
    }

    /**
     * Returns the placing state of the currently focused program
     *
     * @return the placing state
     */
    public static PlacingState getPlacingState() {
        return getFocusedProgram().getPlacingState();
    }

    /**
     * Sets the placing of the currently focused program
     *
     * @param state the placing state
     */
    public static void setPlacingState(PlacingState.State state) {
        getFocusedProgram().setPlacingState(state);
    }
}
