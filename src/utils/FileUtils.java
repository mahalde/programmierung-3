package utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utilities for everything related to files and directories
 */
public class FileUtils {

    /**
     * Creates a directory with the given name.
     * If the directory already exists it will not be cleared
     *
     * @param name the name of the directory
     */
    public static void createDirectory(String name) {
        try {
            Files.createDirectory(Paths.get(name));
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Checks if a name is a valid program name by validating if it is a valid java class name.
     * The name can also not contain any dots.
     *
     * @param name the name to check
     * @return true if the name is a valid program name and false if it is not
     */
    public static boolean isValidProgramName(String name) {
        // cannot contain any dots because it could give problems while compiling the file
        // dot notation is used for import of classes with their packages
        return name != null && SourceVersion.isName(name) && !name.contains(".");
    }

    /**
     * Gets the program name of a given file which is the last part after a dot.
     * E.g. "Hello.Program.World.java" -> "World"
     * If the file is not a java file, returns null.
     *
     * @param file the given file
     * @return the name of the program
     */
    public static String getJavaProgramName(File file) {
        String name = file.getName();

        if (!name.endsWith(".java")) {
            return null;
        }

        return name.substring(0, name.lastIndexOf('.'));
    }

    /**
     * Writes given content to a file with the given file name
     *
     * @param fileName the given file name
     * @param content  the content to write in the file
     */
    public static void writeToFile(String fileName, String... content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String line : content) {
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ViewUtils.showAlert(Alert.AlertType.ERROR,
                    "Fehler",
                    "Ein unbekannter Fehler ist aufgetreten",
                    null);
        }
    }

    /**
     * Gets the content of a file from the disk.
     * If the file doesn't exist returns an empty string.
     *
     * @param fileName the given file name
     * @return the contents of the file
     */
    public static String getContentFromFile(String fileName) {
        try {
            List<String> content = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());

            return content.stream()
                    .reduce((a, b) -> a + System.lineSeparator() + b)
                    .orElseThrow(() -> new IOException("Fehler beim Auslesen"));
        } catch (FileNotFoundException | NoSuchFileException e) {
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            ViewUtils.showAlert(Alert.AlertType.ERROR,
                    "Fehler",
                    "Ein unbekannter Fehler ist aufgetreten",
                    null);
            return "";
        }
    }
}
