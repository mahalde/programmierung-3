package cmd;

import javafx.application.Application;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import simulator.Territory;
import simulator.TerritoryPane;

/**
 * The main class for starting the flight simulator software
 */
public class FlightSimulator extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = createScene();
        primaryStage.setTitle("Java Flugsimulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene createScene() {
        BorderPane root = new BorderPane();
        MenuBar menuBar = createMenuBar();

        root.setTop(menuBar);

        BorderPane contentPane = new BorderPane();
        ToolBar toolBar = createToolbar();
        contentPane.setTop(toolBar);

        SplitPane content = createSplitPane();
        contentPane.setCenter(content);
        content.getItems().get(0).requestFocus();

        Label label = new Label("Herzlich willkommen!");
        contentPane.setBottom(label);

        BorderPane.setAlignment(toolBar, Pos.CENTER_LEFT);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);

        root.setCenter(contentPane);

        return new Scene(root, 1200, 800);
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
                Utils.createImage("/resources/New16.gif"));
        Utils.addAccelerator(newItem, "SHORTCUT+N");

        MenuItem openItem = new MenuItem("_Öffnen",
                Utils.createImage("/resources/Open16.gif"));
        Utils.addAccelerator(openItem, "SHORTCUT+O");

        MenuItem compileItem = new MenuItem("_Kompilieren");
        Utils.addAccelerator(compileItem, "SHORTCUT+K");

        MenuItem printItem = new MenuItem("_Drucken",
                Utils.createImage("/resources/Print16.gif"));
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

        ToggleGroup placeSomethingGroup = new ToggleGroup();
        RadioMenuItem placePlane = new RadioMenuItem("Flug_zeug platzieren");
        RadioMenuItem placePassenger = new RadioMenuItem("_Passagier platzieren");
        RadioMenuItem placeThunderstorm = new RadioMenuItem("Ge_witter platzieren");
        RadioMenuItem deleteField = new RadioMenuItem("Ka_chel löschen");

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

        MenuItem goLeftItem = new MenuItem("_linksUm");
        Utils.addAccelerator(goLeftItem, "SHORTCUT+ALT+L");

        MenuItem forwardItem = new MenuItem("_vor");
        Utils.addAccelerator(forwardItem, "SHORTCUT+ALT+V");

        MenuItem onboardenItem = new MenuItem("o_nboarden");
        Utils.addAccelerator(onboardenItem, "SHORTCUT+ALT+N");

        MenuItem offboardenItem = new MenuItem("off_boarden");
        Utils.addAccelerator(offboardenItem, "SHORTCUT+ALT+B");

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
                Utils.createImage("/resources/Play16.gif"));
        Utils.addAccelerator(startItem, "SHORTCUT+F10");

        MenuItem pauseItem = new MenuItem("_Pause",
                Utils.createImage("/resources/Pause16.gif"));
        Utils.addAccelerator(pauseItem, "SHORTCUT+F11");

        MenuItem stopItem = new MenuItem("St_op",
                Utils.createImage("/resources/Stop16.gif"));
        Utils.addAccelerator(stopItem, "SHORTCUT+F12");

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
        Button planeButton = createTooltipButton("/resources/Plane24.png", "Flugzeug platzieren");
        Button passengerButton = createTooltipButton("/resources/Passenger24.png", "Passagier platzieren");
        Button thunderstormButton = createTooltipButton("/resources/Thunderstorm24.png", "Gewitter platzieren");
        Button deleteTileButton = createTooltipButton("/resources/Delete24.gif", "Kachel löschen");
        Button passengersInPlaneButton = createTooltipButton("/resources/PlanePassenger24.png", "Passagiere im Flugzeug");
        Button planeLeftButton = createTooltipButton("/resources/PlaneLeft24.png", "linksUm");
        Button planeForwardButton = createTooltipButton("/resources/PlaneMove24.png", "vor");
        Button planePickButton = createTooltipButton("/resources/PlanePick24.png", "onboarden");
        Button planePutButton = createTooltipButton("/resources/PlanePut24.png", "offboarden");
        Button playButton = createTooltipButton("/resources/Play24.gif", "Start / Fortsetzen");
        Button pauseButton = createTooltipButton("/resources/Pause24.gif", "Pause");
        Button stopButton = createTooltipButton("/resources/Stop24.gif", "Stop");

        Slider speedSlider = new Slider(0, 100, 0);
        Utils.addTooltip(speedSlider, "Geschwindigkeit");

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

    private Button createTooltipButton(String graphicPath, String tooltipText) {
        Button button = new Button();
        button.setGraphic(Utils.createImage(graphicPath));
        Utils.addTooltip(button, tooltipText);

        return button;
    }

    private SplitPane createSplitPane() {
        SplitPane splitPane = new SplitPane();

        TextArea textArea = new TextArea("// Hier kannst du Code schreiben");
        textArea.setStyle("-fx-font-family: 'monospaced';");

        ScrollPane scrollPane = new ScrollPane(new TerritoryPane(new Territory(12, 8)));

        VBox vBox = new VBox(scrollPane);
        HBox hBox = new HBox(vBox);

        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        splitPane.getItems().addAll(textArea, hBox);

        return splitPane;
    }
}
