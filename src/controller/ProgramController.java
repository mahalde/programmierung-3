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

public class ProgramController {

    public static final String DIRECTORY_NAME = "programme";
    private static final String DEFAULT_PROGRAM_NAME = "DefaultFlightSimulator";
    private static final String PREFIX = "public class %s extends model.Plane { public" + System.lineSeparator();
    private static final String DEFAULT_CONTENT = "void main() {" + System.lineSeparator() + "\t" + System.lineSeparator() + "}";
    private static final String POSTFIX = System.lineSeparator() + "}";

    private static final List<Program> programs = new ArrayList<>();

    public static void createNewProgram() throws ProgramAlreadyOpenException, InvalidProgramNameException {
        createNewProgram(DEFAULT_PROGRAM_NAME);
    }

    public static void createNewProgram(String name) throws ProgramAlreadyOpenException, InvalidProgramNameException {
        if (programs.stream().map(Program::getName).anyMatch(programName -> programName.equals(name))) {
            throw new ProgramAlreadyOpenException(name);
        }

        if (!FileUtils.isValidProgramName(name)) {
            throw new InvalidProgramNameException(name);
        }

        Stage stage = new Stage();
        final ExceptionObservable exceptionObservable = new ExceptionObservable();

        Program program = new Program(name, stage);

        programs.add(program);

        String content = getContentFromFile(program);

        Territory territory = new Territory(10, 14);
        TerritoryPane territoryPane = new TerritoryPane(territory);
        territory.addObserver(territoryPane);

        CompileController.initialCompileAndReload(territory, program);

        FlightSimulatorScene scene = new FlightSimulatorScene(1200, 800, territoryPane, territory, content);

        exceptionObservable.addObserver(scene);

        stage.setOnCloseRequest(e -> ProgramController.closeProgram());
        stage.setTitle(program.getName() + " - Java Flugsimulator");
        stage.setScene(scene);
        stage.show();
    }

    private static String getContentFromFile(Program program) {
        String allContent = FileUtils.getContentFromFile(program.getFullFileName());

        if (allContent.equals("")) {
            return DEFAULT_CONTENT;
        }

        return allContent.substring(String.format(PREFIX, program.getName()).length(), allContent.length() - POSTFIX.length());
    }

    public static Program getFocusedProgram() {
        return programs.stream()
                .filter(program -> program.getStage().isFocused())
                .findAny()
                .orElseThrow(() -> new RuntimeException("No focused window found"));
    }

    public static void saveProgram(String content) {
        Program program = getFocusedProgram();
        String fileName = program.getFullFileName();

        FileUtils.writeToFile(fileName, String.format(PREFIX, program.getName()), content, POSTFIX);
    }

    public static void closeProgram() {
        Program program = getFocusedProgram();

        String content = getWrittenContent(program);
        saveProgram(content);

        programs.remove(program);

        program.getStage().close();
    }

    public static String getWrittenContent(Program program) {
        return ((FlightSimulatorScene) program.getStage().getScene()).getWrittenContent();
    }
}
