package controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.Territory;

import java.util.Optional;

public class ChangeSizeEventHandler<T extends Event> implements EventHandler<T> {

    private final Territory territory;

    public ChangeSizeEventHandler(Territory territory) {
        this.territory = territory;
    }

    @Override
    public void handle(T event) {
        // Inspired by https://code.makery.ch/blog/javafx-dialogs-official/#custom-login-dialog
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();

        dialog.setHeaderText("Welche Größe soll das Territorium haben?");

        ButtonType okButton = new ButtonType("Größe ändern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField widthField = new TextField();
        widthField.setPromptText("Breite");
        widthField.setText(String.valueOf(this.territory.getWidth()));
        TextField heightField = new TextField();
        heightField.setPromptText("Höhe");
        heightField.setText(String.valueOf(this.territory.getHeight()));

        grid.add(new Label("Breite:"), 0, 0);
        grid.add(widthField, 1, 0);
        grid.add(new Label("Höhe:"), 0, 1);
        grid.add(heightField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Node okButtonNode = dialog.getDialogPane().lookupButton(okButton);

        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                okButtonNode.setDisable(value < 1 || value > 100);
            } catch (NumberFormatException ignored) {
                okButtonNode.setDisable(true);
            }
        });

        heightField.textProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                okButtonNode.setDisable(value < 1 || value > 100);
            } catch (NumberFormatException ignored) {
                okButtonNode.setDisable(true);
            }
        }));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return new Pair<>(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

        result.ifPresent(pair -> this.territory.changeSize(pair.getKey(), pair.getValue()));
    }
}
