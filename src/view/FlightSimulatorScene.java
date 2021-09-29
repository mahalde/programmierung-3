package view;

import controller.ButtonClickEventHandler;
import controller.ChangeSizeEventHandler;
import controller.PlacingState;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Plane;
import model.Territory;
import model.exception.SimulatorException;
import utils.ViewUtils;

import java.util.Observable;
import java.util.Observer;

public class FlightSimulatorScene extends Scene implements Observer {

    private final TerritoryPane territoryPane;
    private final Territory territory;

    private Label label;

    public FlightSimulatorScene(double width, double height, TerritoryPane territoryPane, Territory territory) {
        super(new BorderPane(), width, height);

        this.territoryPane = territoryPane;
        this.territory = territory;
        populateScene();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof SimulatorException) {
            this.label.setText(((SimulatorException) arg).getMessage());
        }
    }

    private void populateScene() {
        MenuBar menuBar = createMenuBar();


        ((BorderPane) this.getRoot()).setTop(menuBar);

        BorderPane contentPane = new BorderPane();
        ToolBar toolBar = createToolbar();
        contentPane.setTop(toolBar);

        SplitPane content = createSplitPane();
        contentPane.setCenter(content);
        Platform.runLater(() -> content.getItems().get(0).requestFocus());

        this.label = new Label("Herzlich willkommen!");
        contentPane.setBottom(this.label);

        BorderPane.setAlignment(toolBar, Pos.CENTER_LEFT);
        BorderPane.setAlignment(this.label, Pos.CENTER_LEFT);

        ((BorderPane) this.getRoot()).setCenter(contentPane);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu editorMenu = createEditorMenu();
        Menu territoryMenu = createTerritoryMenu();
        Menu hamsterMenu = createPlaneMenu();
        Menu simulationMenu = createSimulationMenu();

        menuBar.getMenus().addAll(editorMenu, territoryMenu, hamsterMenu, simulationMenu);

        return menuBar;
    }

    private Menu createEditorMenu() {
        Menu editorMenu = new Menu("_Editor");

        MenuItem newItem = new MenuItem("_Neu",
                ViewUtils.createImage("/resources/New16.gif"));
        ViewUtils.addAccelerator(newItem, "SHORTCUT+N");

        MenuItem openItem = new MenuItem("_Öffnen",
                ViewUtils.createImage("/resources/Open16.gif"));
        ViewUtils.addAccelerator(openItem, "SHORTCUT+O");

        MenuItem compileItem = new MenuItem("_Kompilieren");
        ViewUtils.addAccelerator(compileItem, "SHORTCUT+K");

        MenuItem printItem = new MenuItem("_Drucken",
                ViewUtils.createImage("/resources/Print16.gif"));
        printItem.setAccelerator(KeyCombination.valueOf("SHORTCUT+P"));

        MenuItem closeItem = new MenuItem("_Beenden");
        closeItem.setAccelerator(KeyCombination.valueOf("SHORTCUT+Q"));
        closeItem.setOnAction(e -> Platform.exit());

        editorMenu.getItems().addAll(
                newItem,
                openItem,
                new SeparatorMenuItem(),
                compileItem,
                printItem,
                new SeparatorMenuItem(),
                closeItem
        );

        return editorMenu;
    }

    private Menu createTerritoryMenu() {
        Menu territoryMenu = new Menu("_Territorium");

        Menu saveMenu = new Menu("S_peichern");

        MenuItem xmlItem = new MenuItem("_XML");
        MenuItem jaxbItem = new MenuItem("_JAXB");
        MenuItem serialiseItem = new MenuItem("Se_rialisieren");

        saveMenu.getItems().addAll(xmlItem, jaxbItem, serialiseItem);

        Menu loadMenu = new Menu("_Laden");

        MenuItem loadXmlItem = new MenuItem("_XML");
        MenuItem loadJaxbItem = new MenuItem("_JAXB");
        MenuItem deserialiseItem = new MenuItem("_Deserialisieren");

        loadMenu.getItems().addAll(loadXmlItem, loadJaxbItem, deserialiseItem);

        Menu pictureMenu = new Menu("Als _Bild speichern");

        MenuItem jpegItem = new MenuItem("JPG");
        MenuItem pngItem = new MenuItem("PNG");

        pictureMenu.getItems().addAll(jpegItem, pngItem);

        MenuItem printItem = new MenuItem("_Drucken");
        MenuItem changeSizeItem = new MenuItem("_Größe ändern");
        changeSizeItem.setOnAction(new ChangeSizeEventHandler<>(territory));

        ToggleGroup placeSomethingGroup = new ToggleGroup();
        RadioMenuItem placePlane = new PlacingStateRadioMenuItem("Flug_zeug platzieren", PlacingState.State.PLANE);
        RadioMenuItem placePassenger = new PlacingStateRadioMenuItem("_Passagier platzieren", PlacingState.State.PASSENGER);
        RadioMenuItem placeThunderstorm = new PlacingStateRadioMenuItem("Ge_witter platzieren", PlacingState.State.THUNDERSTORM);
        RadioMenuItem deleteField = new PlacingStateRadioMenuItem("Ka_chel löschen", PlacingState.State.DELETE);
        placeSomethingGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            Toggle selectedToggle = placeSomethingGroup.getSelectedToggle();
            if (selectedToggle != null) {
                PlacingState.getState().setSelected((PlacingState.State) selectedToggle.getUserData());
            }
        }));

        placePlane.setToggleGroup(placeSomethingGroup);
        placePassenger.setToggleGroup(placeSomethingGroup);
        placeThunderstorm.setToggleGroup(placeSomethingGroup);
        deleteField.setToggleGroup(placeSomethingGroup);

        territoryMenu.getItems().addAll(
                saveMenu,
                loadMenu,
                pictureMenu,
                printItem,
                changeSizeItem,
                new SeparatorMenuItem(),
                placePlane,
                placePassenger,
                placeThunderstorm,
                deleteField
        );

        return territoryMenu;
    }

    private Menu createPlaneMenu() {
        Menu planeMenu = new Menu("_Flugzeug");

        MenuItem passengersInPlaneItem = new MenuItem("_Passagiere im Flugzeug...");
        passengersInPlaneItem.setOnAction(new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.PASSENGERS_IN_PLANE, territory));


        MenuItem goLeftItem = new MenuItem("_linksUm");
        ViewUtils.addAccelerator(goLeftItem, "SHORTCUT+ALT+L");
        goLeftItem.setOnAction(new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.LEFT, territory));


        MenuItem forwardItem = new MenuItem("_vor");
        ViewUtils.addAccelerator(forwardItem, "SHORTCUT+ALT+V");
        forwardItem.setOnAction(new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.FORWARD, territory));


        MenuItem onboardenItem = new MenuItem("o_nboarden");
        ViewUtils.addAccelerator(onboardenItem, "SHORTCUT+ALT+N");
        onboardenItem.setOnAction(new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.BOARD_ON, territory));


        MenuItem offboardenItem = new MenuItem("off_boarden");
        ViewUtils.addAccelerator(offboardenItem, "SHORTCUT+ALT+B");
        offboardenItem.setOnAction(new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.BOARD_OFF, territory));


        planeMenu.getItems().addAll(
                passengersInPlaneItem,
                goLeftItem,
                forwardItem,
                onboardenItem,
                offboardenItem
        );

        return planeMenu;
    }

    private Menu createSimulationMenu() {
        Menu simulationMenu = new Menu("_Simulation");

        MenuItem startItem = new MenuItem("S_tart / Fortsetzen",
                ViewUtils.createImage("/resources/Play16.gif"));
        ViewUtils.addAccelerator(startItem, "SHORTCUT+F10");

        MenuItem pauseItem = new MenuItem("_Pause",
                ViewUtils.createImage("/resources/Pause16.gif"));
        ViewUtils.addAccelerator(pauseItem, "SHORTCUT+F11");

        MenuItem stopItem = new MenuItem("St_op",
                ViewUtils.createImage("/resources/Stop16.gif"));
        ViewUtils.addAccelerator(stopItem, "SHORTCUT+F12");

        simulationMenu.getItems().addAll(
                startItem,
                pauseItem,
                stopItem
        );

        return simulationMenu;
    }

    private ToolBar createToolbar() {
        ToolBar toolBar = new ToolBar();

        Button newButton = createTooltipButton("/resources/New24.gif", "Neu");
        Button openButton = createTooltipButton("/resources/Open24.gif", "Öffnen");
        Button saveButton = createTooltipButton("/resources/Save24.gif", "Speichern");
        Button compileButton = createTooltipButton("/resources/Compile24.gif", "Kompilieren");
        Button changeTerrainSizeButton = createTooltipButton("/resources/Terrain24.gif", "Größe ändern");
        changeTerrainSizeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ChangeSizeEventHandler<>(territory));

        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton planeButton = createTooltipButton("/resources/Plane24.png", "Flugzeug platzieren", toggleGroup, PlacingState.State.PLANE);
        ToggleButton passengerButton = createTooltipButton("/resources/Passenger24.png", "Passagier platzieren", toggleGroup, PlacingState.State.PASSENGER);
        ToggleButton thunderstormButton = createTooltipButton("/resources/Thunderstorm24.png", "Gewitter platzieren", toggleGroup, PlacingState.State.THUNDERSTORM);
        ToggleButton deleteTileButton = createTooltipButton("/resources/Delete24.gif", "Kachel löschen", toggleGroup, PlacingState.State.DELETE);

        toggleGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            Toggle selectedToggle = toggleGroup.getSelectedToggle();
            if (selectedToggle != null) {
                PlacingState.getState().setSelected((PlacingState.State) selectedToggle.getUserData());
            }
        }));

        Button passengersInPlaneButton = createTooltipButton("/resources/PlanePassenger24.png", "Passagiere im Flugzeug");
        passengersInPlaneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.PASSENGERS_IN_PLANE, territory));
        Button planeLeftButton = createTooltipButton("/resources/PlaneLeft24.png", "linksUm");
        planeLeftButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.LEFT, territory));
        Button planeForwardButton = createTooltipButton("/resources/PlaneMove24.png", "vor");
        planeForwardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.FORWARD, territory));
        Button planePickButton = createTooltipButton("/resources/PlanePick24.png", "onboarden");
        planePickButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.BOARD_ON, territory));
        Button planePutButton = createTooltipButton("/resources/PlanePut24.png", "offboarden");
        planePutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new ButtonClickEventHandler<>(ButtonClickEventHandler.Action.BOARD_OFF, territory));
        Button playButton = createTooltipButton("/resources/Play24.gif", "Start / Fortsetzen");
        Button pauseButton = createTooltipButton("/resources/Pause24.gif", "Pause");
        Button stopButton = createTooltipButton("/resources/Stop24.gif", "Stop");

        Slider speedSlider = new Slider(0, 100, 0);
        ViewUtils.addTooltip(speedSlider, "Geschwindigkeit");

        toolBar.getItems().addAll(
                newButton,
                openButton,
                new Separator(),
                saveButton,
                compileButton,
                new Separator(),
                changeTerrainSizeButton,
                planeButton,
                passengerButton,
                thunderstormButton,
                deleteTileButton,
                new Separator(),
                passengersInPlaneButton,
                planeLeftButton,
                planeForwardButton,
                planePickButton,
                planePutButton,
                new Separator(),
                playButton,
                pauseButton,
                stopButton,
                new Separator(),
                speedSlider
        );

        return toolBar;
    }

    private ToggleButton createTooltipButton(String graphicPath, String tooltipText, ToggleGroup toggleGroup, PlacingState.State state) {
        PlacingStateToggleButton button = new PlacingStateToggleButton(state);
        button.setGraphic(ViewUtils.createImage(graphicPath));
        ViewUtils.addTooltip(button, tooltipText);
        button.setToggleGroup(toggleGroup);
        PlacingState.getState().addObserver(button);

        return button;
    }

    private Button createTooltipButton(String graphicPath, String tooltipText) {
        Button button = new Button();
        button.setGraphic(ViewUtils.createImage(graphicPath));
        ViewUtils.addTooltip(button, tooltipText);

        return button;
    }

    private SplitPane createSplitPane() {
        SplitPane splitPane = new SplitPane();

        TextArea textArea = new TextArea("void main {\n\t\n}");
        textArea.setStyle("-fx-font-family: 'monospaced';");

        ScrollPane scrollPane = new ScrollPane(territoryPane);

        VBox vBox = new VBox(scrollPane);
        HBox hBox = new HBox(vBox);

        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        splitPane.getItems().addAll(textArea, hBox);

        return splitPane;
    }
}
