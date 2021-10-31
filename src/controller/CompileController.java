package controller;

import controller.simulation.SimulationManager;
import controller.simulation.SimulationState;
import javafx.scene.control.Alert;
import model.Plane;
import model.Program;
import model.Territory;
import utils.ViewUtils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Contains methods for compiling written code and reloading the plane in the territory.
 * Acts as a singleton class with exclusively static fields and methods.
 */
public class CompileController {

    /**
     * Compiles and reloads the initial program which gets loaded when the simulator is launched.
     *
     * @param territory the territory which should be reloaded
     * @param program   the program to compile
     */
    public static void initialCompileAndReload(Territory territory, Program program) {
        boolean success = compile(null, program);

        if (success) {
            loadAndSetNewPlane(territory, program, false);
        }
    }

    /**
     * Compiles the currently focused program and reloads the corresponding territory
     *
     * @param territory         the given territory
     * @param simulationManager the simulation manager of the focused simulation
     */
    public static void compileAndReload(Territory territory, SimulationManager simulationManager) {
        if (simulationManager.getState().getSelected() == SimulationState.State.STARTED) {
            ViewUtils.showAlert(Alert.AlertType.ERROR,
                    null,
                    null,
                    "Während einer laufenden Simulation kann nicht kompiliert werden!");
            return;
        }

        Program program = ProgramController.getFocusedProgram();
        String content = ProgramController.getWrittenContent(program);

        ProgramController.saveProgram(content);

        ByteArrayOutputStream err = new ByteArrayOutputStream();

        boolean success = compile(err, program);

        if (!success) {
            String errMessage = err.toString();
            ViewUtils.showAlert(Alert.AlertType.ERROR,
                    "Fehler beim Kompilieren",
                    null,
                    errMessage);
        } else {
            loadAndSetNewPlane(territory, program, true);
            ViewUtils.showAlert(Alert.AlertType.INFORMATION,
                    "Erfolgreiches Kompilieren",
                    null,
                    "Das Programm wurde erfolgreich kompiliert");
        }
    }

    /**
     * Compiles a given file
     *
     * @param errStream error stream to give feedback back to the user on failed compilation
     * @param program   the program which is saved in a file
     * @return whether the program compiled successfully
     */
    private static boolean compile(ByteArrayOutputStream errStream, Program program) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        return compiler.run(null, null, errStream, program.getFullFileName()) == 0;
    }

    /**
     * Reloads the plane and places it back in the territory
     *
     * @param territory the given territory
     * @param program   the program from which to load the plane
     * @param showAlert indicates whether to show an alert on failed instantiation
     */
    public static void loadAndSetNewPlane(Territory territory, Program program, boolean showAlert) {
        ClassLoader loader;
        try {
            loader = new URLClassLoader(new URL[]{new File(ProgramController.DIRECTORY_NAME).toURI().toURL()});
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        try {
            Class<?> planeClass = loader.loadClass(program.getName());

            Plane plane = (Plane) planeClass.newInstance();

            territory.setPlane(plane);
        } catch (Exception e) {
            e.printStackTrace();
            if (showAlert) {
                ViewUtils.showAlert(Alert.AlertType.ERROR,
                        "Fehler beim Kompilieren",
                        null,
                        "Beim Kompilieren ist ein Fehler aufgetreten");
            }
        }
    }
}
