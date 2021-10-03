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

public class FileUtils {

    public static void createDirectory(String name) {
        try {
            Files.createDirectory(Paths.get(name));
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public static boolean isValidProgramName(String name) {
        return name != null && SourceVersion.isName(name) && !name.contains(".");
    }

    public static String getJavaProgramName(File file) {
        String name = file.getName();

        if (!name.endsWith(".java")) {
            return null;
        }

        return name.substring(0, name.lastIndexOf('.'));
    }

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
