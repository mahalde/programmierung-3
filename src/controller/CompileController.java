package controller;

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

public class CompileController {

    public static void initialCompileAndReload(Territory territory, Program program) {
        boolean success = compile(null, program);

        if (success) {
            loadAndSetNewPlane(territory, program, false);
        }
    }

    public static void compileAndReload(Territory territory) {
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

    private static boolean compile(ByteArrayOutputStream errStream, Program program) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        return compiler.run(null, null, errStream, program.getFullFileName()) == 0;
    }

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
            territory.setPlaneTerritory();

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
