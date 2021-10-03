package model;

import controller.ProgramController;
import javafx.stage.Stage;

import java.io.File;

public class Program {

    private final String name;
    private final Stage stage;

    public Program(String name, Stage stage) {
        this.name = name;
        this.stage = stage;
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
}
